package com.example.mypracticeapplication.domain.notification.usecase

import com.example.mypracticeapplication.domain.notification.model.Notification
import com.example.mypracticeapplication.domain.notification.repository.NotificationRepository

class AddNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: Notification) {
        repository.addNotification(notification)
    }
}



