package com.example.mypracticeapplication.domain.notification.usecase

import com.example.mypracticeapplication.domain.notification.model.Notification
import com.example.mypracticeapplication.domain.notification.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<Notification>> {
        return repository.getNotifications()
    }
}



