package com.example.mypracticeapplication.ui.screens.animation

import android.graphics.BitmapFactory
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.UUID
import kotlin.math.sin
import kotlin.random.Random

val DarkBackground = Color(0xFF0D1117)

val LovePalette = listOf(
    Color(0xFFFF4D6D), // Rose
    Color(0xFFFF758F), // Pink
    Color(0xFFFF8FA3), // Light Pink
    Color(0xFFC9184A), // Crimson
    Color(0xFFFF006E)  // Magenta
)

val GreenMoneyPalette = listOf(
    Color(0xFF2E7D32), // Dark Green (Dollar Bill)
    Color(0xFF00C853), // Accent Green (Success/Profit)
    Color(0xFF4CAF50), // Standard Green (Mid-tone)
    Color(0xFF81C784), // Light Green (Highlight)
    Color(0xFF1B5E20)  // Deep Forest (Shadow)
)

val GoldenMoneyPalette = listOf(
    Color(0xFFFFD700), // Gold (Classic)
    Color(0xFFFFC300), // Vivid Yellow (Coin)
    Color(0xFFDAA520), // Goldenrod (Deep Gold)
    Color(0xFFF4D35E), // Muted Gold (Champagne)
    Color(0xFFB8860B)  // Dark Goldenrod (Bronze/Shadow)
)

enum class PillCircleType {
    COIN, LIKE, FOLLOW
}

sealed class CustomChartOverlay(
    val xIndex: Float,
    val yPercent: Float,
    val res: Int,
    val colors: List<Color>,
    val icon: ImageVector,
    val type: PillCircleType,
    val duration : Long = 1000L
) {
    data class Circle(
        val _xIndex: Float,
        val _yPercent: Float,
        val radius: Dp,
        val _res: Int,
        val _type: PillCircleType = PillCircleType.COIN,
        val _colors: List<Color>,
        val _icon: ImageVector = Icons.Default.CurrencyBitcoin,
        val _duartion: Long = 1000L
    ) : CustomChartOverlay(_xIndex, _yPercent, _res,_colors,_icon, _type, _duartion )

    data class Pill(
        val _xIndex: Float,
        val _yPercent: Float,
        val width: Dp,
        val height: Dp,
        val _res: Int,
        val _colors: List<Color>,
        val _type: PillCircleType = PillCircleType.LIKE,
        val _icon: ImageVector = Icons.Default.Favorite,
        val _duartion: Long = 1000L
    ) : CustomChartOverlay(_xIndex, _yPercent, _res, _colors,_icon, _type, _duartion)
}

data class OverlayParticleData(
    val id: UUID = UUID.randomUUID(),
    val x: Float,
    val y: Float,
    val rotation: Float,
    val color: Color,
    val scaleTo: Float = Random.nextFloat() * 0.4f + 0.8f
)

@Stable
class OverlayParticleEmitterState {
    val particles: SnapshotStateList<OverlayParticleData> = mutableStateListOf()

    fun emit(x: Float, y: Float, colors: List<Color>) {
        particles.add(
            OverlayParticleData(
                x = x,
                y = y,
                rotation = Random.nextFloat() * 60f - 30f,
                color = colors[Random.nextInt(colors.size)]
            )
        )
    }

    fun remove(particle: OverlayParticleData) {
        particles.remove(particle)
    }
}

@Composable
fun rememberOverlayParticleEmitterState() = remember { OverlayParticleEmitterState() }

// --- Particle Renderer (Passive) ---

@Composable
fun ParticleRenderer(
    state: OverlayParticleEmitterState,
    icon: ImageVector,
    duration: Long,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        // Overlay Particles
        state.particles.forEach { particle ->
            androidx.compose.runtime.key(particle.id) {
                FloatingParticleView(
                    particle = particle,
                    icon = icon,
                    onAnimationFinished = { state.remove(particle) },
                    duration
                )
            }
        }
    }
}

