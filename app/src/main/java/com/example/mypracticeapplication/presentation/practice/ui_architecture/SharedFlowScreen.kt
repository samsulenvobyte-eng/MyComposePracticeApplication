package com.example.mypracticeapplication.presentation.practice.ui_architecture

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Data class representing a notification event
 */
data class NotificationEvent(
    val id: Int,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * UI State for SharedFlow demo screen
 */
data class SharedFlowUiState(
    val notificationCount: Int = 0,
    val recentNotifications: List<NotificationEvent> = emptyList()
)

/**
 * ViewModel demonstrating MutableSharedFlow usage
 * 
 * Key differences between SharedFlow and StateFlow:
 * - SharedFlow: Emits values to collectors WITHOUT holding a current value (fire-and-forget events)
 * - StateFlow: Always holds a current value and emits it to new collectors
 * 
 * Use SharedFlow for one-time events like:
 * - Toast/Snackbar messages
 * - Navigation events
 * - Notifications
 */
class SharedFlowViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SharedFlowUiState())
    val uiState: StateFlow<SharedFlowUiState> = _uiState.asStateFlow()

    // MutableSharedFlow for one-time notification events
    // replay = 0 means no buffering - new collectors won't receive past events
    private val _notificationFlow = MutableSharedFlow<NotificationEvent>(replay = 0)
    val notificationFlow: SharedFlow<NotificationEvent> = _notificationFlow.asSharedFlow()

    private var notificationIdCounter = 0

    /**
     * Sends a new notification through SharedFlow
     * This demonstrates how SharedFlow is perfect for one-time events
     */
    fun sendNotification(title: String, message: String) {
        viewModelScope.launch {
            val notification = NotificationEvent(
                id = ++notificationIdCounter,
                title = title,
                message = message
            )
            
            // Update UI state with new notification
            _uiState.value = _uiState.value.copy(
                notificationCount = _uiState.value.notificationCount + 1,
                recentNotifications = listOf(notification) + _uiState.value.recentNotifications.take(4)
            )
            
            // Emit the event through SharedFlow - collectors will receive this once
            _notificationFlow.emit(notification)
        }
    }

    /**
     * Sends a quick notification with pre-defined content
     */
    fun sendQuickNotification() {
        val messages = listOf(
            "New message received!" to "You have a new message from John",
            "Update available" to "A new version of the app is ready",
            "Task completed" to "Your download has finished",
            "Reminder" to "Don't forget your meeting at 3 PM",
            "Achievement unlocked" to "You've completed 10 tasks today!"
        )
        val (title, message) = messages.random()
        sendNotification(title, message)
    }

    fun clearNotifications() {
        _uiState.value = SharedFlowUiState()
    }
}

@Composable
fun SharedFlowScreen(
    modifier: Modifier = Modifier,
    viewModel: SharedFlowViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect SharedFlow events - this demonstrates one-time event handling
    LaunchedEffect(Unit) {
        viewModel.notificationFlow.collect { notification ->
            // Show snackbar for each notification event
            snackbarHostState.showSnackbar(
                message = "${notification.title}: ${notification.message}"
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.sendQuickNotification() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Send Notification"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "SharedFlow Demo",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Demonstrating MutableSharedFlow for one-time events",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Notification counter card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Notifications",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${uiState.notificationCount}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.sendQuickNotification() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Send Notification")
                }
                Button(
                    onClick = { viewModel.clearNotifications() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    )
                ) {
                    Text("Clear All")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent notifications list
            Text(
                text = "Recent Notifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.recentNotifications.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "No notifications yet. Tap the button or FAB to send one!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.recentNotifications) { notification ->
                        NotificationCard(notification = notification)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(notification: NotificationEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${notification.id}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SharedFlowScreenPreview() {
    SharedFlowScreen()
}


