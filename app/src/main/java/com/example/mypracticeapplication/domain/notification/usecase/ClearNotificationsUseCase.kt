package com.example.mypracticeapplication.domain.notification.usecase

import com.example.mypracticeapplication.domain.notification.repository.NotificationRepository

class ClearNotificationsUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke() {
        repository.clearAll()
    }
}



