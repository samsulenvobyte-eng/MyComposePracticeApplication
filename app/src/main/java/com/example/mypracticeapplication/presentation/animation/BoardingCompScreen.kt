package com.example.mypracticeapplication.presentation.animation

import android.graphics.BitmapFactory
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.R
import kotlinx.coroutines.isActive
import kotlin.math.min
import kotlin.random.Random

// Colors
private val PrimaryColor = Color(0xFFFF6B6B)
private val SecondaryColor = Color(0xFF4ECDC4)
private val DarkBackground = Color(0xFF0B0F19)
private val HeartColor = Color(0xFFFF2D55)

// Animation constants
private const val HEART_SPAWN_INTERVAL_MS = 500L
private const val HEART_LIFETIME_MS = 1200L
private const val HEART_TRAVEL_DISTANCE = 300f
private const val MAX_HEARTS = 8

/**
 * Optimized heart data - uses primitive types and pre-calculated values
 */
class Heart(
    val spawnTime: Long,
    val offsetX: Float = Random.nextFloat() * 50f - 25f,
    val rotation: Float = Random.nextFloat() * 30f - 15f
) {
    // Calculate progress (0 to 1) based on elapsed time
    fun getProgress(currentTime: Long): Float {
        val elapsed = currentTime - spawnTime
        return (elapsed.toFloat() / HEART_LIFETIME_MS).coerceIn(0f, 1f)
    }
    
    fun isExpired(currentTime: Long): Boolean = 
        currentTime - spawnTime > HEART_LIFETIME_MS
}

/**
 * Pill overlay configuration - immutable data class
 */
data class PillConfig(
    val xIndex: Float = 3.5f,
    val yPercent: Float = 0.4f,
    val width: Dp = 100.dp,
    val height: Dp = 200.dp,
    val res: Int = R.drawable.img_people_landscape
)

