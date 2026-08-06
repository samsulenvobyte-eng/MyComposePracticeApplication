package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ═══════════════════════════════════════════════════════════════════════════════
// COLOR PALETTE - Matching Lottie JSON blue theme
// ═══════════════════════════════════════════════════════════════════════════════

private val BlueThemePalette = listOf(
    Color(0xFF42AAFF),  // Light Blue
    Color(0xFF3AA3FD),  // Blue
    Color(0xFF7783EE),  // Purple-Blue
    Color(0xFF3C868C),  // Teal-Blue
    Color(0xFF4E95D0),  // Steel Blue
    Color(0xFF018CFE),  // Deep Blue
    Color(0xFF5B68F8),  // Indigo
    Color(0xFF45DEF8),  // Cyan
)

private val HeaderGradient = listOf(
    Color(0xFF3AA3FD),
    Color(0xFF7783EE)
)

// ═══════════════════════════════════════════════════════════════════════════════
// CONFIGURATION DATA CLASS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Customizable configuration for the JSON confetti animation.
 */
data class JsonConfettiConfig(
    val particleCount: Int = 100,
    val colors: List<Color> = BlueThemePalette,
    val gravity: Float = 600f,
    val airDrag: Float = 0.985f,
    val minVelocity: Float = -1400f,
    val maxVelocity: Float = -900f,
    val spreadAngle: Float = 120f,       // Degrees of spread from center
    val duration: Int = 3700,             // Animation duration in ms
    val enable3DRotation: Boolean = true,
    val rotationSpeedX: Float = 1080f,    // Degrees per animation cycle
    val rotationSpeedY: Float = 720f
)

// ═══════════════════════════════════════════════════════════════════════════════
// PARTICLE SHAPE TYPES
// ═══════════════════════════════════════════════════════════════════════════════

private enum class JsonShapeType {
    RECTANGLE,
    TALL_RECTANGLE,
    SQUARE,
    TRIANGLE,
    PARALLELOGRAM
}

// ═══════════════════════════════════════════════════════════════════════════════
// PARTICLE DATA CLASS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Optimized particle class for high-frequency updates.
 * Using a regular class instead of a data class to avoid per-frame allocations during physics updates.
 */
private class JsonParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val width: Float,
    val height: Float,
    val color: Color,
    var rotationZ: Float,       // 2D rotation
    var rotationX: Float,       // 3D rotation X axis
    var rotationY: Float,       // 3D rotation Y axis
    val rotationSpeedZ: Float,
    val rotationSpeedX: Float,
    val rotationSpeedY: Float,
    val shapeType: JsonShapeType,
    val alpha: Float,
    val scale: Float,
    var isAlive: Boolean = true
)

// ═══════════════════════════════════════════════════════════════════════════════
// PARTICLE GENERATOR
// ═══════════════════════════════════════════════════════════════════════════════

