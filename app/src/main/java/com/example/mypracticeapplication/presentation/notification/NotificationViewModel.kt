package com.example.mypracticeapplication.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mypracticeapplication.presentation.notification.di.NotificationDependencyProvider
import com.example.mypracticeapplication.domain.notification.model.Notification
import com.example.mypracticeapplication.domain.notification.usecase.AddNotificationUseCase
import com.example.mypracticeapplication.domain.notification.usecase.ClearNotificationsUseCase
import com.example.mypracticeapplication.domain.notification.usecase.DeleteNotificationUseCase
import com.example.mypracticeapplication.domain.notification.usecase.GetNotificationsUseCase
import com.example.mypracticeapplication.domain.notification.usecase.RestoreNotificationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class NotificationViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val addNotificationUseCase: AddNotificationUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val restoreNotificationUseCase: RestoreNotificationUseCase,
    private val clearNotificationsUseCase: ClearNotificationsUseCase
) : ViewModel() {

    private val _oneTimeEvent = Channel<NotificationOneTimeEvent>(Channel.BUFFERED)
    val oneTimeEvent = _oneTimeEvent.receiveAsFlow()
    
    // We only need local state for error messages now
    private val _errorMessage = MutableStateFlow<String?>(null)

    // The single source of truth from Domain layer
    val uiState: StateFlow<NotificationUiState> = combine(
        getNotificationsUseCase(),
        _errorMessage
    ) { notifications, error ->
        NotificationUiState(
            notifications = notifications.map {
                NotificationItem(
                    id = it.id,
                    title = it.title,
                    message = it.message,
                    timestamp = it.timestamp
                )
            },
            isLoading = false,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotificationUiState(isLoading = true)
    )

    fun onEvent(event: NotificationUiEvent) {
        when (event) {
            is NotificationUiEvent.AddMessageClicked -> addMessage()
            is NotificationUiEvent.AddAlertClicked -> addAlert()
            is NotificationUiEvent.ClearAllClicked -> clearAll()
            is NotificationUiEvent.DeleteNotificationClicked -> deleteNotification(event.item)
            is NotificationUiEvent.DismissError -> dismissError()
            is NotificationUiEvent.UndoDeleteClicked -> undoDelete()
            is NotificationUiEvent.AddBigTextClicked -> addBigText()
            is NotificationUiEvent.AddBigPictureClicked -> addBigPicture()
            is NotificationUiEvent.AddInboxClicked -> addInbox()
            is NotificationUiEvent.AddActionNotificationClicked -> addActionNotif()
        }
    }

    private var recentlyDeletedNotification: Notification? = null
    private var recentlyDeletedIndex: Int = -1

    private fun addMessage() {
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            title = "New Message",
            message = "You have a new message from Alice",
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            addNotificationUseCase(notification)
            sendOneTimeEvent(NotificationOneTimeEvent.FireSystemNotification(
                type = NotificationOneTimeEvent.NotificationType.BASIC,
                title = notification.title, 
                message = notification.message
            ))
        }
    }

    private fun addAlert() {
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            title = "System Alert",
            message = "Battery is running low (15%)",
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            addNotificationUseCase(notification)
            sendOneTimeEvent(NotificationOneTimeEvent.FireSystemNotification(
                type = NotificationOneTimeEvent.NotificationType.BASIC,
                title = notification.title, 
                message = notification.message
            ))
        }
    }

    private fun addBigText() {
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            title = "Big Text Notification",
            message = "Expand this notification to see the full story.",
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            addNotificationUseCase(notification)
            sendOneTimeEvent(NotificationOneTimeEvent.FireSystemNotification(
                type = NotificationOneTimeEvent.NotificationType.BIG_TEXT,
                title = notification.title, 
                message = notification.message
            ))
        }
    }

    private fun addBigPicture() {
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            title = "Big Picture Notification",
            message = "Expand to see the image.",
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            addNotificationUseCase(notification)
            sendOneTimeEvent(NotificationOneTimeEvent.FireSystemNotification(
                type = NotificationOneTimeEvent.NotificationType.BIG_PICTURE,
                title = notification.title, 
                message = notification.message
            ))
        }
    }

    private fun addInbox() {
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            title = "Inbox Notification",
            message = "Expand to see multiple lines.",
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            addNotificationUseCase(notification)
            sendOneTimeEvent(NotificationOneTimeEvent.FireSystemNotification(
                type = NotificationOneTimeEvent.NotificationType.INBOX,
                title = notification.title, 
                message = notification.message
            ))
        }
    }

    private fun addActionNotif() {
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            title = "Action Notification",
            message = "This notification has buttons.",
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            addNotificationUseCase(notification)
            sendOneTimeEvent(NotificationOneTimeEvent.FireSystemNotification(
                type = NotificationOneTimeEvent.NotificationType.ACTION,
                title = notification.title, 
                message = notification.message
            ))
        }
    }

    private fun clearAll() {
        viewModelScope.launch {
            clearNotificationsUseCase()
            sendOneTimeEvent(NotificationOneTimeEvent.ShowSnackbar("All notifications cleared"))
        }
    }

    private fun deleteNotification(item: NotificationItem) {
        // Find index in current state
        val currentList = uiState.value.notifications
        recentlyDeletedIndex = currentList.indexOf(item)
        
        // Map back to Domain Model for restoration
        recentlyDeletedNotification = Notification(
            id = item.id,
            title = item.title,
            message = item.message,
            timestamp = item.timestamp
        )

        viewModelScope.launch {
            deleteNotificationUseCase(item.id)
            sendOneTimeEvent(
                NotificationOneTimeEvent.ShowSnackbar(
                    message = "Notification deleted",
                    actionLabel = "Undo",
                    deletedItem = item
                )
            )
        }
    }

    private fun dismissError() {
        _errorMessage.value = null
    }

    private fun undoDelete() {
        val notification = recentlyDeletedNotification ?: return
        val index = if (recentlyDeletedIndex >= 0) recentlyDeletedIndex else 0

        viewModelScope.launch {
            restoreNotificationUseCase(index, notification)
            recentlyDeletedNotification = null
            recentlyDeletedIndex = -1
            sendOneTimeEvent(NotificationOneTimeEvent.ShowSnackbar("Restored notification"))
        }
    }

    private fun sendOneTimeEvent(event: NotificationOneTimeEvent) {
        viewModelScope.launch {
            _oneTimeEvent.send(event)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = NotificationDependencyProvider.repository
                return NotificationViewModel(
                    GetNotificationsUseCase(repo),
                    AddNotificationUseCase(repo),
                    DeleteNotificationUseCase(repo),
                    RestoreNotificationUseCase(repo),
                    ClearNotificationsUseCase(repo)
                ) as T
            }
        }
    }
}