@Composable
fun BoardingCompScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        BoardingCompHeader(onNavigateBack = onNavigateBack)
        
        OptimizedPillWithHearts(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Composable
private fun OptimizedPillWithHearts(
    modifier: Modifier = Modifier
) {
    val pillConfig = remember { PillConfig() }
    
    // Load image once
    val context = LocalContext.current
    val image = remember(pillConfig.res) {
        BitmapFactory.decodeResource(context.resources, pillConfig.res)?.asImageBitmap()
    }
    
    // Heart state - use ArrayList for in-place updates to avoid list allocations
    val hearts = remember { ArrayList<Heart>(MAX_HEARTS) }
    var lastSpawnTime by remember { mutableLongStateOf(0L) }
    var frameTick by remember { mutableLongStateOf(0L) }
    
    // Single animation loop for all hearts
    LaunchedEffect(Unit) {
        while (isActive) {
            val currentTime = withFrameMillis { it }
            frameTick = currentTime
            
            // Spawn new heart if enough time has passed
            if (currentTime - lastSpawnTime > HEART_SPAWN_INTERVAL_MS) {
                lastSpawnTime = currentTime
                hearts.add(Heart(spawnTime = currentTime))
            }
            
            // Remove expired hearts
            if (hearts.isNotEmpty()) {
                var i = 0
                while (i < hearts.size) {
                    if (hearts[i].isExpired(currentTime)) {
                        hearts.removeAt(i)
                    } else {
                        i++
                    }
                }

                // Limit to MAX_HEARTS
                while (hearts.size > MAX_HEARTS) {
                    hearts.removeAt(0)
                }
            }
        }
    }
    
    val density = LocalDensity.current
    val pillWidthPx = with(density) { pillConfig.width.toPx() }
    val pillHeightPx = with(density) { pillConfig.height.toPx() }
    val heartSizePx = with(density) { 36.dp.toPx() }
    
    // Pre-allocate normalized heart path (0..1) to avoid per-frame allocations
    val heartPath = remember {
        Path().apply {
            moveTo(0.5f, 0.25f)
            cubicTo(0.15f, 0.1f, 0f, 0.35f, 0f, 0.5f)
            cubicTo(0f, 0.7f, 0.25f, 0.85f, 0.5f, 1f)
            cubicTo(0.75f, 0.85f, 1f, 0.7f, 1f, 0.5f)
            cubicTo(1f, 0.35f, 0.85f, 0.1f, 0.5f, 0.25f)
            close()
        }
    }

    Spacer(
        modifier = modifier.drawWithCache {
            val imageBitmap = image ?: return@drawWithCache onDrawBehind { }

            val barCount = 5
            val spacing = size.width * 0.05f
            val barWidth = (size.width - (spacing * (barCount - 1))) / barCount

            val pillCenterX = (pillConfig.xIndex * (barWidth + spacing)) + (barWidth / 2)
            val pillCenterY = size.height * 0.5f - (size.height * 0.5f * (pillConfig.yPercent - 0.5f))

            val topLeft = Offset(pillCenterX - pillWidthPx / 2, pillCenterY - pillHeightPx / 2)
            val pillBottomY = pillCenterY + pillHeightPx / 2

            val pillPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = Rect(topLeft, Size(pillWidthPx, pillHeightPx)),
                        cornerRadius = CornerRadius(pillWidthPx / 2, pillWidthPx / 2)
                    )
                )
            }

            val imgWidth = imageBitmap.width.toFloat()
            val imgHeight = imageBitmap.height.toFloat()
            val scaleFactor = maxOf(pillWidthPx / imgWidth, pillHeightPx / imgHeight)
            val scaledWidth = (imgWidth * scaleFactor).toInt()
            val scaledHeight = (imgHeight * scaleFactor).toInt()
            val drawX = (topLeft.x + (pillWidthPx - scaledWidth) / 2).toInt()
            val drawY = (topLeft.y + (pillHeightPx - scaledHeight) / 2).toInt()

            onDrawBehind {
                // Trigger redraw on frameTick change
                frameTick

                // Draw pill with clipped image
                clipPath(pillPath) {
                    drawImage(
                        image = imageBitmap,
                        dstOffset = IntOffset(drawX, drawY),
                        dstSize = IntSize(scaledWidth, scaledHeight)
                    )
                }

                // Draw all hearts
                val currentTime = frameTick
                for (i in 0 until hearts.size) {
                    val heart = hearts[i]
                    val progress = heart.getProgress(currentTime)
                    val alpha = calculateHeartAlpha(progress)

                    if (alpha > 0f) {
                        val scale = calculateHeartScale(progress)
                        val translateY = -HEART_TRAVEL_DISTANCE * progress

                        val x = pillCenterX + heart.offsetX - heartSizePx / 2
                        val y = pillBottomY + translateY - heartSizePx / 2

                        translate(left = x + heartSizePx / 2, top = y + heartSizePx / 2) {
                            rotate(degrees = heart.rotation) {
                                scale(scale = scale) {
                                    translate(left = -heartSizePx / 2, top = -heartSizePx / 2) {
                                        // Reuse heartPath with scaling
                                        scale(heartSizePx) {
                                            drawPath(
                                                path = heartPath,
                                                color = HeartColor,
                                                alpha = alpha
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

/**
 * Calculate scale with spring-like bounce effect
 */
private fun calculateHeartScale(progress: Float): Float {
    return when {
        progress < 0.15f -> {
            // Pop in: 0 -> 1.3
            val p = progress / 0.15f
            1.3f * easeOutBack(p)
        }
        progress < 0.3f -> {
            // Settle: 1.3 -> 1.0
            val p = (progress - 0.15f) / 0.15f
            1.3f - 0.3f * p
        }
        else -> 1f
    }
}

/**
 * Calculate alpha with fade out
 */
private fun calculateHeartAlpha(progress: Float): Float {
    return when {
        progress < 0.5f -> 1f
        else -> 1f - ((progress - 0.5f) / 0.5f)
    }
}

/**
 * Easing function for bounce effect
 */
private fun easeOutBack(x: Float): Float {
    val c1 = 1.70158f
    val c3 = c1 + 1f
    val t = x - 1f
    return 1f + c3 * t * t * t + c1 * t * t
}

@Composable
private fun BoardingCompHeader(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    val headerBrush = remember {
        Brush.horizontalGradient(
            colors = listOf(PrimaryColor, SecondaryColor)
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(headerBrush)
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Column {
            Text(
                text = "🎯 Boarding Comp",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            val subtitleColor = remember { Color.White.copy(alpha = 0.8f) }
            Text(
                text = "Onboarding Component Animations",
                style = MaterialTheme.typography.bodySmall,
                color = subtitleColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BoardingCompScreenPreview() {
    BoardingCompScreen()
}


