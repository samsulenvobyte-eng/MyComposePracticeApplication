package com.example.mypracticeapplication.presentation.ttboost_animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Creates the bubble shape with a pointer at the bottom.
 */
private fun createBubbleShape(): GenericShape {
    return GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val cornerRadius = 40f
        val pointerHeight = 50f
        val pointerWidth = 50f
        val rectHeight = height - pointerHeight

        addRoundRect(
            RoundRect(
                rect = Rect(offset = Offset.Zero, size = Size(width, rectHeight)),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        )

        moveTo((width / 2) - (pointerWidth / 2), rectHeight)
        lineTo(width / 2, height)
        lineTo((width / 2) + (pointerWidth / 2), rectHeight)
        close()
    }
}

/**
 * A 3D rotating stat bubble with an icon and animated counter.
 * 
 * @param icon The icon to display
 * @param count Lambda providing the current count value (defers state reading)
 * @param color The bubble background color
 * @param shadowColor The shadow/depth color (currently unused but kept for API compatibility)
 * @param modifier Modifier for positioning and styling
 * @param onClick Optional callback when bubble is clicked
 */
@Composable
fun StatBubble(
    icon: ImageVector,
    count: () -> Int,
    color: Color,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // 3D Rotation Animation
    val infiniteTransition = rememberInfiniteTransition(label = "3d_float")

    val rotationY = infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotationY"
    )

    val rotationX = infiniteTransition.animateFloat(
        initialValue = 5f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotationX"
    )

    val bubbleShape = remember { createBubbleShape() }

    Box(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .graphicsLayer {
                // Defer state reading to draw phase to avoid recomposing the whole bubble
                this.rotationY = rotationY.value
                this.rotationX = rotationX.value
                cameraDistance = 12f * density
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            }
    ) {
        // Main bubble surface
        Box(
            modifier = Modifier.background(color, bubbleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.padding(bottom = 20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    AnimatedContent(
                        targetState = count(),
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInVertically { height -> height } + fadeIn()).togetherWith(
                                    slideOutVertically { height -> -height } + fadeOut()
                                )
                            } else {
                                (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                                    slideOutVertically { height -> height } + fadeOut()
                                )
                            }.using(SizeTransform(clip = false))
                        },
                        label = "counter"
                    ) { targetCount ->
                        Text(
                            text = "$targetCount",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }
            }
        }
    }
}


