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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
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
// FESTIVE PALETTE - Extracted from Lottie JSON
// ═══════════════════════════════════════════════════════════════════════════════

private val FestivePalette = listOf(
    Color(0xFF57B3FE),  // Vibrant Blue
    Color(0xFF7683EE),  // Lavender
    Color(0xFF018CFD),  // Deep Blue
    Color(0xFF63717D),  // Steel Gray
    Color(0xFF9EA7B0),  // Muted Silver
    Color(0xFF42A6FF),  // Sky Blue
    Color(0xFF5B68F8),  // Royal Indigo
    Color(0xFF44DBF8)   // Bright Cyan
)

private val FestiveHeaderGradient = listOf(
    Color(0xFF7683EE),
    Color(0xFF57B3FE)
)

// ═══════════════════════════════════════════════════════════════════════════════
// CONFIGURATION
// ═══════════════════════════════════════════════════════════════════════════════

data class FestiveConfettiConfig(
    val particleCount: Int = 80,
    val colors: List<Color> = FestivePalette,
    val gravity: Float = 500f,
    val airDrag: Float = 0.99f,
    val trailLength: Int = 12,           // Number of segments in the tail
    val wiggleIntensity: Float = 30f,    // Sinusoidal wavy motion amplitude
    val duration: Int = 4000,
    val horizontalSpread: Float = 1200f
)

// ═══════════════════════════════════════════════════════════════════════════════
// DATA MODELS
// ═══════════════════════════════════════════════════════════════════════════════

private enum class FestiveShapeType {
    RECTANGLE,
    TRIANGLE,
    PARALLELOGRAM,
    POLYLINE  // The "trail" forming shape
}

private data class TrailPoint(
    val x: Float,
    val y: Float,
    val rotationZ: Float
)

private class FestiveParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val width: Float,
    val height: Float,
    val color: Color,
    var rotationZ: Float,
    val rotationSpeedZ: Float,
    val horizontalOffset: Float, // Starting phase for wiggle
    val horizontalFreq: Float,   // Frequency for wiggle
    val shapeType: FestiveShapeType,
    val alpha: Float,
    val trail: ArrayList<TrailPoint> = ArrayList(),
    var isAlive: Boolean = true
)

// ═══════════════════════════════════════════════════════════════════════════════
// LOGIC
// ═══════════════════════════════════════════════════════════════════════════════

private fun generateFestiveParticles(
    width: Float,
    height: Float,
    config: FestiveConfettiConfig
): ArrayList<FestiveParticle> {
    val particles = ArrayList<FestiveParticle>(config.particleCount)
    repeat(config.particleCount) {
        val centerX = width / 2
        val startX = centerX + Random.nextFloat() * 400f - 200f
        val startY = -100f // Start above screen
        
        // Initial burst outward
        val vx = (Random.nextFloat() * 800f - 400f)
        val vy = (Random.nextFloat() * 300f + 200f)
        
        val shapeType = FestiveShapeType.entries[Random.nextInt(FestiveShapeType.entries.size)]
        
        particles.add(
            FestiveParticle(
                x = startX,
                y = startY,
                vx = vx,
                vy = vy,
                width = Random.nextFloat() * 15f + 10f,
                height = Random.nextFloat() * 10f + 8f,
                color = config.colors.random(),
                rotationZ = Random.nextFloat() * 360f,
                rotationSpeedZ = Random.nextFloat() * 400f - 200f,
                horizontalOffset = Random.nextFloat() * PI.toFloat() * 2,
                horizontalFreq = Random.nextFloat() * 1.5f + 0.5f,
                shapeType = shapeType,
                alpha = Random.nextFloat() * 0.5f + 0.5f
            )
        )
    }
    return particles
}

