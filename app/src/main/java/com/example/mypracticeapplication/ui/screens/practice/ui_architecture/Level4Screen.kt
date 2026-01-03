package com.example.mypracticeapplication.ui.screens.practice.ui_architecture

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Level 4 Screen: Demonstrates Events-based UI Architecture
 * 
 * Architecture Pattern:
 * ┌─────────────────────────────────────────────────────────────────┐
 * │                          SCREEN                                 │
 * │  ┌──────────────┐         ┌──────────────────────────┐         │
 * │  │   UI State   │ ←─────  │       ViewModel          │         │
 * │  │  (StateFlow) │         │  onEvent(UiEvent)        │         │
 * │  └──────────────┘         └──────────────────────────┘         │
 * │         │                           ↑                          │
 * │         ↓                           │                          │
 * │  ┌──────────────┐         ┌─────────┴────────────┐             │
 * │  │  Composable  │ ─────→  │     UiEvent          │             │
 * │  │     UI       │ events  │  (Sealed Interface)  │             │
 * │  └──────────────┘         └──────────────────────┘             │
 * │         ↑                                                       │
 * │         │                                                       │
 * │  ┌──────────────┐                                              │
 * │  │ One-Time     │ ← Channel (Snackbar, Navigation, etc.)      │
 * │  │ Events       │                                              │
 * │  └──────────────┘                                              │
 * └─────────────────────────────────────────────────────────────────┘
 */

// Primary accent color for Level 4
private val PrimaryColor = Color(0xFF6366F1) // Indigo
private val SuccessColor = Color(0xFF10B981) // Green
private val WarningColor = Color(0xFFF59E0B) // Amber
private val DangerColor = Color(0xFFEF4444) // Red

@Composable
fun Level4Screen(
    viewModel: Level4ViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // ════════════════════════════════════════════════════════════════════════
    // ONE-TIME EVENT HANDLING
    // ════════════════════════════════════════════════════════════════════════
    /**
     * LaunchedEffect to collect one-time events from ViewModel.
     * This is the proper way to handle side effects:
     * - Snackbar messages
     * - Navigation
     * - Toasts
     * - Dialog triggers
     */
    LaunchedEffect(Unit) {
        viewModel.oneTimeEvent.collectLatest { event ->
            when (event) {
                is Level4OneTimeEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        duration = SnackbarDuration.Short
                    )
                    
                    // Handle Undo action
                    if (result == SnackbarResult.ActionPerformed && event.deletedTask != null) {
                        viewModel.onEvent(Level4UiEvent.UndoDeleteClicked(event.deletedTask))
                    }
                }
                
                is Level4OneTimeEvent.NavigateToTaskDetail -> {
                    // Handle navigation (would use navController in real app)
                }
                
                is Level4OneTimeEvent.NavigateBack -> {
                    // Handle back navigation
                }
                
                is Level4OneTimeEvent.ShowConfirmationDialog -> {
                    // Show dialog
                }
            }
        }
    }
    
    // ════════════════════════════════════════════════════════════════════════
    // SCREEN CONTENT
    // ════════════════════════════════════════════════════════════════════════
    Level4ScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

/**
 * Stateless Composable - Pure UI based on state
 * All user interactions are converted to events and sent to ViewModel
 */
