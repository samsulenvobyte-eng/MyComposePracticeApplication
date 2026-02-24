package com.example.mypracticeapplication.presentation.practice.ui_architecture


enum class Status {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}

data class Level1UiState(
    val status: Status = Status.PENDING
)