@Composable
fun CustomOverlayItemView(
    overlay: CustomChartOverlay,
    barWidth: Float,
    spacing: Float,
    canvasHeight: Float,
    isVisible: Boolean,
    icon: ImageVector = Icons.Default.HeartBroken,
) {
    // Local particle system for this overlay
    val particleState = rememberOverlayParticleEmitterState()
    val density = LocalDensity.current
    // Resource loading
    val context = LocalContext.current
    val image = remember(overlay.res) {
        val bitmap = BitmapFactory.decodeResource(context.resources, overlay.res)
        bitmap?.asImageBitmap()
    }

    // Individual entrance animation
    val entranceProgress = remember { Animatable(0f) }

    // Independent breathing/floating animation
    val infiniteTransition = rememberInfiniteTransition(label = "overlay_breathing")
    val breathingOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000 + (overlay.xIndex * 200).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    LaunchedEffect(isVisible) {
        if (isVisible) {
            entranceProgress.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    // Auto-emit particles from CENTER BOTTOM
    LaunchedEffect(isVisible, canvasHeight, barWidth) {
        if (isVisible) {
            while (isActive) {
                // 1. Base Center Position
                val barCenterX = (overlay.xIndex * (barWidth + spacing)) + (barWidth / 2)
                val centerY = canvasHeight - (canvasHeight * overlay.yPercent) + breathingOffset

                // 2. Calculate offset to the bottom edge
                val bottomOffset = with(density) {
                    when (overlay) {
                        is CustomChartOverlay.Circle -> overlay.radius.toPx()
                        is CustomChartOverlay.Pill -> overlay.height.toPx() / 2
                    }
                }

                delay(300 + Random.nextLong(200))

                particleState.emit(
                    x = barCenterX,
                    y = centerY + bottomOffset - 20,
                    colors = overlay.colors
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (image != null && entranceProgress.value > 0f) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val scale = entranceProgress.value
                val barCenterX = (overlay.xIndex * (barWidth + spacing)) + (barWidth / 2)
                val centerY = canvasHeight - (canvasHeight * overlay.yPercent) + breathingOffset

                when (overlay) {
                    is CustomChartOverlay.Circle -> {
                        val radiusPx = overlay.radius.toPx() * scale
                        val path = Path().apply {
                            addOval(
                                Rect(
                                    center = Offset(barCenterX, centerY),
                                    radius = radiusPx
                                )
                            )
                        }
                        clipPath(path) {
                            val dstWidth = radiusPx * 2
                            val dstHeight = radiusPx * 2
                            val imgWidth = image.width.toFloat()
                            val imgHeight = image.height.toFloat()
                            val scaleFactor =
                                kotlin.math.max(dstWidth / imgWidth, dstHeight / imgHeight)
                            val scaledWidth = imgWidth * scaleFactor
                            val scaledHeight = imgHeight * scaleFactor
                            val drawX = (barCenterX - radiusPx) + (dstWidth - scaledWidth) / 2
                            val drawY = (centerY - radiusPx) + (dstHeight - scaledHeight) / 2

                            drawImage(
                                image = image,
                                dstOffset = IntOffset(drawX.toInt(), drawY.toInt()),
                                dstSize = IntSize(scaledWidth.toInt(), scaledHeight.toInt())
                            )
                        }
                    }

                    is CustomChartOverlay.Pill -> {
                        val widthPx = overlay.width.toPx() * scale
                        val heightPx = overlay.height.toPx() * scale
                        val topLeft = Offset(barCenterX - widthPx / 2, centerY - heightPx / 2)
                        val path = Path().apply {
                            addRoundRect(
                                RoundRect(
                                    rect = Rect(topLeft, Size(widthPx, heightPx)),
                                    cornerRadius = CornerRadius(widthPx / 2, widthPx / 2)
                                )
                            )
                        }
                        clipPath(path) {
                            val dstWidth = widthPx
                            val dstHeight = heightPx
                            val imgWidth = image.width.toFloat()
                            val imgHeight = image.height.toFloat()
                            val scaleFactor =
                                kotlin.math.max(dstWidth / imgWidth, dstHeight / imgHeight)
                            val scaledWidth = imgWidth * scaleFactor
                            val scaledHeight = imgHeight * scaleFactor
                            val drawX = topLeft.x + (dstWidth - scaledWidth) / 2
                            val drawY = topLeft.y + (dstHeight - scaledHeight) / 2

                            drawImage(
                                image = image,
                                dstOffset = IntOffset(drawX.toInt(), drawY.toInt()),
                                dstSize = IntSize(scaledWidth.toInt(), scaledHeight.toInt())
                            )
                        }
                    }
                }
            }
        }

        // Render particles on top of the image
        ParticleRenderer(
            state = particleState,
            icon = overlay.icon,
            modifier = Modifier.fillMaxSize(),
            duration = overlay.duration
        )
    }
}

// --- Internal Animation View ---

@Composable
private fun FloatingParticleView(
    particle: OverlayParticleData,
    icon: ImageVector,
    onAnimationFinished: () -> Unit,
    duration: Long
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val yOffset = remember { Animatable(0f) } // Fixed typo here
    val xWiggle = remember { Animatable(0f) }

    LaunchedEffect(particle.id) {
        // 1. Entrance Pop
        async {
            scale.animateTo(
                targetValue = particle.scaleTo * 1.2f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            scale.animateTo(
                targetValue = particle.scaleTo,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }

        // 2. Float Upward
        async {
            yOffset.animateTo(
                targetValue = -300f - Random.nextFloat() * 200f, // Reduced float distance for overlays
                animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
            )
        }

        // 3. Wiggle
        async {
            val startWiggle = Random.nextFloat() * 40f - 20f
            xWiggle.animateTo(
                targetValue = startWiggle,
                animationSpec = tween(1000, easing = SineEasing)
            )
            xWiggle.animateTo(
                targetValue = -startWiggle,
                animationSpec = tween(1000, easing = SineEasing)
            )
        }

        // 4. Fade & Cleanup
        async {
            val randomLifespan = duration + Random.nextLong(1000)
            delay(randomLifespan)
            alpha.animateTo(0f, animationSpec = tween(500))
            onAnimationFinished()
        }
    }

    // Just an icon at the correct position
    // Since we are in a Box filling the screen, we use offset
    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = particle.color,
            modifier = Modifier
                .size(16.dp) // Smaller heart
                .offset {
                    IntOffset(
                        x = (particle.x).toInt() + xWiggle.value.toInt() - 30, // Center correction approx
                        y = (particle.y).toInt() + yOffset.value.toInt() - 30
                    )
                }
                .scale(scale.value)
                .rotate(particle.rotation)
                .alpha(alpha.value)
        )
    }
}

private val SineEasing = Easing { x -> sin(x * Math.PI).toFloat() }
