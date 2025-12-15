package com.example.mypracticeapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperimentScreen(
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Experiment",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1a1a2e)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1a1a2e)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8E8F8)) // Light pink background
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Main container taking 50% of screen height
            AdaptiveImageLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun AdaptiveImageLayout(
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        // Calculate adaptive sizes based on container dimensions
        val containerWidth = maxWidth
        val containerHeight = maxHeight

        // Aspect ratio 50:63 (width:height)
        val aspectRatio = 50f / 63f

        // Primary image takes ~70% of container width, height calculated from aspect ratio
        val primaryImageWidth = containerWidth * 0.70f
        val primaryImageHeight = primaryImageWidth / aspectRatio

        // Secondary image takes ~55% of container width, height calculated from aspect ratio
        val secondaryImageWidth = containerWidth * 0.55f
        val secondaryImageHeight = secondaryImageWidth / aspectRatio

        // Corner radius scales with container size
        val primaryCornerRadius = (containerWidth.value * 0.05f).dp
        val secondaryCornerRadius = (containerWidth.value * 0.05f).dp

        // Border width scales with container
        val borderWidth = (containerWidth.value * 0.01f).dp.coerceIn(2.dp, 4.dp)

        // Primary (larger) image - positioned at top-left





        Box(
            modifier = Modifier
                .size(width = primaryImageWidth, height = primaryImageHeight)
                .align(Alignment.TopStart)
                .zIndex(1f)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(primaryCornerRadius),
                    clip = false
                )
                .clip(RoundedCornerShape(primaryCornerRadius))
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder icon for image
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Primary Image Placeholder",
                modifier = Modifier.size(primaryImageWidth * 0.3f),
                tint = Color(0xFFBDBDBD)
            )
        }

        // Secondary (smaller) image - positioned at bottom-right with gradient border
        Box(
            modifier = Modifier
                .size(width = secondaryImageWidth, height = secondaryImageHeight)
                .align(Alignment.BottomEnd)
                .offset(x = (-8).dp, y = (-8).dp)
                .zIndex(2f)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(secondaryCornerRadius),
                    clip = false
                )
                .border(
                    width = borderWidth,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFE040FB),
                            Color(0xFF7C4DFF),
                            Color(0xFF536DFE)
                        )
                    ),
                    shape = RoundedCornerShape(secondaryCornerRadius)
                )
                .clip(RoundedCornerShape(secondaryCornerRadius))
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder icon for image
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Secondary Image Placeholder",
                modifier = Modifier.size(secondaryImageWidth * 0.3f),
                tint = Color(0xFFBDBDBD)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ExperimentScreenPreview() {
    ExperimentScreen()
}