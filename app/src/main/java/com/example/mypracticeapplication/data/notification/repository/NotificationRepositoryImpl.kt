package com.example.mypracticeapplication.data.notification.repository

import com.example.mypracticeapplication.domain.notification.model.Notification
import com.example.mypracticeapplication.domain.notification.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationRepositoryImpl : NotificationRepository {
    // In-memory cache representing a database or remote source
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())

    override fun getNotifications(): Flow<List<Notification>> {
        return _notifications.asStateFlow()
    }

    override suspend fun addNotification(notification: Notification) {
        _notifications.update { currentList ->
            listOf(notification) + currentList
        }
    }

    override suspend fun deleteNotification(id: String) {
        _notifications.update { currentList ->
            currentList.filterNot { it.id == id }
        }
    }

    override suspend fun restoreNotification(index: Int, notification: Notification) {
        _notifications.update { currentList ->
            val mutableList = currentList.toMutableList()
            // Ensure index is within bounds
            val safeIndex = index.coerceIn(0, mutableList.size)
            mutableList.add(safeIndex, notification)
            mutableList
        }
    }

    override suspend fun clearAll() {
        _notifications.value = emptyList()
    }
}



