package com.example.mypracticeapplication.domain.notification.repository

import com.example.mypracticeapplication.domain.notification.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun addNotification(notification: Notification)
    suspend fun deleteNotification(id: String)
    suspend fun restoreNotification(index: Int, notification: Notification)
    suspend fun clearAll()
}



