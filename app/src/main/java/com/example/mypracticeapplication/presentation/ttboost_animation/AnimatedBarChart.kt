package com.example.mypracticeapplication.presentation.ttboost_animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import kotlin.math.sin

import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Density

/**
 * Animated bar chart with entrance animation and ambient breathing effect.
 * 
 * @param barData List of relative bar heights (0.0 to 1.0)
 * @param progress Animation progress for bar entrance (0.0 to 1.0)
 * @param barWidth Width of each bar
 * @param barSpacing Spacing between bars
 * @param modifier Modifier for the canvas
 */
@Composable
fun AnimatedBarChart(
    barData: List<Float>,
    progress: () -> Float,
    barWidth: Dp,
    barSpacing: Dp,
    modifier: Modifier = Modifier
) {
    // Infinite transition for ambient breathing
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathingPhase = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    // Optimization: Cache CornerRadius and calculations to avoid per-frame allocations
    val barCornerRadius = remember { CornerRadius(35f, 35f) }

    Canvas(modifier = modifier.fillMaxSize()) {
        val barWidthPx = barWidth.toPx()
        val spacingPx = barSpacing.toPx()
        val currentProgress = progress()
        val phase = breathingPhase.value

        // Optimization: Use standard for-loop to avoid iterator allocation in hot path
        for (index in barData.indices) {
            val targetRelativeHeight = barData[index]

            // Calculate ambient offset using sine wave based on phase and index
            val ambientOffset = if (currentProgress > 0.95f) {
                sin(phase + index * 0.5f) * 0.03f
            } else {
                0f
            }

            // Combine heights: Target × Entrance + Ambient
            val finalRelativeHeight =
                (targetRelativeHeight * currentProgress + ambientOffset).coerceAtLeast(0.01f)

            val barHeight = size.height * finalRelativeHeight
            val xOffset = index * (barWidthPx + spacingPx)
            val yOffset = size.height - barHeight // Draw from bottom up

            // Draw bar with gradient
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = TtBoostTheme.BarGradient,
                    startY = yOffset,
                    endY = size.height
                ),
                alpha = 0.3f,
                topLeft = Offset(xOffset, yOffset),
                size = Size(barWidthPx, barHeight),
                cornerRadius = barCornerRadius
            )
        }
    }
}


