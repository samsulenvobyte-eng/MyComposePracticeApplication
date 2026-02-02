package com.example.mypracticeapplication.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

// ═══════════════════════════════════════════════════════════════════════════════
// DATA MODELS
// ═══════════════════════════════════════════════════════════════════════════════

enum class ConfettiShape {
    RECTANGLE,
    CIRCLE,
    STAR
}

internal data class ConfettiParticle(
    val id: Int,
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val gradientColor: Color?,
    val size: Float,
    val shape: ConfettiShape,
    var rotationX: Float,
    var rotationY: Float,
    var rotationZ: Float,
    val rotationSpeedX: Float,
    val rotationSpeedY: Float,
    val rotationSpeedZ: Float,
    val drag: Float,
    val mass: Float,
    val oscillationSpeed: Float,
    val oscillationAmp: Float,
    var timeOffset: Float
)

// ═══════════════════════════════════════════════════════════════════════════════
// COMPONENT
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun LottieConfettiView(
    triggerKey: Any,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    particleCount: Int = 84,
    spreadAngle: Float = 108f,
    startPositionY: Float = 0.44f,
    forceMultiplier: Float = 1.5f,
    speedMultiplier: Float = 0.5f,
    sizeMultiplier: Float = 0.5f,
    is3DEnabled: Boolean = false,
    onAnimationEnd: (() -> Unit)? = null
) {
    val animationProgress = remember { Animatable(0f) }
    var particles by remember { mutableStateOf(emptyList<ConfettiParticle>()) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(triggerKey, canvasSize) {
        if (canvasSize != Size.Zero) {
            val baseMinForce = 15f * forceMultiplier
            val baseMaxForce = 40f * forceMultiplier

            particles = generateConfettiParticles(
                count = particleCount,
                centerX = canvasSize.width / 2,
                centerY = canvasSize.height * startPositionY,
                colors = colors,
                spreadAngle = spreadAngle,
                minForce = baseMinForce,
                maxForce = baseMaxForce,
                sizeMultiplier = sizeMultiplier
            )
            animationProgress.snapTo(0f)
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 3700,
                    easing = LinearEasing
                )
            )
            delay(300)
            onAnimationEnd?.invoke()
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        if (canvasSize == Size.Zero) {
            canvasSize = size
        }

        if (animationProgress.value <= 0f || animationProgress.value >= 1f) return@Canvas

        val progress = animationProgress.value
        val time = progress * 3.7f * speedMultiplier

        particles.forEach { particle ->
            val frames = time * 60f
            val dragEffect = particle.drag.toDouble().pow(frames.toDouble()).toFloat()
            val decaySum = (1f - dragEffect) / (1f - particle.drag)
            val moveX = particle.vx * decaySum
            val moveY = particle.vy * decaySum
            val gravityDisplacement = 0.5f * (980f * particle.mass) * time * time
            val oscTime = time * particle.oscillationSpeed + particle.timeOffset
            val oscOffset = sin(oscTime) * particle.oscillationAmp

            val currentX = particle.x + moveX + oscOffset
            val currentY = particle.y + moveY + gravityDisplacement

            if (currentY > size.height + 100) return@forEach

            val spinX = particle.rotationX + particle.rotationSpeedX * frames * speedMultiplier
            val spinY = particle.rotationY + particle.rotationSpeedY * frames * speedMultiplier
            val spinZ = particle.rotationZ + particle.rotationSpeedZ * frames * speedMultiplier

            val scaleX = if (is3DEnabled) cos(spinY * (PI.toFloat() / 180f)).coerceIn(0.05f, 1f) else 1f
            val scaleY = if (is3DEnabled) cos(spinX * (PI.toFloat() / 180f)).coerceIn(0.05f, 1f) else 1f

            val alpha = if (progress > 0.7f) {
                (1f - (progress - 0.7f) / 0.3f).coerceIn(0f, 1f)
            } else 1f

            rotate(degrees = spinZ, pivot = Offset(currentX, currentY)) {
                drawConfettiShape(
                    particle = particle,
                    x = currentX,
                    y = currentY,
                    scaleX = scaleX,
                    scaleY = scaleY,
                    alpha = alpha
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// INTERNAL HELPERS
// ═══════════════════════════════════════════════════════════════════════════════

private fun generateConfettiParticles(
    count: Int,
    centerX: Float,
    centerY: Float,
    colors: List<Color>,
    directionAngle: Float = 270f,
    spreadAngle: Float = 90f,
    minForce: Float = 15f,
    maxForce: Float = 40f,
    sizeMultiplier: Float = 1f
): List<ConfettiParticle> {
    return List(count) { index ->
        val baseColor = colors.random()
        val randomSpread = (Random.nextFloat() - 0.5f) * spreadAngle
        val angle = directionAngle + randomSpread
        val angleRad = angle * (PI.toFloat() / 180f)
        val force = Random.nextFloat() * (maxForce - minForce) + minForce
        val initialVx = cos(angleRad) * force
        val initialVy = sin(angleRad) * force

        ConfettiParticle(
            id = index,
            x = centerX,
            y = centerY,
            vx = initialVx,
            vy = initialVy,
            color = baseColor,
            gradientColor = null,
            size = (Random.nextFloat() * 30f + 15f) * sizeMultiplier,
            shape = ConfettiShape.entries.random(),
            rotationX = Random.nextFloat() * 360f,
            rotationY = Random.nextFloat() * 360f,
            rotationZ = Random.nextFloat() * 360f,
            rotationSpeedX = (Random.nextFloat() - 0.5f) * 15f,
            rotationSpeedY = (Random.nextFloat() - 0.5f) * 15f,
            rotationSpeedZ = (Random.nextFloat() - 0.5f) * 10f,
            drag = Random.nextFloat() * 0.05f + 0.92f,
            mass = Random.nextFloat() * 0.5f + 0.8f,
            oscillationSpeed = Random.nextFloat() * 0.1f + 0.05f,
            oscillationAmp = Random.nextFloat() * 2f,
            timeOffset = Random.nextFloat() * 100f
        )
    }
}

private fun DrawScope.drawConfettiShape(
    particle: ConfettiParticle,
    x: Float,
    y: Float,
    scaleX: Float,
    scaleY: Float,
    alpha: Float
) {
    val width = particle.size * scaleX
    val height = when (particle.shape) {
        ConfettiShape.RECTANGLE -> particle.size * 0.6f * scaleY
        ConfettiShape.CIRCLE, ConfettiShape.STAR -> particle.size * scaleY
    }
    val color = particle.color.copy(alpha = alpha)

    when (particle.shape) {
        ConfettiShape.RECTANGLE -> {
            drawRect(
                color = color,
                topLeft = Offset(x - width / 2, y - height / 2),
                size = Size(width, height)
            )
        }
        ConfettiShape.CIRCLE -> {
            drawOval(
                color = color,
                topLeft = Offset(x - width / 2, y - height / 2),
                size = Size(width, height)
            )
        }
        ConfettiShape.STAR -> {
            val path = Path().apply {
                val outerRadius = width / 2
                val innerRadius = outerRadius * 0.4f
                val points = 5
                for (i in 0 until points * 2) {
                    val radius = if (i % 2 == 0) outerRadius else innerRadius
                    val angle = i * PI.toFloat() / points - PI.toFloat() / 2
                    val px = x + cos(angle) * radius
                    val py = y + sin(angle) * radius
                    if (i == 0) moveTo(px, py) else lineTo(px, py)
                }
                close()
            }
            drawPath(path = path, color = color)
        }
    }
}
