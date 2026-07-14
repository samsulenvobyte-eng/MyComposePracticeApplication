package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

// ═══════════════════════════════════════════════════════════════════════════════
// CONSTANTS & COLORS
// ═══════════════════════════════════════════════════════════════════════════════

private val LottieBlueColors = listOf(
    Color(0xFF42AAF8), // Primary Blue
    Color(0xFF2DB3F9), // Lighter Blue
    Color(0xFF0073BC), // Darker Blue
    Color(0xFF59A3CE), // Muted Blue
    Color(0xFF7780FD), // Purple-ish Blue (for variety)
    Color(0xFFE0E0E0), // Silver/Light Grey
    Color(0xFFB0B0B0)  // Darker Grey
)

private enum class LottieShape {
    RECTANGLE,
    TALL_RECTANGLE,
    TRIANGLE,
    PARALLELOGRAM
}

// ═══════════════════════════════════════════════════════════════════════════════
// PARTICLE DATA CLASS
// ═══════════════════════════════════════════════════════════════════════════════

private data class LottieParticle(
    val id: Int,
    var x: Float,                     // Current X position
    var y: Float,                     // Current Y position
    var vx: Float,                    // Velocity X
    var vy: Float,                    // Velocity Y
    val color: Color,
    val gradientColor: Color?,
    val brush: Brush?,
    val size: Float,
    val shape: LottieShape,
    val heightMultiplier: Float,
    var rotationX: Float,             // Current 3D Rotation X
    var rotationY: Float,             // Current 3D Rotation Y
    var rotationZ: Float,             // Current 2D Rotation Z
    val rotationSpeedX: Float,
    val rotationSpeedY: Float,
    val rotationSpeedZ: Float,
    val drag: Float,                  // Air resistance (0.9 - 0.99)
    val mass: Float,                  // Gravity multiplier
    val oscillationSpeed: Float,      // Flutter speed
    val oscillationAmp: Float,        // Flutter intensity
    var timeOffset: Float             // Random start offset for flutter
)

// ═══════════════════════════════════════════════════════════════════════════════
// PARTICLE GENERATOR
// ═══════════════════════════════════════════════════════════════════════════════

