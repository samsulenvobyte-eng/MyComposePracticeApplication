package com.example.mypracticeapplication.presentation.notification.di

import com.example.mypracticeapplication.data.notification.repository.NotificationRepositoryImpl
import com.example.mypracticeapplication.domain.notification.repository.NotificationRepository

object NotificationDependencyProvider {
    val repository: NotificationRepository by lazy {
        NotificationRepositoryImpl()
    }
}
