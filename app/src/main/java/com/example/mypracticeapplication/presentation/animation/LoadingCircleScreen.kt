package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LoadingCircleScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Loading Circle Animation",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                LoadingCircleAnimation(modifier = Modifier.size(200.dp))
            }
        }
    }
}

/**
 * Optimized loading circle animation.
 *
 * Performance improvements:
 * 1. Caches [Stroke] and [Color] objects using [remember] to avoid per-frame allocations.
 * 2. Utilizes [graphicsLayer] for rotation to offload it to the GPU, bypassing the Draw phase on every frame.
 * 3. Uses [drawWithCache] to pre-calculate geometry (radius, sizes, offsets) only when layout size changes.
 * 4. Separates static track and animated arcs into different layers to avoid redundant drawing of the track.
 */
@Composable
fun LoadingCircleAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LoadingCircleRotation")
    
    // Rotate 360 degrees over 1000ms
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    // Cache style and colors to avoid allocations in draw block
    val trackColor = remember { Color(0xFFECEBEC) }
    val arcColor = remember { Color(0xFF238C2A) }
    val strokeWidth = 30f
    val stroke = remember { Stroke(width = strokeWidth, cap = StrokeCap.Round) }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Static Track Layer
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    // Pre-calculate diameter and center to ensure the stroke stays within bounds
                    val diameter = size.minDimension - strokeWidth
                    onDrawBehind {
                        drawCircle(
                            color = trackColor,
                            radius = diameter / 2,
                            center = center,
                            style = stroke
                        )
                    }
                }
        )
        
        // Rotating Arcs Layer
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                // Offload rotation to GPU
                .graphicsLayer { rotationZ = rotation }
                .drawWithCache {
                    val diameter = size.minDimension - strokeWidth
                    val arcSize = Size(diameter, diameter)
                    val topLeft = Offset(
                        (size.width - diameter) / 2,
                        (size.height - diameter) / 2
                    )
                    onDrawBehind {
                        // Arc 1
                        drawArc(
                            color = arcColor,
                            startAngle = 0f,
                            sweepAngle = 45f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = stroke
                        )

                        // Arc 2 (Offset by 120 degrees)
                        drawArc(
                            color = arcColor,
                            startAngle = 120f,
                            sweepAngle = 45f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = stroke
                        )

                        // Arc 3 (Offset by 240 degrees)
                        drawArc(
                            color = arcColor,
                            startAngle = 240f,
                            sweepAngle = 45f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = stroke
                        )
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingCirclePreview() {
    MaterialTheme {
        LoadingCircleScreen(navController = rememberNavController())
    }
}
