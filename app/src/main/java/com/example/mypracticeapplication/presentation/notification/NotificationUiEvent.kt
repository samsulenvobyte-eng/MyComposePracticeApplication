package com.example.mypracticeapplication.presentation.notification

sealed interface NotificationUiEvent {
    object AddMessageClicked : NotificationUiEvent
    object AddAlertClicked : NotificationUiEvent
    object ClearAllClicked : NotificationUiEvent
    data class DeleteNotificationClicked(val item: NotificationItem) : NotificationUiEvent
    object DismissError : NotificationUiEvent
    object UndoDeleteClicked : NotificationUiEvent
    
    // Advanced notification types
    object AddBigTextClicked : NotificationUiEvent
    object AddBigPictureClicked : NotificationUiEvent
    object AddInboxClicked : NotificationUiEvent
    object AddActionNotificationClicked : NotificationUiEvent
}


