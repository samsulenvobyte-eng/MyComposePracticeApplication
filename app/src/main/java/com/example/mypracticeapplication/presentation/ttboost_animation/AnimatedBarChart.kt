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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
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
    entranceProgress: () -> Float, // Use lambda to defer state reading to draw phase
    barWidth: Dp,
    barSpacing: Dp,
    modifier: Modifier = Modifier
) {
    // Pre-allocate brush to avoid per-frame allocations during animation
    val barBrush = remember {
        Brush.verticalGradient(
            colors = TtBoostTheme.BarGradient,
            startY = 0f,
            endY = 1f
        )
    }

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
        val canvasHeight = size.height
        val currentProgress = entranceProgress()

        // Use indexed for-loop to avoid iterator allocation per frame
        for (index in barData.indices) {
            val targetRelativeHeight = barData[index]

            // Calculate ambient offset using sine wave based on phase and index
            val ambientOffset = if (currentProgress > 0.95f) {
                sin(breathingPhase + index * 0.5f) * 0.03f
            } else {
                0f
            }

            // Combine heights: Target × Entrance + Ambient
            val finalRelativeHeight =
                (targetRelativeHeight * currentProgress + ambientOffset).coerceAtLeast(0.01f)

            val barHeight = canvasHeight * finalRelativeHeight
            val xOffset = index * (barWidthPx + spacingPx)

            // Draw bar using withTransform to reuse the pre-allocated verticalGradient brush.
            // We translate to the bar's top-left and scale Y by the barHeight so the
            // 0f-1f brush perfectly covers the bar's vertical span.
            withTransform({
                translate(left = xOffset, top = canvasHeight - barHeight)
                scale(scaleX = 1f, scaleY = barHeight, pivot = Offset.Zero)
            }) {
                drawRoundRect(
                    brush = barBrush,
                    alpha = 0.3f,
                    size = Size(barWidthPx, 1f),
                    // Compensate for Y scale in corner radius to maintain visual consistency
                    cornerRadius = CornerRadius(35f, 35f / barHeight)
                )
            }
        }
    }
}