@Composable
private fun Level4ScreenContent(
    uiState: Level4UiState,
    onEvent: (Level4UiEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = PrimaryColor,
                    contentColor = Color.White,
                    actionColor = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            Header()
            
            // Loading Indicator
            AnimatedVisibility(visible = uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryColor
                )
            }
            
            // Error Message
            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                ErrorBanner(
                    message = uiState.errorMessage ?: "",
                    onDismiss = { onEvent(Level4UiEvent.DismissError) },
                    onRetry = { onEvent(Level4UiEvent.RetryClicked) }
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Progress Section
                ProgressSection(
                    progress = uiState.progress,
                    completedCount = uiState.completedCount,
                    totalCount = uiState.totalCount
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Add Task Input
                AddTaskSection(
                    taskTitle = uiState.newTaskTitle,
                    canAddTask = uiState.canAddTask,
                    onTitleChange = { onEvent(Level4UiEvent.TaskTitleChanged(it)) },
                    onAddClick = { onEvent(Level4UiEvent.AddTaskClicked) }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Filter Chips
                FilterSection(
                    selectedFilter = uiState.selectedFilter,
                    activeCount = uiState.activeCount,
                    completedCount = uiState.completedCount,
                    onFilterSelected = { onEvent(Level4UiEvent.FilterSelected(it)) }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Task List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.filteredTasks,
                        key = { it.id }
                    ) { task ->
                        TaskItem(
                            task = task,
                            onCheckedChange = { isCompleted ->
                                onEvent(Level4UiEvent.TaskCheckedChanged(task.id, isCompleted))
                            },
                            onDeleteClick = {
                                onEvent(Level4UiEvent.DeleteTaskClicked(task.id))
                            },
                            onPriorityChange = { priority ->
                                onEvent(Level4UiEvent.TaskPriorityChanged(task.id, priority))
                            }
                        )
                    }
                    
                    // Empty state
                    if (uiState.filteredTasks.isEmpty()) {
                        item {
                            EmptyState(filter = uiState.selectedFilter)
                        }
                    }
                }
                
                // Bulk Actions
                if (uiState.tasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    BulkActionsSection(
                        hasCompletedTasks = uiState.completedCount > 0,
                        allCompleted = uiState.activeCount == 0,
                        onClearCompleted = { onEvent(Level4UiEvent.ClearCompletedClicked) },
                        onMarkAllComplete = { onEvent(Level4UiEvent.MarkAllCompleteClicked) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryColor)
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Column {
            Text(
                text = "📋 Task Manager",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Level 4: Events Architecture",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun ErrorBanner(
    message: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = DangerColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = DangerColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                modifier = Modifier.weight(1f),
                color = DangerColor
            )
            TextButton(onClick = onRetry) {
                Text("Retry", color = DangerColor)
            }
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = Color.Gray)
            }
        }
    }
}

@Composable
private fun ProgressSection(
    progress: Float,
    completedCount: Int,
    totalCount: Int
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "progress"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$completedCount / $totalCount tasks",
                    color = PrimaryColor,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = SuccessColor,
                trackColor = Color.Gray.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
            
            if (progress >= 1f) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🎉 All tasks completed!",
                    color = SuccessColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun AddTaskSection(
    taskTitle: String,
    canAddTask: Boolean,
    onTitleChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = taskTitle,
            onValueChange = onTitleChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Add a new task...") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (canAddTask) {
                        onAddClick()
                        keyboardController?.hide()
                    }
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                cursorColor = PrimaryColor
            )
        )
        
        Button(
            onClick = {
                onAddClick()
                keyboardController?.hide()
            },
            enabled = canAddTask,
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSection(
    selectedFilter: TaskFilter,
    activeCount: Int,
    completedCount: Int,
    onFilterSelected: (TaskFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskFilter.entries.forEach { filter ->
            val isSelected = selectedFilter == filter
            val count = when (filter) {
                TaskFilter.ALL -> activeCount + completedCount
                TaskFilter.ACTIVE -> activeCount
                TaskFilter.COMPLETED -> completedCount
            }
            
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = "${filter.name} ($count)",
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryColor,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onPriorityChange: (Priority) -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (task.isCompleted)
            SuccessColor.copy(alpha = 0.1f)
        else
            MaterialTheme.colorScheme.surfaceVariant,
        label = "taskBg"
    )
    
    val priorityColor = when (task.priority) {
        Priority.HIGH -> DangerColor
        Priority.MEDIUM -> WarningColor
        Priority.LOW -> Color.Gray
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = SuccessColor,
                    checkmarkColor = Color.White
                )
            )
            
            // Task Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                
                // Priority selector
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Priority.entries.forEach { priority ->
                        val isSelected = task.priority == priority
                        val color = when (priority) {
                            Priority.HIGH -> DangerColor
                            Priority.MEDIUM -> WarningColor
                            Priority.LOW -> Color.Gray
                        }
                        
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 16.dp else 12.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) color else color.copy(alpha = 0.3f))
                                .then(
                                    if (isSelected) Modifier.border(
                                        2.dp,
                                        Color.White,
                                        CircleShape
                                    ) else Modifier
                                )
                                .clickable { onPriorityChange(priority) }
                        )
                    }
                }
            }
            
            // Delete Button
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Task",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun EmptyState(filter: TaskFilter) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (filter) {
                TaskFilter.ALL -> "📝"
                TaskFilter.ACTIVE -> "✨"
                TaskFilter.COMPLETED -> "🎯"
            },
            fontSize = 48.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when (filter) {
                TaskFilter.ALL -> "No tasks yet"
                TaskFilter.ACTIVE -> "No active tasks"
                TaskFilter.COMPLETED -> "No completed tasks"
            },
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Text(
            text = when (filter) {
                TaskFilter.ALL -> "Add your first task above!"
                TaskFilter.ACTIVE -> "Great job completing all tasks!"
                TaskFilter.COMPLETED -> "Complete some tasks to see them here"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun BulkActionsSection(
    hasCompletedTasks: Boolean,
    allCompleted: Boolean,
    onClearCompleted: () -> Unit,
    onMarkAllComplete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (hasCompletedTasks) {
            Button(
                onClick = onClearCompleted,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DangerColor.copy(alpha = 0.1f),
                    contentColor = DangerColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Clear Completed")
            }
        }
        
        if (!allCompleted) {
            Button(
                onClick = onMarkAllComplete,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SuccessColor.copy(alpha = 0.1f),
                    contentColor = SuccessColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Mark All Done")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Level4ScreenPreview() {
    Level4Screen()
}
