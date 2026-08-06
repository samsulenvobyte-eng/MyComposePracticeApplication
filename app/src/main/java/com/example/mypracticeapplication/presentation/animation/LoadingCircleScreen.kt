package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LoadingCircleScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // Optional: Add a top bar if desired, or keep it simple as per request
        }
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

@Composable
fun LoadingCircleAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LoadingCircleRotation")
    
    // Rotate 360 degrees over 1000ms (1 second), matching the Lottie file
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    // Performance Optimization: Cache drawing constants and objects to avoid per-frame allocations
    val strokeWidth = 30f
    val trackColor = remember { Color(0xFFECEBEC) }
    val arcColor = remember { Color(0xFF238C2A) }
    val trackStroke = remember(strokeWidth) { Stroke(width = strokeWidth) }
    val arcStroke = remember(strokeWidth) { Stroke(width = strokeWidth, cap = StrokeCap.Round) }

    Box(modifier = modifier) {
        // Performance Optimization: Separate static and dynamic elements into different layers.
        // The static track is drawn once and cached.
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val canvasSize = size.minDimension - strokeWidth
                    drawCircle(
                        color = trackColor,
                        radius = canvasSize / 2,
                        center = center,
                        style = trackStroke
                    )
                }
        )

        // Performance Optimization: Use graphicsLayer to offload rotation to the GPU.
        // By deferring the rotation to the graphics layer, we skip the Draw phase
        // during animation frames, as the arcs are cached as a layer.
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = rotation
                }
                .drawBehind {
                    val padding = strokeWidth / 2
                    val canvasSize = size.minDimension - strokeWidth
                    val arcSize = Size(canvasSize, canvasSize)
                    val topLeft = Offset(padding, padding)

                    // Arc 1
                    drawArc(
                        color = arcColor,
                        startAngle = 0f,
                        sweepAngle = 45f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = arcStroke
                    )

                    // Arc 2 (Offset by 120 degrees)
                    drawArc(
                        color = arcColor,
                        startAngle = 120f,
                        sweepAngle = 45f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = arcStroke
                    )

                    // Arc 3 (Offset by 240 degrees)
                    drawArc(
                        color = arcColor,
                        startAngle = 240f,
                        sweepAngle = 45f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = arcStroke
                    )
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


