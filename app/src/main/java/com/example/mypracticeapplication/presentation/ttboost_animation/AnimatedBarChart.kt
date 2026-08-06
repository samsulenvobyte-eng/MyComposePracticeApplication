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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import kotlin.math.sin

/**
 * Animated bar chart with entrance animation and ambient breathing effect.
 * 
 * @param barData List of relative bar heights (0.0 to 1.0)
 * @param entranceProgress Animation progress for bar entrance (0.0 to 1.0)
 * @param barWidth Width of each bar
 * @param barSpacing Spacing between bars
 * @param modifier Modifier for the canvas
 */
@Composable
fun AnimatedBarChart(
    barData: List<Float>,
    entranceProgress: () -> Float, // Optimized: Pass lambda to avoid parent recomposition
    barWidth: Dp,
    barSpacing: Dp,
    modifier: Modifier = Modifier
) {
    // Infinite transition for ambient breathing
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    // Optimized: Avoid property delegation 'by' to skip state read during composition
    val breathingPhase = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    // Optimized: Cache static geometry objects to avoid per-frame heap allocations
    val cornerRadius = remember { CornerRadius(35f, 35f) }

    Canvas(modifier = modifier.fillMaxSize()) {
        val barWidthPx = barWidth.toPx()
        val spacingPx = barSpacing.toPx()
        val currentEntranceProgress = entranceProgress()

        barData.forEachIndexed { index, targetRelativeHeight ->
            // Calculate ambient offset using sine wave based on phase and index
            // Read state .value directly here to defer invalidation to draw phase
            val ambientOffset = if (currentEntranceProgress > 0.95f) {
                sin(breathingPhase.value + index * 0.5f) * 0.03f
            } else {
                0f
            }

            // Combine heights: Target × Entrance + Ambient
            val finalRelativeHeight =
                (targetRelativeHeight * currentEntranceProgress + ambientOffset).coerceAtLeast(0.01f)

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
                cornerRadius = cornerRadius
            )
        }
    }
}


