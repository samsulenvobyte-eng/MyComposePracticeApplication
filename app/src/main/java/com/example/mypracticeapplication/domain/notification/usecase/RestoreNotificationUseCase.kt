package com.example.mypracticeapplication.domain.notification.usecase

import com.example.mypracticeapplication.domain.notification.model.Notification
import com.example.mypracticeapplication.domain.notification.repository.NotificationRepository

class RestoreNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(index: Int, notification: Notification) {
        repository.restoreNotification(index, notification)
    }
}



