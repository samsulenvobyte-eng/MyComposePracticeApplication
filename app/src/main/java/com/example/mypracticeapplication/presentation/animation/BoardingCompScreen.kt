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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    onNavigateBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
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
    
    // Heart state - use mutableStateOf with a list that we manage manually
    var hearts by remember { mutableStateOf(listOf<Heart>()) }
    var lastSpawnTime by remember { mutableStateOf(0L) }
    var currentTime by remember { mutableStateOf(0L) }
    
    // Single animation loop for all hearts
    LaunchedEffect(Unit) {
        val startTime = withFrameMillis { it }
        
        while (isActive) {
            currentTime = withFrameMillis { it }
            
            // Spawn new heart if enough time has passed
            if (currentTime - lastSpawnTime > HEART_SPAWN_INTERVAL_MS) {
                lastSpawnTime = currentTime
                hearts = (hearts + Heart(spawnTime = currentTime))
                    .filter { !it.isExpired(currentTime) }
                    .takeLast(MAX_HEARTS)
            }
            
            // Remove expired hearts periodically
            hearts = hearts.filter { !it.isExpired(currentTime) }
        }
    }
    
    val density = LocalDensity.current
    val pillWidthPx = with(density) { pillConfig.width.toPx() }
    val pillHeightPx = with(density) { pillConfig.height.toPx() }

    // PERFORMANCE: Cache Path objects to avoid allocations in the draw loop
    val heartPath = remember {
        Path().apply {
            // Normalized heart shape (0.0 to 1.0)
            moveTo(0.5f, 0.25f)
            cubicTo(0.15f, 0.1f, 0f, 0.35f, 0f, 0.5f)
            cubicTo(0f, 0.7f, 0.25f, 0.85f, 0.5f, 1f)
            cubicTo(0.75f, 0.85f, 1f, 0.7f, 1f, 0.5f)
            cubicTo(1f, 0.35f, 0.85f, 0.1f, 0.5f, 0.25f)
            close()
        }
    }

    val pillPath = remember(pillWidthPx, pillHeightPx) {
        Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(Offset.Zero, Size(pillWidthPx, pillHeightPx)),
                    cornerRadius = CornerRadius(pillWidthPx / 2, pillWidthPx / 2)
                )
            )
        }
    }
    
    // Single Canvas for everything - most performant approach
    Canvas(modifier = modifier) {
        if (image == null) return@Canvas
        
        // Pre-calculate pill position (only depends on canvas size)
        val barCount = 5
        val spacing = size.width * 0.05f
        val barWidth = (size.width - (spacing * (barCount - 1))) / barCount
        
        val pillCenterX = (pillConfig.xIndex * (barWidth + spacing)) + (barWidth / 2)
        val pillCenterY = size.height * 0.5f - (size.height * 0.5f * (pillConfig.yPercent - 0.5f))
        
        val topLeft = Offset(pillCenterX - pillWidthPx / 2, pillCenterY - pillHeightPx / 2)
        val pillBottomY = pillCenterY + pillHeightPx / 2
        
        // Draw pill with clipped image
        drawPill(
            image = image,
            topLeft = topLeft,
            width = pillWidthPx,
            height = pillHeightPx,
            pillPath = pillPath
        )
        
        // Draw all hearts in single draw call batch
        hearts.forEach { heart ->
            drawHeart(
                heart = heart,
                currentTime = currentTime,
                centerX = pillCenterX,
                bottomY = pillBottomY,
                heartPath = heartPath
            )
        }
    }
}

/**
 * Draw pill shape with clipped image - extracted for clarity
 */
private fun DrawScope.drawPill(
    image: ImageBitmap,
    topLeft: Offset,
    width: Float,
    height: Float,
    pillPath: Path
) {
    translate(left = topLeft.x, top = topLeft.y) {
        clipPath(pillPath) {
            val imgWidth = image.width.toFloat()
            val imgHeight = image.height.toFloat()

            val scaleFactor = maxOf(width / imgWidth, height / imgHeight)
            val scaledWidth = imgWidth * scaleFactor
            val scaledHeight = imgHeight * scaleFactor

            // Draw relative to the translated origin (0,0)
            val drawX = (width - scaledWidth) / 2
            val drawY = (height - scaledHeight) / 2

            drawImage(
                image = image,
                dstOffset = IntOffset(drawX.toInt(), drawY.toInt()),
                dstSize = IntSize(scaledWidth.toInt(), scaledHeight.toInt())
            )
        }
    }
}

/**
 * Draw a single heart with animation - uses DrawScope transforms for efficiency
 */
private fun DrawScope.drawHeart(
    heart: Heart,
    currentTime: Long,
    centerX: Float,
    bottomY: Float,
    heartPath: Path
) {
    val progress = heart.getProgress(currentTime)
    
    // Calculate animation values
    val scale = calculateHeartScale(progress)
    val alpha = calculateHeartAlpha(progress)
    val translateY = -HEART_TRAVEL_DISTANCE * progress
    
    if (alpha <= 0f) return
    
    val heartSize = 36.dp.toPx()
    val x = centerX + heart.offsetX - heartSize / 2
    val y = bottomY + translateY - heartSize / 2
    
    translate(left = x + heartSize / 2, top = y + heartSize / 2) {
        rotate(degrees = heart.rotation) {
            scale(scale = scale) {
                translate(left = -heartSize / 2, top = -heartSize / 2) {
                    drawHeartShape(
                        size = heartSize,
                        color = HeartColor.copy(alpha = alpha),
                        path = heartPath
                    )
                }
            }
        }
    }
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

/**
 * Draw heart shape using Canvas paths - more efficient than Icon composable.
 * PERFORMANCE: Reuses a cached Path object to avoid per-frame allocations.
 */
private fun DrawScope.drawHeartShape(
    size: Float,
    color: Color,
    path: Path
) {
    // scale() by default scales from the center of the current canvas.
    // Since we've already translated to the heart's position, we must scale
    // from (0,0) to keep the heart in place.
    scale(scale = size, pivot = Offset.Zero) {
        drawPath(path = path, color = color)
    }
}

@Composable
private fun BoardingCompHeader(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(PrimaryColor, SecondaryColor)
                )
            )
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
            Text(
                text = "Onboarding Component Animations",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BoardingCompScreenPreview() {
    BoardingCompScreen()
}


