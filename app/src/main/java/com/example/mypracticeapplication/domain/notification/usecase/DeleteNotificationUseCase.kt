package com.example.mypracticeapplication.domain.notification.usecase

import com.example.mypracticeapplication.domain.notification.repository.NotificationRepository

class DeleteNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteNotification(id)
    }
}



