package com.example.mypracticeapplication.presentation.notification

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class NotificationUiState(
    val notifications: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


