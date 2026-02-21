package com.example.mypracticeapplication.presentation.experiment

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplexUiScreen(
    onNavigateBack: () -> Unit
) {
    // 1. Deferred Composition State
    var showContent by remember { mutableStateOf(false) }

    // 2. Trigger content loading AFTER the transition starts
    // A small delay allows the navigation frame to render the Scaffold first,
    // ensuring the slide animation begins smoothly before the main thread gets hit.
    LaunchedEffect(Unit) {
        delay(200) // Small delay to let the transition "catch"
        showContent = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complex UI Stress Test") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (!showContent) {
                // Lightweight Loading State
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                ) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Preparing 2,000 Nodes...", color = Color.Gray)
                }
            } else {
                // Heavy Content
                // Using AnimatedVisibility or simple if-check implies a composition cost.
                // Since we are already inside the "showContent = true" block, 
                // this composition happens now.
                androidx.compose.animation.AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(500))
                ) {
                    HeavyGrid()
                }
            }
        }
    }
}

@Composable
private fun HeavyGrid() {
    // A Scrollable Column with a FlowRow to create massive node count
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Rendering 2,000 colored boxes...",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.labelLarge
        )
        
        // This creates a LOT of layout nodes (FlowRow + 2000 Boxes)
        // If this were composed immediately on navigation, the animation would jitter.
        FlowRow(
            modifier = Modifier.padding(4.dp),
            maxItemsInEachRow = 10
        ) {
            repeat(2000) { index ->
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(30.dp)
                        .background(
                            color = Color(
                                red = (index * 13) % 255,
                                green = (index * 77) % 255,
                                blue = (index * 39) % 255
                            )
                        )
                )
            }
        }
    }
}


