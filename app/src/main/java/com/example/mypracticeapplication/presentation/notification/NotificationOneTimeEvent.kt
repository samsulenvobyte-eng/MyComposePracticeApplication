package com.example.mypracticeapplication.presentation.notification

sealed interface NotificationOneTimeEvent {
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val deletedItem: NotificationItem? = null
    ) : NotificationOneTimeEvent
    
    enum class NotificationType {
        BASIC, BIG_TEXT, BIG_PICTURE, INBOX, ACTION
    }

    data class FireSystemNotification(
        val type: NotificationType,
        val title: String,
        val message: String
    ) : NotificationOneTimeEvent
}


