package com.example.mypracticeapplication.ui.screens.practice.ui_architecture

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class NotificationType {
    EMAIL, SMS, PUSH, NONE
}

enum class Theme {
    LIGHT, DARK, SYSTEM
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Level2Screen() {
    // Local UI state - Move these to ViewModel/UiState for practice
    var userName by remember { mutableStateOf("") }
    var selectedNotification by remember { mutableStateOf(NotificationType.EMAIL) }
    var selectedTheme by remember { mutableStateOf(Theme.SYSTEM) }
    var soundEnabled by remember { mutableStateOf(true) }
    var volume by remember { mutableFloatStateOf(0.7f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Header(onBackClick = { /* TODO */ })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 1️⃣ User Name Input
            UserNameSection(
                userName = userName,
                onUserNameChange = { userName = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2️⃣ Notification Type Selector
            NotificationSection(
                selectedType = selectedNotification,
                onTypeSelected = { selectedNotification = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3️⃣ Theme Selector
            ThemeSection(
                selectedTheme = selectedTheme,
                onThemeSelected = { selectedTheme = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4️⃣ Sound Toggle
            SoundToggle(
                soundEnabled = soundEnabled,
                onSoundToggle = { soundEnabled = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5️⃣ Volume Slider
            VolumeSection(
                volume = volume,
                onVolumeChange = { volume = it },
                enabled = soundEnabled
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 6️⃣ Action Buttons
            ActionButtons(
                onSaveClick = { /* TODO */ },
                onResetClick = { /* TODO */ }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Header(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun UserNameSection(
    userName: String,
    onUserNameChange: (String) -> Unit
) {
    Column {
        Text(
            text = "User Name",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = onUserNameChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your name") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationSection(
    selectedType: NotificationType,
    onTypeSelected: (NotificationType) -> Unit
) {
    Column {
        Text(
            text = "Notification Preference",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NotificationType.entries.forEach { type ->
                val chipColor = when (type) {
                    NotificationType.EMAIL -> Color(0xFF1976D2)
                    NotificationType.SMS -> Color(0xFF388E3C)
                    NotificationType.PUSH -> Color(0xFFF57C00)
                    NotificationType.NONE -> Color(0xFF757575)
                }
                FilterChip(
                    selected = selectedType == type,
                    onClick = { onTypeSelected(type) },
                    label = {
                        Text(
                            text = type.name,
                            fontWeight = if (selectedType == type) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = chipColor,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
private fun ThemeSection(
    selectedTheme: Theme,
    onThemeSelected: (Theme) -> Unit
) {
    Column {
        Text(
            text = "Theme",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Theme.entries.forEach { theme ->
                val (bgColor, textColor) = when (theme) {
                    Theme.LIGHT -> Color(0xFFFFF9C4) to Color.Black
                    Theme.DARK -> Color(0xFF424242) to Color.White
                    Theme.SYSTEM -> Color(0xFF90CAF9) to Color.Black
                }
                val isSelected = selectedTheme == theme

                Button(
                    onClick = { onThemeSelected(theme) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) bgColor else Color.Transparent,
                        contentColor = if (isSelected) textColor else MaterialTheme.colorScheme.onSurface
                    ),
                    border = if (!isSelected) BorderStroke(1.dp, bgColor) else null
                ) {
                    Text(
                        text = theme.name,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SoundToggle(
    soundEnabled: Boolean,
    onSoundToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (soundEnabled)
                Color(0xFF4CAF50).copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Enable Sound",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Switch(
                checked = soundEnabled,
                onCheckedChange = onSoundToggle
            )
        }
    }
}

@Composable
private fun VolumeSection(
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    enabled: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Volume: ${(volume * 100).toInt()}%",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (enabled) MaterialTheme.colorScheme.onBackground 
                   else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = volume,
            onValueChange = onVolumeChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ActionButtons(
    onSaveClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Save Settings",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = onResetClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF757575))
        ) {
            Text(
                text = "Reset to Defaults",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF757575)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Level2ScreenPreview() {
    Level2Screen()
}