private fun generateLottieParticles(
    count: Int,
    centerX: Float,
    centerY: Float,
    directionAngle: Float = 270f, // Default Up
    spreadAngle: Float = 90f,     // Default 90 degrees fan
    minForce: Float = 15f,
    maxForce: Float = 40f
): List<LottieParticle> {
    return List(count) { index ->
        val baseColor = LottieBlueColors.random()
        val gradientColor = if (Random.nextFloat() > 0.3f) LottieBlueColors.random() else null
        val shape = LottieShape.entries.random()

        // Directional Cannon Physics
        val randomSpread = (Random.nextFloat() - 0.5f) * spreadAngle
        val angle = directionAngle + randomSpread
        val angleRad = angle * (PI.toFloat() / 180f)

        // Initial "Pop" force - Varied for depth
        val force = Random.nextFloat() * (maxForce - minForce) + minForce

        // Velocity components
        val initialVx = cos(angleRad) * force
        val initialVy = sin(angleRad) * force

        LottieParticle(
            id = index,
            x = centerX,
            y = centerY,
            vx = initialVx,
            vy = initialVy,
            color = baseColor,
            gradientColor = gradientColor,
            brush = if (gradientColor != null) Brush.horizontalGradient(listOf(baseColor, gradientColor)) else null,
            size = Random.nextFloat() * 30f + 15f,
            shape = shape,
            heightMultiplier = when (shape) {
                LottieShape.RECTANGLE -> 0.6f
                LottieShape.TALL_RECTANGLE -> 2.0f
                LottieShape.TRIANGLE -> 1.0f
                LottieShape.PARALLELOGRAM -> 0.5f
            },
            rotationX = Random.nextFloat() * 360f,
            rotationY = Random.nextFloat() * 360f,
            rotationZ = Random.nextFloat() * 360f,
            rotationSpeedX = (Random.nextFloat() - 0.5f) * 15f,
            rotationSpeedY = (Random.nextFloat() - 0.5f) * 15f,
            rotationSpeedZ = (Random.nextFloat() - 0.5f) * 10f,
            drag = Random.nextFloat() * 0.05f + 0.92f, // 0.92 - 0.97
            mass = Random.nextFloat() * 0.5f + 0.8f,
            oscillationSpeed = Random.nextFloat() * 0.1f + 0.05f,
            oscillationAmp = Random.nextFloat() * 2f,
            timeOffset = Random.nextFloat() * 100f
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONTROLS COMPOSABLE
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun ConfettiControls(
    spread: Float,
    onSpreadChange: (Float) -> Unit,
    positionY: Float,
    onPositionChange: (Float) -> Unit,
    heightFraction: Float,
    onHeightChange: (Float) -> Unit,
    speed: Float,
    onSpeedChange: (Float) -> Unit,
    onFire: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF1E1E1E).copy(alpha = 0.9f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Fire Button
        Button(
            onClick = onFire,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF42AAF8)
            ),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("🚀 FIRE CANNON", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Column
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                // Spread Slider
                Text("Spread: ${spread.toInt()}°", color = Color.White, style = MaterialTheme.typography.bodySmall)
                androidx.compose.material3.Slider(
                    value = spread,
                    onValueChange = onSpreadChange,
                    valueRange = 0f..180f,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = Color(0xFF2DB3F9),
                        activeTrackColor = Color(0xFF2DB3F9)
                    )
                )
                
                 // Height/Force Slider
                Text("Peak Height: ${(heightFraction * 100).toInt()}%", color = Color.White, style = MaterialTheme.typography.bodySmall)
                androidx.compose.material3.Slider(
                    value = heightFraction,
                    onValueChange = onHeightChange,
                    valueRange = 0.2f..1.5f,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = Color(0xFFE0E0E0),
                        activeTrackColor = Color(0xFFE0E0E0)
                    )
                )
            }
            
            // Right Column
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                // Position Slider
                Text("Start Y: ${(positionY * 100).toInt()}%", color = Color.White, style = MaterialTheme.typography.bodySmall)
                androidx.compose.material3.Slider(
                    value = positionY,
                    onValueChange = onPositionChange,
                    valueRange = 0f..1f,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = Color(0xFF59A3CE),
                        activeTrackColor = Color(0xFF59A3CE)
                    )
                )

                // Speed Slider
                val speedText = remember(speed) { "Speed: ${String.format(java.util.Locale.US, "%.1fx", speed)}" }
                Text(speedText, color = Color.White, style = MaterialTheme.typography.bodySmall)
                androidx.compose.material3.Slider(
                    value = speed,
                    onValueChange = onSpeedChange,
                    valueRange = 0.5f..3.0f,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = Color(0xFF7780FD),
                        activeTrackColor = Color(0xFF7780FD)
                    )
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONFETTI EXPLOSION COMPOSABLE
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun LottieConfettiExplosion(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    particleCount: Int = 100,
    spreadAngle: Float = 90f,
    startPositionY: Float = 0.5f,
    forceMultiplier: Float = 1f,
    speedMultiplier: Float = 1f,
    onAnimationEnd: (() -> Unit)? = null
) {
    val animationProgress = remember { Animatable(0f) }
    var particles by remember { mutableStateOf(emptyList<LottieParticle>()) }

    // Pre-allocate paths for performance
    val sharedPath = remember { Path() }
    val triangleTemplate = remember {
        Path().apply {
            moveTo(0f, -0.5f)
            lineTo(-0.5f, 0.5f)
            lineTo(0.5f, 0.5f)
            close()
        }
    }
    val parallelogramTemplate = remember {
        val skew = 0.3f
        Path().apply {
            moveTo(-0.5f + skew, -0.5f)
            lineTo(0.5f + skew, -0.5f)
            lineTo(0.5f - skew, 0.5f)
            lineTo(-0.5f - skew, 0.5f)
            close()
        }
    }

    // We need the size to center the burst, but Canvas size is only available during draw.
    // We'll use a BoxWithConstraints or just assume a reasonable center for now.
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(isVisible, canvasSize) {
        if (isVisible && canvasSize != Size.Zero) {
            // Generate particles from the location
            val baseMinForce = 15f * forceMultiplier
            val baseMaxForce = 40f * forceMultiplier
            
            particles = generateLottieParticles(
                count = particleCount, 
                centerX = canvasSize.width / 2, 
                centerY = canvasSize.height * startPositionY, 
                directionAngle = 270f, 
                spreadAngle = spreadAngle,
                minForce = baseMinForce,
                maxForce = baseMaxForce
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
    
    if (isVisible) {
        Canvas(modifier = modifier.fillMaxSize()) {
            if (canvasSize == Size.Zero) {
                canvasSize = size
            }

            val progress = animationProgress.value
            val time = progress * 3.7f * speedMultiplier // Dynamic Speed

            for (i in particles.indices) {
                val particle = particles[i]
                // 1. Calculate Drag (Velocity Decay)
                val frames = time * 60f
                val dragEffect = particle.drag.toDouble().pow(frames.toDouble()).toFloat()

                // 2. Apply Velocity & Gravity
                val decaySum = (1f - dragEffect) / (1f - particle.drag)
                val moveX = particle.vx * decaySum
                val moveY = particle.vy * decaySum

                // Gravity
                val gravityDisplacement = 0.5f * (980f * particle.mass) * time * time

                // 3. Oscillation (Flutter)
                val oscTime = time * particle.oscillationSpeed + particle.timeOffset
                val oscOffset = sin(oscTime) * particle.oscillationAmp

                // Final Position
                val currentX = particle.x + moveX + oscOffset
                val currentY = particle.y + moveY + gravityDisplacement

                // Skip if out of bounds (optimization)
                if (currentY > size.height + 100) continue

                // 4. 3D Tumbling
                val spinX = particle.rotationX + particle.rotationSpeedX * frames * speedMultiplier
                val spinY = particle.rotationY + particle.rotationSpeedY * frames * speedMultiplier
                val spinZ = particle.rotationZ + particle.rotationSpeedZ * frames * speedMultiplier

                // Scale
                val scaleX = cos(spinY * (PI.toFloat() / 180f)).coerceIn(0.05f, 1f)
                val scaleY = cos(spinX * (PI.toFloat() / 180f)).coerceIn(0.05f, 1f)

                // 5. Fade Out
                val alpha = if (progress > 0.7f) {
                    (1f - (progress - 0.7f) / 0.3f).coerceIn(0f, 1f)
                } else {
                    1f
                }

                // Draw
                val width = particle.size * scaleX
                val height = particle.size * particle.heightMultiplier * scaleY

                withTransform({
                    translate(currentX, currentY)
                    rotate(spinZ, Offset.Zero)
                }) {
                    when (particle.shape) {
                        LottieShape.RECTANGLE, LottieShape.TALL_RECTANGLE -> {
                            val rectWidth = if (particle.shape == LottieShape.TALL_RECTANGLE) width / 2 else width
                            if (particle.brush != null) {
                                drawRect(
                                    brush = particle.brush,
                                    topLeft = Offset(-rectWidth / 2, -height / 2),
                                    size = Size(rectWidth, height),
                                    alpha = alpha
                                )
                            } else {
                                drawRect(
                                    color = particle.color,
                                    topLeft = Offset(-rectWidth / 2, -height / 2),
                                    size = Size(rectWidth, height),
                                    alpha = alpha
                                )
                            }
                        }
                        LottieShape.TRIANGLE -> {
                            sharedPath.reset()
                            sharedPath.addPath(triangleTemplate)
                            withTransform({
                                scale(width, height, Offset.Zero)
                            }) {
                                if (particle.brush != null) {
                                    drawPath(path = sharedPath, brush = particle.brush, alpha = alpha)
                                } else {
                                    drawPath(path = sharedPath, color = particle.color, alpha = alpha)
                                }
                            }
                        }
                        LottieShape.PARALLELOGRAM -> {
                            sharedPath.reset()
                            sharedPath.addPath(parallelogramTemplate)
                            withTransform({
                                scale(width, height, Offset.Zero)
                            }) {
                                if (particle.brush != null) {
                                    drawPath(path = sharedPath, brush = particle.brush, alpha = alpha)
                                } else {
                                    drawPath(path = sharedPath, color = particle.color, alpha = alpha)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SCREEN COMPOSABLE
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun LottieConfettiScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(false) }
    
    // Cannon Configuration State
    var spread by remember { mutableFloatStateOf(90f) }
    var positionY by remember { mutableFloatStateOf(1.0f) }
    var forceMultiplier by remember { mutableFloatStateOf(0.8f) } // Default power
    var speed by remember { mutableFloatStateOf(1.0f) }
    
    // Trigger State
    var triggerCount by remember { mutableIntStateOf(0) }
    
    // Auto-trigger
    LaunchedEffect(Unit) {
        delay(500)
        isPlaying = true
    }
    
    // Restart logic
    LaunchedEffect(triggerCount) {
        if (triggerCount > 0) {
            isPlaying = false
            delay(50) 
            isPlaying = true
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        LottieConfettiHeader(onNavigateBack = onNavigateBack)
        
        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // Transparent/Dark background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0A0A0A))
            )
            
            // Confetti Animation
            LottieConfettiExplosion(
                isVisible = isPlaying,
                particleCount = 150,
                spreadAngle = spread,
                startPositionY = positionY,
                forceMultiplier = forceMultiplier,
                speedMultiplier = speed,
                onAnimationEnd = { isPlaying = false }
            )
            
            // Controls Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                ConfettiControls(
                    spread = spread,
                    onSpreadChange = { spread = it },
                    positionY = positionY,
                    onPositionChange = { positionY = it },
                    heightFraction = forceMultiplier,
                    onHeightChange = { forceMultiplier = it },
                    speed = speed,
                    onSpeedChange = { speed = it },
                    onFire = { 
                        triggerCount++ 
                    }
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// HEADER COMPOSABLE
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun LottieConfettiHeader(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF42AAF8),
                        Color(0xFF7780FD)
                    )
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
                text = "💙 Lottie Cannon",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Interactive Physics Demo",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// PREVIEW
// ═══════════════════════════════════════════════════════════════════════════════

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LottieConfettiScreenPreview() {
    com.example.mypracticeapplication.presentation.theme.MyPracticeApplicationTheme {
        LottieConfettiScreen()
    }
}


