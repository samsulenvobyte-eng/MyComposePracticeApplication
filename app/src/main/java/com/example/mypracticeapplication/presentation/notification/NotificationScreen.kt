package com.example.mypracticeapplication.presentation.notification

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.mypracticeapplication.core.NotificationHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Formatter created once outside to avoid recreation on every item recomposition
private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onBackClick: () -> Unit,
    viewModel: NotificationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Launcher for POST_NOTIFICATIONS permission
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // We handle the result silently or show a message if desired
    }

    LaunchedEffect(Unit) {
        NotificationHelper.createNotificationChannel(context)

        // Make sure we have permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        
        viewModel.oneTimeEvent.collectLatest { event ->
            when (event) {
                is NotificationOneTimeEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        duration = SnackbarDuration.Short
                    )
                    
                    if (result == SnackbarResult.ActionPerformed && event.deletedItem != null) {
                        viewModel.onEvent(NotificationUiEvent.UndoDeleteClicked)
                    }
                }
                is NotificationOneTimeEvent.FireSystemNotification -> {
                    when (event.type) {
                        NotificationOneTimeEvent.NotificationType.BASIC -> {
                            NotificationHelper.showBasicNotification(context, event.title, event.message)
                        }
                        NotificationOneTimeEvent.NotificationType.BIG_TEXT -> {
                            NotificationHelper.showBigTextNotification(context, event.title, event.message)
                        }
                        NotificationOneTimeEvent.NotificationType.BIG_PICTURE -> {
                            NotificationHelper.showBigPictureNotification(context, event.title, event.message)
                        }
                        NotificationOneTimeEvent.NotificationType.INBOX -> {
                            NotificationHelper.showInboxNotification(context, event.title)
                        }
                        NotificationOneTimeEvent.NotificationType.ACTION -> {
                            NotificationHelper.showActionNotification(context, event.title, event.message)
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF2C3E50),
                    contentColor = Color.White,
                    actionColor = Color(0xFF6dd5ed)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Notifications", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2C3E50),
                            Color(0xFF4CA1AF)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Control Panel
                ControlPanel(onEvent = viewModel::onEvent)

                Spacer(modifier = Modifier.height(24.dp))

                // List
                if (uiState.notifications.isEmpty()) {
                    EmptyState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.notifications,
                            key = { it.id }
                        ) { item ->
                            NotificationItemView(
                                item = item,
                                onDeleteClick = {
                                    viewModel.onEvent(NotificationUiEvent.DeleteNotificationClicked(item))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlPanel(onEvent: (NotificationUiEvent) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Notification Styles Demo",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            
            // Row 1: Basic types
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onEvent(NotificationUiEvent.AddMessageClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6dd5ed), contentColor = Color.Black),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Basic Msg", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { onEvent(NotificationUiEvent.AddAlertClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B), contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Basic Alert", fontWeight = FontWeight.SemiBold)
                }
            }
            
            // Row 2: Advanced visual styles
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onEvent(NotificationUiEvent.AddBigTextClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CA1AF), contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Big Text", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { onEvent(NotificationUiEvent.AddBigPictureClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0), contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Big Picture", fontWeight = FontWeight.SemiBold)
                }
            }
            
            // Row 3: Complex layout styles
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onEvent(NotificationUiEvent.AddInboxClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800), contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Inbox View", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { onEvent(NotificationUiEvent.AddActionNotificationClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50), contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Action Btns", fontWeight = FontWeight.SemiBold)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = { onEvent(NotificationUiEvent.ClearAllClicked) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f), contentColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear History")
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No notifications yet",
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun NotificationItemView(
    item: NotificationItem,
    onDeleteClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C3E50).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = Color(0xFF2C3E50)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = item.message,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = timeFormatter.format(Date(item.timestamp)),
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Gray.copy(alpha = 0.6f)
                )
            }
        }
    }
}


