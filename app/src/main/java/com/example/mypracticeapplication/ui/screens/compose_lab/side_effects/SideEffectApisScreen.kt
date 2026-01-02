package com.example.mypracticeapplication.ui.screens.compose_lab.side_effects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * SideEffectApisScreen demonstrates various side effect APIs in Jetpack Compose:
 * - LaunchedEffect
 * - DisposableEffect
 * - SideEffect
 * - rememberCoroutineScope
 * - rememberUpdatedState
 * - produceState
 * - derivedStateOf
 * - snapshotFlow
 */
@Composable
fun SideEffectApisScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToLaunchedEffect: () -> Unit = {},
    onNavigateToDisposableEffect: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Text(
            text = "Side Effect APIs",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Explore the various side effect APIs in Jetpack Compose",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Navigation buttons for side effect examples
        SideEffectNavButton(
            text = "🚀 LaunchedEffect",
            description = "Run suspend functions safely",
            containerColor = Color(0xFF6200EE),
            onClick = onNavigateToLaunchedEffect
        )

        SideEffectNavButton(
            text = "🧹 DisposableEffect",
            description = "Setup + cleanup resources",
            containerColor = Color(0xFF2E7D32),
            onClick = onNavigateToDisposableEffect
        )
    }
}

@Composable
private fun SideEffectNavButton(
    text: String,
    description: String,
    containerColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SideEffectApisScreenPreview() {
    SideEffectApisScreen()
}
