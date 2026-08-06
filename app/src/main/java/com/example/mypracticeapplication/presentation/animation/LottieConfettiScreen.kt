package com.example.mypracticeapplication.presentation.animation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
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
// PARTICLE CLASS
// ═══════════════════════════════════════════════════════════════════════════════

private class LottieParticle(
    val id: Int,
    var x: Float,                     // Current X position
    var y: Float,                     // Current Y position
    var vx: Float,                    // Velocity X
    var vy: Float,                    // Velocity Y
    val color: Color,
    val size: Float,
    val shape: LottieShape,
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
    var timeOffset: Float,            // Random start offset for flutter
    val brush: Brush? = null,         // Pre-calculated brush for gradient

    // Performance: Separating calculation state from draw state
    var currentX: Float = 0f,
    var currentY: Float = 0f,
    var currentRotationZ: Float = 0f,
    var currentScaleX: Float = 1f,
    var currentScaleY: Float = 1f,
    var currentAlpha: Float = 1f,
    var isAlive: Boolean = true
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
        val hasGradient = Random.nextFloat() > 0.3f
        val gradientColor = if (hasGradient) LottieBlueColors.random() else null

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
            size = Random.nextFloat() * 30f + 15f,
            shape = LottieShape.entries.random(),
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
            timeOffset = Random.nextFloat() * 100f,
            brush = gradientColor?.let {
                Brush.horizontalGradient(listOf(baseColor, it))
            }
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
                Text("Speed: ${"%.1fx".format(Locale.US, speed)}", color = Color.White, style = MaterialTheme.typography.bodySmall)
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
    var particles by remember { mutableStateOf<List<LottieParticle>>(emptyList()) }
    var frameTick by remember { mutableLongStateOf(0L) }
    val density = LocalDensity.current

    // Performance: Cache normalized paths to avoid per-frame allocations
    val trianglePath = remember {
        Path().apply {
            moveTo(0f, -0.5f)
            lineTo(-0.5f, 0.5f)
            lineTo(0.5f, 0.5f)
            close()
        }
    }
    val parallelogramPath = remember {
        Path().apply {
            val skew = 0.3f
            moveTo(-0.5f + skew, -0.5f)
            lineTo(0.5f + skew, -0.5f)
            lineTo(0.5f - skew, 0.5f)
            lineTo(-0.5f - skew, 0.5f)
            close()
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        LaunchedEffect(isVisible) {
            if (isVisible) {
                val baseMinForce = 15f * forceMultiplier
                val baseMaxForce = 40f * forceMultiplier

                val newParticles = generateLottieParticles(
                    count = particleCount,
                    centerX = widthPx / 2,
                    centerY = heightPx * startPositionY,
                    directionAngle = 270f,
                    spreadAngle = spreadAngle,
                    minForce = baseMinForce,
                    maxForce = baseMaxForce
                )
                particles = ArrayList(newParticles)

                val startTime = System.nanoTime()
                val durationNanos = 3700L * 1_000_000L

                while (true) {
                    withFrameNanos { frameTimeNanos ->
                        val elapsedNanos = frameTimeNanos - startTime
                        val progress = (elapsedNanos.toFloat() / durationNanos).coerceIn(0f, 1f)
                        val time = progress * 3.7f * speedMultiplier
                        val frames = time * 60f

                        var anyAlive = false
                        for (i in 0 until particles.size) {
                            val particle = particles[i]
                            if (!particle.isAlive) continue

                            // 1. Calculate Drag (Velocity Decay)
                            val dragEffect = particle.drag.toDouble().pow(frames.toDouble()).toFloat()

                            // 2. Apply Velocity & Gravity
                            val decaySum = (1f - dragEffect) / (1f - particle.drag)
                            val moveX = particle.vx * decaySum
                            val moveY = particle.vy * decaySum
                            val gravityDisplacement = 0.5f * (980f * particle.mass) * time * time

                            // 3. Oscillation (Flutter)
                            val oscTime = time * particle.oscillationSpeed + particle.timeOffset
                            val oscOffset = sin(oscTime) * particle.oscillationAmp

                            // Update Current Position
                            particle.currentX = particle.x + moveX + oscOffset
                            particle.currentY = particle.y + moveY + gravityDisplacement

                            // Check life
                            if (particle.currentY > heightPx + 100) {
                                particle.isAlive = false
                            } else {
                                anyAlive = true
                            }

                            // 4. 3D Tumbling
                            val spinX = particle.rotationX + particle.rotationSpeedX * frames * speedMultiplier
                            val spinY = particle.rotationY + particle.rotationSpeedY * frames * speedMultiplier
                            particle.currentRotationZ = particle.rotationZ + particle.rotationSpeedZ * frames * speedMultiplier

                            // Scale
                            particle.currentScaleX = cos(spinY * (PI.toFloat() / 180f)).coerceIn(0.05f, 1f)
                            particle.currentScaleY = cos(spinX * (PI.toFloat() / 180f)).coerceIn(0.05f, 1f)

                            // 5. Fade Out
                            particle.currentAlpha = if (progress > 0.7f) {
                                (1f - (progress - 0.7f) / 0.3f).coerceIn(0f, 1f)
                            } else {
                                1f
                            }
                        }

                        frameTick = frameTimeNanos
                        if (!anyAlive || progress >= 1f) {
                            frameTick = 0L // Signal end of animation
                        }
                    }
                    if (frameTick == 0L) {
                        delay(300)
                        onAnimationEnd?.invoke()
                        break
                    }
                }
            } else {
                particles = ArrayList()
                frameTick = 0L
            }
        }

        if (isVisible && frameTick != 0L) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Performance: High-frequency state read of frameTick triggers redraw
                val dummy = frameTick

                for (i in 0 until particles.size) {
                    val particle = particles[i]
                    if (!particle.isAlive) continue

                    drawLottieShape(
                        particle = particle,
                        trianglePath = trianglePath,
                        parallelogramPath = parallelogramPath
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawLottieShape(
    particle: LottieParticle,
    trianglePath: Path,
    parallelogramPath: Path
) {
    val x = particle.currentX
    val y = particle.currentY
    val alpha = particle.currentAlpha
    val scaleX = particle.currentScaleX
    val scaleY = particle.currentScaleY

    val baseWidth = particle.size
    val baseHeight = when (particle.shape) {
        LottieShape.RECTANGLE -> particle.size * 0.6f
        LottieShape.TALL_RECTANGLE -> particle.size * 2f
        LottieShape.TRIANGLE -> particle.size
        LottieShape.PARALLELOGRAM -> particle.size * 0.5f
    }

    withTransform({
        translate(x, y)
        rotate(particle.currentRotationZ, pivot = Offset.Zero)
        scale(scaleX, scaleY, pivot = Offset.Zero)
    }) {
        when (particle.shape) {
            LottieShape.RECTANGLE, LottieShape.TALL_RECTANGLE -> {
                val drawWidth = if (particle.shape == LottieShape.TALL_RECTANGLE) baseWidth / 2f else baseWidth
                if (particle.brush != null) {
                    drawRect(
                        brush = particle.brush,
                        topLeft = Offset(-drawWidth / 2f, -baseHeight / 2f),
                        size = Size(drawWidth, baseHeight),
                        alpha = alpha
                    )
                } else {
                    drawRect(
                        color = particle.color,
                        topLeft = Offset(-drawWidth / 2f, -baseHeight / 2f),
                        size = Size(drawWidth, baseHeight),
                        alpha = alpha
                    )
                }
            }
            LottieShape.TRIANGLE -> {
                // Performance: Reuse normalized path and scale it in place
                withTransform({
                    scale(baseWidth, baseHeight, pivot = Offset.Zero)
                }) {
                    if (particle.brush != null) {
                        drawPath(path = trianglePath, brush = particle.brush, alpha = alpha)
                    } else {
                        drawPath(path = trianglePath, color = particle.color, alpha = alpha)
                    }
                }
            }
            LottieShape.PARALLELOGRAM -> {
                // Performance: Reuse normalized path and scale it in place
                withTransform({
                    scale(baseWidth, baseHeight, pivot = Offset.Zero)
                }) {
                    if (particle.brush != null) {
                        drawPath(path = parallelogramPath, brush = particle.brush, alpha = alpha)
                    } else {
                        drawPath(path = parallelogramPath, color = particle.color, alpha = alpha)
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
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
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
    LottieConfettiScreen()
}


