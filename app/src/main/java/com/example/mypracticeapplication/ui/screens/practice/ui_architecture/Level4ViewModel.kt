package com.example.mypracticeapplication.ui.screens.practice.ui_architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Level 4 ViewModel: Demonstrates proper event handling
 * 
 * Key Architecture Patterns:
 * 1. Single onEvent() function handles ALL user events
 * 2. StateFlow for UI State (survives config changes)
 * 3. Channel for One-Time Events (consumed once)
 * 4. Clean separation: Events IN → State/Effects OUT
 */
class Level4ViewModel : ViewModel() {

    // ════════════════════════════════════════════════════════════════════════
    // STATE & EVENTS SETUP
    // ════════════════════════════════════════════════════════════════════════
    
    private val _uiState = MutableStateFlow(Level4UiState())
    val uiState: StateFlow<Level4UiState> = _uiState.asStateFlow()
    
    // Channel for one-time events - ensures events are consumed only once
    // Unlike StateFlow, Channel doesn't replay old events on new collectors
    private val _oneTimeEvent = Channel<Level4OneTimeEvent>(Channel.BUFFERED)
    val oneTimeEvent = _oneTimeEvent.receiveAsFlow()
    
    init {
        // Load initial sample data
        loadSampleTasks()
    }
    
    // ════════════════════════════════════════════════════════════════════════
    // SINGLE ENTRY POINT FOR ALL EVENTS
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Single function that handles ALL user events.
     * 
     * Benefits of this pattern:
     * - Centralized event handling logic
     * - Easy to trace what happens for each event
     * - When exhaustive - compiler ensures all events are handled
     * - Perfect for logging/analytics
     */
    fun onEvent(event: Level4UiEvent) {
        when (event) {
            // ─────────────────────────────────────────────────────────────────
            // Task Input Events
            // ─────────────────────────────────────────────────────────────────
            is Level4UiEvent.TaskTitleChanged -> {
                _uiState.update { it.copy(newTaskTitle = event.title) }
            }
            
            is Level4UiEvent.AddTaskClicked -> {
                addNewTask()
            }
            
            // ─────────────────────────────────────────────────────────────────
            // Task Action Events
            // ─────────────────────────────────────────────────────────────────
            is Level4UiEvent.TaskCheckedChanged -> {
                toggleTaskCompletion(event.taskId, event.isCompleted)
            }
            
            is Level4UiEvent.DeleteTaskClicked -> {
                deleteTask(event.taskId)
            }
            
            is Level4UiEvent.TaskPriorityChanged -> {
                changeTaskPriority(event.taskId, event.priority)
            }
            
            // ─────────────────────────────────────────────────────────────────
            // Filter Events
            // ─────────────────────────────────────────────────────────────────
            is Level4UiEvent.FilterSelected -> {
                _uiState.update { it.copy(selectedFilter = event.filter) }
            }
            
            // ─────────────────────────────────────────────────────────────────
            // Bulk Actions
            // ─────────────────────────────────────────────────────────────────
            is Level4UiEvent.ClearCompletedClicked -> {
                clearCompletedTasks()
            }
            
            is Level4UiEvent.MarkAllCompleteClicked -> {
                markAllComplete()
            }
            
            // ─────────────────────────────────────────────────────────────────
            // Error & Undo Handling
            // ─────────────────────────────────────────────────────────────────
            is Level4UiEvent.DismissError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            
            is Level4UiEvent.RetryClicked -> {
                loadSampleTasks()
            }
            
            is Level4UiEvent.UndoDeleteClicked -> {
                undoDelete(event.task)
            }
        }
    }
    
    // ════════════════════════════════════════════════════════════════════════
    // PRIVATE BUSINESS LOGIC
    // ════════════════════════════════════════════════════════════════════════
    
    private fun loadSampleTasks() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        // Simulate loading (in real app, this would be a repository call)
        val sampleTasks = listOf(
            Task(
                id = UUID.randomUUID().toString(),
                title = "Learn UI Architecture patterns",
                isCompleted = true,
                priority = Priority.HIGH
            ),
            Task(
                id = UUID.randomUUID().toString(),
                title = "Implement Event-based ViewModel",
                isCompleted = false,
                priority = Priority.HIGH
            ),
            Task(
                id = UUID.randomUUID().toString(),
                title = "Practice sealed classes",
                isCompleted = false,
                priority = Priority.MEDIUM
            ),
            Task(
                id = UUID.randomUUID().toString(),
                title = "Add One-Time Events",
                isCompleted = false,
                priority = Priority.MEDIUM
            ),
            Task(
                id = UUID.randomUUID().toString(),
                title = "Review code with team",
                isCompleted = false,
                priority = Priority.LOW
            )
        )
        
        _uiState.update { 
            it.copy(
                tasks = sampleTasks,
                isLoading = false
            )
        }
    }
    
    private fun addNewTask() {
        val currentTitle = _uiState.value.newTaskTitle.trim()
        if (currentTitle.isBlank()) return
        
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            title = currentTitle,
            priority = Priority.MEDIUM
        )
        
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks + newTask,
                newTaskTitle = "" // Clear input after adding
            )
        }
        
        // Send one-time event to show feedback
        sendOneTimeEvent(
            Level4OneTimeEvent.ShowSnackbar("Task added: $currentTitle")
        )
    }
    
    private fun toggleTaskCompletion(taskId: String, isCompleted: Boolean) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map { task ->
                    if (task.id == taskId) task.copy(isCompleted = isCompleted)
                    else task
                }
            )
        }
    }
    
    private fun deleteTask(taskId: String) {
        val taskToDelete = _uiState.value.tasks.find { it.id == taskId } ?: return
        
        _uiState.update { state ->
            state.copy(tasks = state.tasks.filter { it.id != taskId })
        }
        
        // Send one-time event with undo action
        sendOneTimeEvent(
            Level4OneTimeEvent.ShowSnackbar(
                message = "Task deleted",
                actionLabel = "Undo",
                deletedTask = taskToDelete
            )
        )
    }
    
    private fun undoDelete(task: Task) {
        _uiState.update { state ->
            // Add task back at original position (simplified - adds to start)
            state.copy(tasks = listOf(task) + state.tasks)
        }
        
        sendOneTimeEvent(
            Level4OneTimeEvent.ShowSnackbar("Task restored")
        )
    }
    
    private fun changeTaskPriority(taskId: String, priority: Priority) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map { task ->
                    if (task.id == taskId) task.copy(priority = priority)
                    else task
                }
            )
        }
    }
    
    private fun clearCompletedTasks() {
        val completedCount = _uiState.value.completedCount
        if (completedCount == 0) return
        
        _uiState.update { state ->
            state.copy(tasks = state.tasks.filter { !it.isCompleted })
        }
        
        sendOneTimeEvent(
            Level4OneTimeEvent.ShowSnackbar("Cleared $completedCount completed tasks")
        )
    }
    
    private fun markAllComplete() {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map { it.copy(isCompleted = true) }
            )
        }
        
        sendOneTimeEvent(
            Level4OneTimeEvent.ShowSnackbar("All tasks marked complete! 🎉")
        )
    }
    
    // ════════════════════════════════════════════════════════════════════════
    // ONE-TIME EVENT DISPATCHER
    // ════════════════════════════════════════════════════════════════════════
    
    private fun sendOneTimeEvent(event: Level4OneTimeEvent) {
        viewModelScope.launch {
            _oneTimeEvent.send(event)
        }
    }
}