private fun generateJsonParticles(
    centerX: Float,
    centerY: Float,
    config: JsonConfettiConfig
): List<JsonParticle> {
    val spreadRadians = (config.spreadAngle / 2) * (PI / 180f)
    
    return List(config.particleCount) {
        // Random angle within spread range (upward burst)
        val angle = -PI / 2 + Random.nextDouble(-spreadRadians, spreadRadians)
        val velocity = Random.nextFloat() * (config.maxVelocity - config.minVelocity) + config.minVelocity
        
        val vx = (cos(angle) * velocity * Random.nextFloat() * 0.8f).toFloat()
        val vy = (sin(angle) * velocity).toFloat()
        
        val shapeType = JsonShapeType.entries[Random.nextInt(JsonShapeType.entries.size)]
        
        // Size based on shape type
        val (width, height) = when (shapeType) {
            JsonShapeType.RECTANGLE -> Pair(
                Random.nextFloat() * 20f + 15f,
                Random.nextFloat() * 12f + 8f
            )
            JsonShapeType.TALL_RECTANGLE -> Pair(
                Random.nextFloat() * 8f + 5f,
                Random.nextFloat() * 30f + 20f
            )
            JsonShapeType.SQUARE -> {
                val size = Random.nextFloat() * 15f + 10f
                Pair(size, size)
            }
            JsonShapeType.TRIANGLE -> Pair(
                Random.nextFloat() * 18f + 12f,
                Random.nextFloat() * 18f + 12f
            )
            JsonShapeType.PARALLELOGRAM -> Pair(
                Random.nextFloat() * 25f + 15f,
                Random.nextFloat() * 15f + 10f
            )
        }
        
        JsonParticle(
            x = centerX + Random.nextFloat() * 20f - 10f,
            y = centerY + Random.nextFloat() * 20f - 10f,
            vx = vx,
            vy = vy,
            width = width,
            height = height,
            color = config.colors.random(),
            rotationZ = Random.nextFloat() * 360f,
            rotationX = 0f,
            rotationY = 0f,
            rotationSpeedZ = Random.nextFloat() * 720f - 360f,
            rotationSpeedX = if (config.enable3DRotation) config.rotationSpeedX * (Random.nextFloat() * 0.5f + 0.5f) else 0f,
            rotationSpeedY = if (config.enable3DRotation) config.rotationSpeedY * (Random.nextFloat() * 0.5f + 0.5f) else 0f,
            shapeType = shapeType,
            alpha = Random.nextFloat() * 0.4f + 0.6f,
            scale = Random.nextFloat() * 0.4f + 0.6f
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONFETTI EXPLOSION COMPOSABLE
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun JsonConfettiExplosion(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    config: JsonConfettiConfig = JsonConfettiConfig(),
    onAnimationEnd: (() -> Unit)? = null
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val canvasWidthPx = with(density) { maxWidth.toPx() }
        val canvasHeightPx = with(density) { maxHeight.toPx() }
        
        // Optimization: Use a regular ArrayList and trigger redraws via frameTick to avoid per-frame allocations
        val particles = remember { ArrayList<JsonParticle>() }
        var lastFrameTime by remember { mutableLongStateOf(0L) }
        var isAnimating by remember { mutableStateOf(false) }
        var frameTick by remember { mutableLongStateOf(0L) }
        
        // 3D rotation animation progress
        val rotationProgress = remember { Animatable(0f) }
        
        // Trigger animation
        LaunchedEffect(isVisible) {
            if (isVisible && !isAnimating) {
                val centerX = canvasWidthPx / 2
                val centerY = canvasHeightPx / 2
                particles.clear()
                particles.addAll(generateJsonParticles(centerX, centerY, config))
                lastFrameTime = 0L
                isAnimating = true
                
                // Animate 3D rotation progress
                rotationProgress.snapTo(0f)
                rotationProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = config.duration,
                        easing = LinearEasing
                    )
                )
            }
        }
        
        // Physics update loop
        LaunchedEffect(isAnimating) {
            if (!isAnimating) return@LaunchedEffect
            
            while (isAnimating) {
                withFrameNanos { frameTimeNanos ->
                    if (lastFrameTime == 0L) {
                        lastFrameTime = frameTimeNanos
                        return@withFrameNanos
                    }
                    
                    val deltaTime = (frameTimeNanos - lastFrameTime) / 1_000_000_000f
                    lastFrameTime = frameTimeNanos
                    
                    // Cache rotation progress for this frame
                    val currentRotationProgress = rotationProgress.value

                    // Update particles in-place using a manual loop to avoid iterator allocations
                    var anyAlive = false
                    for (i in 0 until particles.size) {
                        val particle = particles[i]
                        if (!particle.isAlive) continue
                        
                        // Apply gravity
                        val newVy = particle.vy + config.gravity * deltaTime
                        
                        // Apply air drag
                        val draggedVx = particle.vx * config.airDrag
                        val draggedVy = newVy * config.airDrag
                        
                        // Update position
                        particle.x += draggedVx * deltaTime
                        particle.y += draggedVy * deltaTime
                        particle.vx = draggedVx
                        particle.vy = draggedVy
                        
                        // Update 2D rotation
                        particle.rotationZ += particle.rotationSpeedZ * deltaTime
                        
                        // Update 3D rotations based on progress
                        particle.rotationX = particle.rotationSpeedX * currentRotationProgress
                        particle.rotationY = particle.rotationSpeedY * currentRotationProgress
                        
                        // Check if still alive (on screen + buffer)
                        particle.isAlive = particle.y <= canvasHeightPx + 150f &&
                                particle.x >= -100f && particle.x <= canvasWidthPx + 100f
                        
                        if (particle.isAlive) anyAlive = true
                    }
                    
                    // Trigger redraw
                    frameTick = frameTimeNanos

                    if (!anyAlive && particles.isNotEmpty()) {
                        isAnimating = false
                        particles.clear()
                        onAnimationEnd?.invoke()
                    }
                }
            }
        }
        
        // Optimization: Pre-allocate paths for complex shapes to avoid per-frame allocations
        val trianglePath = remember {
            Path().apply {
                moveTo(0.5f, 0f)
                lineTo(0f, 1f)
                lineTo(1f, 1f)
                close()
            }
        }
        val parallelogramPath = remember {
            Path().apply {
                moveTo(0.3f, 0f)
                lineTo(1.3f, 0f)
                lineTo(1f, 1f)
                lineTo(0f, 1f)
                close()
            }
        }

        // Render canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Optimization: Access frameTick to ensure redraws but use local reference
            @Suppress("UNUSED_VARIABLE")
            val tick = frameTick

            // Optimization: Pre-calculate constants to avoid repeated math
            val degToRad = PI.toFloat() / 180f

            // Optimization: Use a manual loop to avoid iterator allocations
            for (i in 0 until particles.size) {
                val particle = particles[i]
                if (!particle.isAlive) continue
                
                val scaledWidth = particle.width * particle.scale
                val scaledHeight = particle.height * particle.scale
                
                // Apply 3D rotation effect via scale transformation
                val scaleX = cos(particle.rotationX * degToRad).coerceIn(0.1f, 1f)
                val scaleY = cos(particle.rotationY * degToRad).coerceIn(0.1f, 1f)
                
                val finalWidth = scaledWidth * scaleX
                val finalHeight = scaledHeight * scaleY
                
                // Optimization: Use withTransform to combine translation, rotation, and scaling
                // This is more performant than creating new Path instances or nested draw blocks
                withTransform({
                    translate(particle.x, particle.y)
                    rotate(particle.rotationZ, Offset.Zero)
                    scale(finalWidth, finalHeight, Offset.Zero)
                }) {
                    when (particle.shapeType) {
                        JsonShapeType.RECTANGLE, JsonShapeType.TALL_RECTANGLE, JsonShapeType.SQUARE -> {
                            // Draw rectangle centered at (0,0) due to translate
                            drawRect(
                                color = particle.color,
                                alpha = particle.alpha,
                                topLeft = Offset(-0.5f, -0.5f),
                                size = Size(1f, 1f)
                            )
                        }
                        JsonShapeType.TRIANGLE -> {
                            // Draw pre-allocated triangle path scaled and translated
                            // The triangle is 1x1, starting at (0,0) in normalized space
                            // Adjusting to center it
                            withTransform({
                                translate(-0.5f, -0.5f)
                            }) {
                                drawPath(
                                    path = trianglePath,
                                    color = particle.color,
                                    alpha = particle.alpha
                                )
                            }
                        }
                        JsonShapeType.PARALLELOGRAM -> {
                            // Draw pre-allocated parallelogram path
                            // The base parallelogram is roughly 1x1 but wider due to skew
                            withTransform({
                                // Adjusting translation to center it roughly
                                translate(-0.65f, -0.5f)
                            }) {
                                drawPath(
                                    path = parallelogramPath,
                                    color = particle.color,
                                    alpha = particle.alpha
                                )
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
fun JsonAnimScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    var startExplosion by remember { mutableStateOf(false) }
    
    // Customizable config state
    var particleCount by remember { mutableIntStateOf(100) }
    var enable3DRotation by remember { mutableStateOf(true) }
    var gravity by remember { mutableFloatStateOf(600f) }
    
    val currentConfig = JsonConfettiConfig(
        particleCount = particleCount,
        enable3DRotation = enable3DRotation,
        gravity = gravity
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        JsonAnimHeader(onNavigateBack = onNavigateBack)
        
        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0D1B2A),
                                Color(0xFF1B263B),
                                Color(0xFF415A77)
                            )
                        )
                    )
            )
            
            // Confetti overlay
            JsonConfettiExplosion(
                isVisible = startExplosion,
                modifier = Modifier.fillMaxSize(),
                config = currentConfig,
                onAnimationEnd = { startExplosion = false }
            )
            
            // Center content with settings
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "🎊",
                    fontSize = 64.sp
                )
                
                Text(
                    text = "JSON Anim",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = "Lottie-style confetti burst in pure Compose",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Trigger button
                Button(
                    onClick = { startExplosion = true },
                    enabled = !startExplosion,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3AA3FD),
                        disabledContainerColor = Color(0xFF3AA3FD).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = if (startExplosion) "🎊 Bursting..." else "🎊 Trigger Burst!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Settings Card
                SettingsCard(
                    particleCount = particleCount,
                    onParticleCountChange = { particleCount = it },
                    enable3DRotation = enable3DRotation,
                    onEnable3DRotationChange = { enable3DRotation = it },
                    gravity = gravity,
                    onGravityChange = { gravity = it },
                    onReset = {
                        particleCount = 100
                        enable3DRotation = true
                        gravity = 600f
                    }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SettingsCard(
    particleCount: Int,
    onParticleCountChange: (Int) -> Unit,
    enable3DRotation: Boolean,
    onEnable3DRotationChange: (Boolean) -> Unit,
    gravity: Float,
    onGravityChange: (Float) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "⚙️ Customization",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            // Particle count slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Particles",
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "$particleCount",
                        color = Color(0xFF42AAFF),
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = particleCount.toFloat(),
                    onValueChange = { onParticleCountChange(it.toInt()) },
                    valueRange = 20f..200f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF42AAFF),
                        activeTrackColor = Color(0xFF42AAFF)
                    )
                )
            }
            
            // Gravity slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Gravity",
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${gravity.toInt()}",
                        color = Color(0xFF42AAFF),
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = gravity,
                    onValueChange = onGravityChange,
                    valueRange = 200f..1200f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF42AAFF),
                        activeTrackColor = Color(0xFF42AAFF)
                    )
                )
            }
            
            // 3D rotation toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "3D Rotation",
                    color = Color.White.copy(alpha = 0.8f)
                )
                Switch(
                    checked = enable3DRotation,
                    onCheckedChange = onEnable3DRotationChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF42AAFF),
                        checkedTrackColor = Color(0xFF42AAFF).copy(alpha = 0.5f)
                    )
                )
            }
            
            // Reset button
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🔄 Reset to Defaults",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun JsonAnimHeader(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(colors = HeaderGradient)
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
                text = "🎊 JSON Anim",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lottie-style animation in Compose",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun JsonAnimScreenPreview() {
    JsonAnimScreen()
}


