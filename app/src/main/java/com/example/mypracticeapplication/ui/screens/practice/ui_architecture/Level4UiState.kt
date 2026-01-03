package com.example.mypracticeapplication.ui.screens.practice.ui_architecture

/**
 * Level 4: Events & One-Time Actions Architecture
 * 
 * This file demonstrates proper event handling in Android UI Architecture:
 * 1. UiState - Observable state that drives the UI
 * 2. UiEvent (User Intent) - Actions from the user that the ViewModel handles
 * 3. OneTimeEvent - Side effects like navigation, snackbar, etc.
 */

// ═══════════════════════════════════════════════════════════════════════════
// 1️⃣ UI STATE - What the screen displays
// ═══════════════════════════════════════════════════════════════════════════

data class Level4UiState(
    val tasks: List<Task> = emptyList(),
    val newTaskTitle: String = "",
    val selectedFilter: TaskFilter = TaskFilter.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    // Derived properties - computed from state
    val filteredTasks: List<Task> get() = when (selectedFilter) {
        TaskFilter.ALL -> tasks
        TaskFilter.ACTIVE -> tasks.filter { !it.isCompleted }
        TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
    }
    
    val completedCount: Int get() = tasks.count { it.isCompleted }
    val activeCount: Int get() = tasks.count { !it.isCompleted }
    val totalCount: Int get() = tasks.size
    val progress: Float get() = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    
    val canAddTask: Boolean get() = newTaskTitle.isNotBlank()
}

data class Task(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val createdAt: Long = System.currentTimeMillis()
)

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class TaskFilter {
    ALL, ACTIVE, COMPLETED
}

// ═══════════════════════════════════════════════════════════════════════════
// 2️⃣ UI EVENTS (User Intents) - Actions the user performs
// ═══════════════════════════════════════════════════════════════════════════
/**
 * Sealed interface for all user events.
 * Benefits:
 * - Type-safe: Compiler ensures all events are handled
 * - Extensible: Easy to add new events
 * - Testable: Each event is a clear, isolated action
 * - Traceable: Easy to log/debug user interactions
 */
sealed interface Level4UiEvent {
    // Task Input Events
    data class TaskTitleChanged(val title: String) : Level4UiEvent
    data object AddTaskClicked : Level4UiEvent
    
    // Task Action Events
    data class TaskCheckedChanged(val taskId: String, val isCompleted: Boolean) : Level4UiEvent
    data class DeleteTaskClicked(val taskId: String) : Level4UiEvent
    data class TaskPriorityChanged(val taskId: String, val priority: Priority) : Level4UiEvent
    
    // Filter Events
    data class FilterSelected(val filter: TaskFilter) : Level4UiEvent
    
    // Bulk Actions
    data object ClearCompletedClicked : Level4UiEvent
    data object MarkAllCompleteClicked : Level4UiEvent
    
    // Error Handling
    data object DismissError : Level4UiEvent
    data object RetryClicked : Level4UiEvent
    
    // Undo Action (triggered from Snackbar)
    data class UndoDeleteClicked(val task: Task) : Level4UiEvent
}

// ═══════════════════════════════════════════════════════════════════════════
// 3️⃣ ONE-TIME EVENTS - Side effects (navigation, snackbar, etc.)
// ═══════════════════════════════════════════════════════════════════════════
/**
 * One-time events are side effects that should only be consumed once.
 * They're NOT part of UiState because:
 * - They shouldn't survive configuration changes
 * - They should only be triggered once (not re-displayed on recomposition)
 * 
 * Examples: Snackbar, Navigation, Toast, Dialog dismiss
 */
sealed interface Level4OneTimeEvent {
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = "Hello",
        val deletedTask: Task? = null  // For undo functionality
    ) : Level4OneTimeEvent
    
    data class NavigateToTaskDetail(val taskId: String) : Level4OneTimeEvent
    data object NavigateBack : Level4OneTimeEvent
    data object ShowConfirmationDialog : Level4OneTimeEvent
}
