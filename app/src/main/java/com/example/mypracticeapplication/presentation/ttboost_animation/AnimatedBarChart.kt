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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import kotlin.math.sin

/**
 * Immutable wrapper for bar data to ensure stability in Compose.
 */
@Immutable
data class BarDataV2(val items: List<Float>)

/**
 * Animated bar chart with entrance animation and ambient breathing effect.
 * 
 * @param barData Data for the bars
 * @param entranceProgressProvider Lambda providing animation progress for bar entrance (0.0 to 1.0)
 * @param barWidth Width of each bar
 * @param barSpacing Spacing between bars
 * @param modifier Modifier for the canvas
 */
@Composable
fun AnimatedBarChart(
    barData: BarDataV2,
    entranceProgressProvider: () -> Float,
    barWidth: Dp,
    barSpacing: Dp,
    modifier: Modifier = Modifier
) {
    // Infinite transition for ambient breathing
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathingPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val barWidthPx = barWidth.toPx()
        val spacingPx = barSpacing.toPx()
        val entranceProgress = entranceProgressProvider()
        val items = barData.items

        for (index in items.indices) {
            val targetRelativeHeight = items[index]
            // Calculate ambient offset using sine wave based on phase and index
            val ambientOffset = if (entranceProgress > 0.95f) {
                sin(breathingPhase + index * 0.5f) * 0.03f
            } else {
                0f
            }

            // Combine heights: Target × Entrance + Ambient
            val finalRelativeHeight =
                (targetRelativeHeight * entranceProgress + ambientOffset).coerceAtLeast(0.01f)

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
                cornerRadius = CornerRadius(35f, 35f)
            )
        }
    }
}


