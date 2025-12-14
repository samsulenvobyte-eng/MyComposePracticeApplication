package com.example.mypracticeapplication.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypracticeapplication.ui.theme.MyPracticeApplicationTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScreen(
    onNavigateBack: () -> Unit = {}
) {
    var showBasicSheet by remember { mutableStateOf(false) }
    var showActionsSheet by remember { mutableStateOf(false) }
    
    val basicSheetState = rememberModalBottomSheetState()
    val actionsSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bottom Sheet Examples",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6366F1)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6)
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Modal Bottom Sheets",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Tap the buttons to see different bottom sheet examples",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            // Basic Bottom Sheet Button
            Button(
                onClick = { showBasicSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF6366F1)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Show Basic Sheet",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actions Bottom Sheet Button
            Button(
                onClick = { showActionsSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Show Actions Sheet",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    // Basic Bottom Sheet
    if (showBasicSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBasicSheet = false },
            sheetState = basicSheetState,
            containerColor = Color.White,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.Gray.copy(alpha = 0.4f))
                )
            }
        ) {
            BasicSheetContent(
                onDismiss = {
                    scope.launch { basicSheetState.hide() }.invokeOnCompletion {
                        if (!basicSheetState.isVisible) {
                            showBasicSheet = false
                        }
                    }
                }
            )
        }
    }

    // Actions Bottom Sheet
    if (showActionsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showActionsSheet = false },
            sheetState = actionsSheetState,
            containerColor = Color.White,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.Gray.copy(alpha = 0.4f))
                )
            }
        ) {
            ActionsSheetContent(
                onActionClick = { action ->
                    scope.launch { actionsSheetState.hide() }.invokeOnCompletion {
                        if (!actionsSheetState.isVisible) {
                            showActionsSheet = false
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun BasicSheetContent(
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFF6366F1).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF6366F1)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to Bottom Sheet!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "This is a basic modal bottom sheet. You can add any content here like forms, lists, or informational content.",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6366F1)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Got it!",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Cancel",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6B7280)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ActionsSheetContent(
    onActionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Quick Actions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        HorizontalDivider(color = Color(0xFFE5E7EB))

        ActionItem(
            icon = Icons.Default.Edit,
            title = "Edit",
            subtitle = "Make changes to this item",
            iconColor = Color(0xFF3B82F6),
            onClick = { onActionClick("edit") }
        )

        ActionItem(
            icon = Icons.Default.Share,
            title = "Share",
            subtitle = "Share with friends and family",
            iconColor = Color(0xFF10B981),
            onClick = { onActionClick("share") }
        )

        ActionItem(
            icon = Icons.Default.Add,
            title = "Duplicate",
            subtitle = "Create a copy of this item",
            iconColor = Color(0xFF8B5CF6),
            onClick = { onActionClick("duplicate") }
        )

        HorizontalDivider(
            color = Color(0xFFE5E7EB),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        ActionItem(
            icon = Icons.Default.Delete,
            title = "Delete",
            subtitle = "Remove this item permanently",
            iconColor = Color(0xFFEF4444),
            onClick = { onActionClick("delete") }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(22.dp),
                tint = iconColor
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937)
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BottomSheetScreenPreview() {
    MyPracticeApplicationTheme {
        BottomSheetScreen()
    }
}