@Composable
fun FestiveConfettiExplosion(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    config: FestiveConfettiConfig = FestiveConfettiConfig(),
    onAnimationEnd: (() -> Unit)? = null
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val canvasWidthPx = with(density) { maxWidth.toPx() }
        val canvasHeightPx = with(density) { maxHeight.toPx() }
        
        val particles = remember { ArrayList<FestiveParticle>() }
        var frameTick by remember { mutableLongStateOf(0L) }
        var lastFrameTime by remember { mutableLongStateOf(0L) }
        var isAnimating by remember { mutableStateOf(false) }
        var elapsedTime by remember { mutableFloatStateOf(0f) }
        
        LaunchedEffect(isVisible) {
            if (isVisible && !isAnimating) {
                particles.clear()
                particles.addAll(generateFestiveParticles(canvasWidthPx, canvasHeightPx, config))
                lastFrameTime = 0L
                elapsedTime = 0f
                isAnimating = true
            }
        }
        
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
                    elapsedTime += deltaTime
                    
                    var anyAlive = false
                    for (i in 0 until particles.size) {
                        val particle = particles[i]
                        if (!particle.isAlive) continue
                        
                        // Physics
                        particle.vy = (particle.vy + config.gravity * deltaTime) * config.airDrag
                        particle.vx = particle.vx * config.airDrag
                        
                        // Horizontal wiggle (Simulating Lottie paths)
                        val wiggle = sin(elapsedTime * particle.horizontalFreq + particle.horizontalOffset) * config.wiggleIntensity * 0.1f
                        
                        particle.x += (particle.vx + wiggle) * deltaTime
                        particle.y += particle.vy * deltaTime
                        particle.rotationZ += particle.rotationSpeedZ * deltaTime
                        
                        // Update Trail (Trim Path effect)
                        particle.trail.add(0, TrailPoint(particle.x, particle.y, particle.rotationZ))
                        if (particle.trail.size > config.trailLength) {
                            particle.trail.removeAt(particle.trail.lastIndex)
                        }
                        
                        val stillAlive = particle.y <= canvasHeightPx + 200f &&
                                       particle.x >= -200f && particle.x <= canvasWidthPx + 200f
                        if (stillAlive) {
                            anyAlive = true
                        } else {
                            particle.isAlive = false
                        }
                    }
                    
                    if (!anyAlive && particles.isNotEmpty()) {
                        isAnimating = false
                        particles.clear()
                        onAnimationEnd?.invoke()
                    }
                    frameTick++
                }
            }
        }
        
        val sharedPath = remember { Path() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Read frameTick to trigger redraw
            val _tick = frameTick

            for (i in 0 until particles.size) {
                val particle = particles[i]
                if (!particle.isAlive) continue
                
                // Draw trails for POLYLINE type OR behind any shape if configured
                if (particle.shapeType == FestiveShapeType.POLYLINE && particle.trail.size > 1) {
                    sharedPath.reset()
                    val firstPoint = particle.trail[0]
                    sharedPath.moveTo(firstPoint.x, firstPoint.y)
                    for (j in 1 until particle.trail.size) {
                        val point = particle.trail[j]
                        sharedPath.lineTo(point.x, point.y)
                    }
                    drawPath(
                        path = sharedPath,
                        color = particle.color,
                        alpha = particle.alpha * 0.6f,
                        style = Stroke(width = 8f, cap = StrokeCap.Round)
                    )
                }
                
                // Draw Head Shape
                if (particle.shapeType != FestiveShapeType.POLYLINE) {
                    rotate(particle.rotationZ, Offset(particle.x, particle.y)) {
                        when (particle.shapeType) {
                            FestiveShapeType.RECTANGLE -> {
                                drawRect(
                                    color = particle.color,
                                    topLeft = Offset(particle.x - particle.width / 2, particle.y - particle.height / 2),
                                    size = Size(particle.width, particle.height),
                                    alpha = particle.alpha
                                )
                            }
                            FestiveShapeType.TRIANGLE -> {
                                sharedPath.reset()
                                sharedPath.moveTo(particle.x, particle.y - particle.height / 2)
                                sharedPath.lineTo(particle.x - particle.width / 2, particle.y + particle.height / 2)
                                sharedPath.lineTo(particle.x + particle.width / 2, particle.y + particle.height / 2)
                                sharedPath.close()
                                drawPath(sharedPath, particle.color, alpha = particle.alpha)
                            }
                            FestiveShapeType.PARALLELOGRAM -> {
                                val skew = particle.width * 0.4f
                                sharedPath.reset()
                                sharedPath.moveTo(particle.x - particle.width / 2 + skew, particle.y - particle.height / 2)
                                sharedPath.lineTo(particle.x + particle.width / 2 + skew, particle.y - particle.height / 2)
                                sharedPath.lineTo(particle.x + particle.width / 2 - skew, particle.y + particle.height / 2)
                                sharedPath.lineTo(particle.x - particle.width / 2 - skew, particle.y + particle.height / 2)
                                sharedPath.close()
                                drawPath(sharedPath, particle.color, alpha = particle.alpha)
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SCREEN
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun FestiveConfettiScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    var trigger by remember { mutableStateOf(false) }
    var particleCount by remember { mutableIntStateOf(80) }
    var trailLength by remember { mutableIntStateOf(12) }
    var wiggleIntensity by remember { mutableFloatStateOf(30f) }
    
    val config = FestiveConfettiConfig(
        particleCount = particleCount,
        trailLength = trailLength,
        wiggleIntensity = wiggleIntensity
    )
    
    Column(modifier = modifier.fillMaxSize().background(Color(0xFF0F172A))) {
        // Header
        FestiveHeader(onBack = onNavigateBack)
        
        Box(modifier = Modifier.fillMaxSize().weight(1f)) {
            // Background Layer
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.radialGradient(
                    0f to Color(0xFF1E293B),
                    1f to Color(0xFF0F172A)
                )
            ))
            
            // Animation Layer
            FestiveConfettiExplosion(
                modifier = Modifier.fillMaxSize(),
                isVisible = trigger,
                config = config,
                onAnimationEnd = { trigger = false }
            )
            
            // UI Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text("🎪", fontSize = 72.sp)
                Text(
                    "Multicolor Trails",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Recreating Lottie 'Trim Path' trails",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Button(
                    onClick = { trigger = true },
                    enabled = !trigger,
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7683EE))
                ) {
                    Text(if (trigger) "🎊 Celebrating..." else "🎊 Let's Party!", fontWeight = FontWeight.Bold)
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("⚙️ Trail Tuning", color = Color.White, fontWeight = FontWeight.Bold)
                        
                        CustomSlider("Particles", particleCount.toFloat(), 20f..150f) { particleCount = it.toInt() }
                        CustomSlider("Trail Length", trailLength.toFloat(), 5f..30f) { trailLength = it.toInt() }
                        CustomSlider("Wiggle intensity", wiggleIntensity, 0f..100f) { wiggleIntensity = it }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            Text(value.toInt().toString(), color = Color(0xFF57B3FE), fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(thumbColor = Color(0xFF57B3FE), activeTrackColor = Color(0xFF57B3FE))
        )
    }
}

@Composable
private fun FestiveHeader(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(FestiveHeaderGradient))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White) }
        Column {
            Text("Festive Confetti", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Advanced Path Animation", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FestivePreview() {
    FestiveConfettiScreen()
}


