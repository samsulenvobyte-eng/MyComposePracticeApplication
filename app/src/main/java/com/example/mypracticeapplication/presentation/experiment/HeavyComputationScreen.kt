package com.example.mypracticeapplication.presentation.experiment

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeavyComputationScreen(
    onNavigateBack: () -> Unit
) {
    // 1. Start with a Loading State
    var isLoaded by remember { mutableStateOf(false) }

    // 2. Defer Heavy Work
    // This effect runs naturally *after* the initial frame composition.
    // By simulating work here (or triggering a ViewModel load), we ensure the
    // transition animation (SlideIn) has already started or finished smoothly.
    LaunchedEffect(Unit) {
        // Simulate heavy calculation or fetching large data
        delay(800) 
        isLoaded = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smooth Heavy Transition") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // 3. Crossfade between Loading and Content
            Crossfade(
                targetState = isLoaded,
                animationSpec = tween(500),
                label = "ContentFade"
            ) { loaded ->
                if (loaded) {
                    HeavyContentList()
                } else {
                    LoadingPlaceholder()
                }
            }
        }
    }
}

@Composable
private fun LoadingPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Preparing Heavy Content...",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}

@Composable
private fun HeavyContentList() {
    // Simulate a complex list with many items
    val items = List(100) { "Complex Item #$it" }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "Calculation Complete!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                "Notice how the screen transition was smooth because we delayed this list rendering until after the navigation animation started.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
        
        items(items) { item ->
            HeavyListItem(item)
        }
    }
}

@Composable
private fun HeavyListItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}


