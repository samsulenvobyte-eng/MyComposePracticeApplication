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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import kotlin.math.sin

/**
 * Immutable wrapper for bar chart data to prevent unnecessary recompositions.
 */
@Immutable
data class BarData(val values: List<Float>)

/**
 * Animated bar chart with entrance animation and ambient breathing effect.
 * 
 * @param barData Immutable list of relative bar heights (0.0 to 1.0)
 * @param modifier Modifier for the canvas
 * @param entranceProgress Animation progress provider for bar entrance (0.0 to 1.0)
 * @param barWidth Width of each bar
 * @param barSpacing Spacing between bars
 */
@Composable
fun AnimatedBarChart(
    barData: BarData,
    entranceProgress: () -> Float,
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

    // Cache the gradient to avoid re-allocation if possible, though start/end are dynamic
    // We use a vertical gradient that will be applied to each bar
    val gradientColors = TtBoostTheme.BarGradient

    Canvas(modifier = modifier.fillMaxSize()) {
        val progress = entranceProgress()
        val phase = breathingPhase
        val barWidthPx = barWidth.toPx()
        val spacingPx = barSpacing.toPx()
        val canvasHeight = size.height
        val barValues = barData.values

        // Use a manual indexed loop to avoid iterator allocation on every frame
        for (index in barValues.indices) {
            val targetRelativeHeight = barValues[index]

            // Calculate ambient offset using sine wave based on phase and index
            val ambientOffset = if (progress > 0.95f) {
                sin(phase + index * 0.5f) * 0.03f
            } else {
                0f
            }

            // Combine heights: Target × Entrance + Ambient
            val finalRelativeHeight =
                (targetRelativeHeight * progress + ambientOffset).coerceAtLeast(0.01f)

            val barHeight = canvasHeight * finalRelativeHeight
            val xOffset = index * (barWidthPx + spacingPx)
            val yOffset = canvasHeight - barHeight // Draw from bottom up

            // Draw bar with gradient
            // Optimization: Brush.verticalGradient creates a new Brush object.
            // For extreme performance, we could use a single Brush and scale it,
            // but since startY/endY are changing, this is the clearest way.
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = gradientColors,
                    startY = yOffset,
                    endY = canvasHeight
                ),
                alpha = 0.3f,
                topLeft = Offset(xOffset, yOffset),
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(35f, 35f)
            )
        }
    }
}


