package com.example.mypracticeapplication.presentation.ttboost_animation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Sealed class representing different overlay types for the chart.
 * Each overlay has a position (xIndex, yPercent) and a drawable resource.
 */
sealed class ChartOverlay(
    val xIndex: Float,
    val yPercent: Float,
    val res: Int
) {
    /**
     * Circular overlay with specified radius
     */
    class Circle(
        xIndex: Float,
        yPercent: Float,
        val radius: Dp,
        res: Int
    ) : ChartOverlay(xIndex, yPercent, res)

    /**
     * Pill-shaped overlay with specified dimensions
     */
    class Pill(
        xIndex: Float,
        yPercent: Float,
        val width: Dp,
        val height: Dp,
        res: Int
    ) : ChartOverlay(xIndex, yPercent, res)

    /**
     * Profile card overlay with rounded corners
     */
    class ProfileCard(
        xIndex: Float,
        yPercent: Float,
        val width: Dp,
        val height: Dp,
        res: Int
    ) : ChartOverlay(xIndex, yPercent, res)
}

/**
 * Renders an overlay based on its type.
 * Handles Circle, Pill, and ProfileCard variants with appropriate shapes.
 */
@Composable
fun OverlayRenderer(
    overlay: ChartOverlay,
    modifier: Modifier = Modifier
) {
    val painter = painterResource(id = overlay.res)

    when (overlay) {
        is ChartOverlay.Circle -> {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .offset(x = -overlay.radius, y = -overlay.radius)
                    .size(overlay.radius * 2)
                    .clip(CircleShape)
            )
        }

        is ChartOverlay.Pill -> {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .offset(x = -overlay.width / 2, y = -overlay.height / 2)
                    .size(overlay.width, overlay.height)
                    .clip(RoundedCornerShape(percent = 50))
            )
        }

        is ChartOverlay.ProfileCard -> {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .offset(x = -overlay.width / 2, y = -overlay.height / 2)
                    .size(overlay.width, overlay.height)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}


