package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// -----------------------------------------------------------------------------
// COLORS (Exact Match from Lottie)
// -----------------------------------------------------------------------------
private val ColorGreenCircle = Color(0xFF26AA43)
private val ColorConfettiTeal = Color(0xFF41AF80)
private val ColorConfettiBlue = Color(0xFF4683F5)
private val ColorConfettiPink = Color(0xFFF82681)
private val ColorConfettiOrange = Color(0xFFFFBC32)

private val AllConfettiColors = listOf(
    ColorConfettiTeal,
    ColorConfettiBlue,
    ColorConfettiPink,
    ColorConfettiOrange
)

@Composable
fun SuccessAnimationScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            SuccessAnimationContent()
        }
    }
}

@Composable
fun SuccessAnimationContent(modifier: Modifier = Modifier) {
    var isPlaying by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    // Animation States
    val circleScale = remember { Animatable(0f) }
    val checkmarkProgress = remember { Animatable(0f) }

    // Pre-calculate checkmark path and measurement
    val checkmarkPath = remember(density) {
        with(density) {
            Path().apply {
                moveTo(32.dp.toPx(), 58.dp.toPx())
                lineTo(48.dp.toPx(), 74.dp.toPx()) // Tip
                lineTo(82.dp.toPx(), 38.dp.toPx()) // End
            }
        }
    }
    val checkmarkPathMeasure = remember { PathMeasure() }.apply {
        setPath(checkmarkPath, false)
    }
    val checkmarkPathLength = checkmarkPathMeasure.length
    val checkmarkStroke = remember(density) {
        Stroke(
            width = with(density) { 8.dp.toPx() },
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    }

    // Confetti State
    var startConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            // Reset
            circleScale.snapTo(0f)
            checkmarkProgress.snapTo(0f)
            startConfetti = false
            
            delay(50) 
            
            // 1. Circle Pops In
            launch {
                circleScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = 0.6f, // Slightly less bouncy to match Lottie "Elastic"
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            
            // 2. Confetti Bursts immediately as circle expands
            delay(150) 
            startConfetti = true 
            
            // 3. Checkmark Draws (Staggered slightly)
            delay(100)
            checkmarkProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(350, easing = LinearOutSlowInEasing)
            )
            
            delay(1500)
            isPlaying = false
        }
    }
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(400.dp)
                .clickable { if (!isPlaying) isPlaying = true }
        ) {
            // Confetti Burst Layer (Behind and On Top possibility, but usually Top is fine)
            ConfettiBurst(
                modifier = Modifier.fillMaxSize(),
                isVisible = startConfetti
            )

            // Green Circle Background
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .graphicsLayer {
                        val scale = circleScale.value
                        scaleX = scale
                        scaleY = scale
                    }
                    .background(ColorGreenCircle, CircleShape)
            )

            // Checkmark
            val animatedCheckmarkPath = remember { Path() }
            Canvas(
                modifier = Modifier
                    .size(110.dp)
                    .graphicsLayer {
                        val scale = circleScale.value.coerceAtLeast(0f)
                        scaleX = scale
                        scaleY = scale
                    }
            ) {
                // Read animation state
                val progress = checkmarkProgress.value

                // Create animated path segment
                animatedCheckmarkPath.reset()
                checkmarkPathMeasure.getSegment(
                    0f,
                    checkmarkPathLength * progress,
                    animatedCheckmarkPath,
                    true
                )

                drawPath(
                    path = animatedCheckmarkPath,
                    color = Color.White,
                    style = checkmarkStroke
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { isPlaying = true },
            enabled = !isPlaying,
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorGreenCircle
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = if (isPlaying) "Playing..." else "Play Success Animation",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

// -----------------------------------------------------------------------------
// CONFETTI SYSTEM
// -----------------------------------------------------------------------------

enum class ParticleShape {
    CIRCLE,
    SQUARE,
    CROSS,
    ZIGZAG,
    CURVE,
    STRIP
}

private class SuccessParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val size: Float,
    val shape: ParticleShape,
    var rotation: Float,
    val rotationSpeed: Float,
    var life: Float = 1f, // 1.0 to 0.0
)

@Composable
fun ConfettiBurst(
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    Box(modifier = modifier) {
        if (isVisible) {
            val density = LocalDensity.current

            // Pre-calculate shape templates
            val lightningPath = remember {
                Path().apply {
                    moveTo(-0.2f, -0.5f)
                    lineTo(0.3f, -0.1f)
                    lineTo(-0.1f, -0.1f)
                    lineTo(0.2f, 0.5f)
                    lineTo(-0.3f, 0.1f)
                    lineTo(0.1f, 0.1f)
                    close()
                }
            }

            val curvePath = remember {
                Path().apply {
                    moveTo(-0.5f, 0f)
                    cubicTo(
                        -0.25f, -0.5f,
                        0.25f, 0.5f,
                        0.5f, 0f
                    )
                }
            }

            val particles = remember { ArrayList<SuccessParticle>() }
            var frameTick by remember { mutableLongStateOf(0L) }
            var lastFrameTime by remember { mutableLongStateOf(0L) }
            
            LaunchedEffect(Unit) {
                with(density) {
                    val centerX = 200.dp.toPx() // Half of 400.dp box
                    val centerY = 200.dp.toPx()
                    
                    particles.clear()
                    repeat(60) {
                        val angle = Random.nextFloat() * 360f
                        val speed = Random.nextFloat() * 600f + 400f // Explosive speed
                        val rad = Math.toRadians(angle.toDouble())
                        
                        // Distribute shapes roughly evenly
                        val shape = when(Random.nextInt(6)) {
                            0 -> ParticleShape.CIRCLE
                            1 -> ParticleShape.SQUARE
                            2 -> ParticleShape.CROSS
                            3 -> ParticleShape.ZIGZAG
                            4 -> ParticleShape.STRIP
                            else -> ParticleShape.CURVE
                        }

                        particles.add(
                            SuccessParticle(
                                x = centerX,
                                y = centerY,
                                vx = (cos(rad) * speed).toFloat(),
                                vy = (sin(rad) * speed).toFloat(),
                                color = AllConfettiColors.random(),
                                size = Random.nextFloat() * 14f + 10f, // 10-24dp size
                                shape = shape,
                                rotation = Random.nextFloat() * 360f,
                                rotationSpeed = Random.nextFloat() * 720f - 360f,
                                life = 1f + Random.nextFloat() * 0.2f // Variance in life
                            )
                        )
                    }
                }
                
                lastFrameTime = 0L
                
                while (particles.isNotEmpty()) {
                    withFrameNanos { frameTimeNanos ->
                        if (lastFrameTime == 0L) {
                            lastFrameTime = frameTimeNanos
                            return@withFrameNanos
                        }
                        
                        val deltaTime = (frameTimeNanos - lastFrameTime) / 1_000_000_000f
                        lastFrameTime = frameTimeNanos
                        
                        for (i in particles.indices.reversed()) {
                            val p = particles[i]
                            p.life -= deltaTime * 1.8f // Fast fade
                            
                            if (p.life <= 0f) {
                                particles.removeAt(i)
                                continue
                            }
                            
                            // Drag physics
                            val drag = 0.95f
                            p.vx *= drag
                            p.vy = (p.vy * drag) + (1200f * deltaTime) // Strong gravity
                            
                            p.x += p.vx * deltaTime
                            p.y += p.vy * deltaTime
                            p.rotation += p.rotationSpeed * deltaTime
                        }
                        frameTick++
                    }
                }
            }
            
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Read frameTick to trigger redraw
                @Suppress("UNUSED_VARIABLE")
                val tick = frameTick

                for (i in particles.indices) {
                    val p = particles[i]
                    val alpha = p.life.coerceIn(0f, 1f)
                    val halfSize = p.size / 2

                    // Draw logic per shape
                    withTransform({
                        translate(p.x, p.y)
                        rotate(p.rotation)
                        scale(p.life) // Shrink as they die
                    }) {
                        when (p.shape) {
                            ParticleShape.CIRCLE -> {
                                drawCircle(
                                    color = p.color,
                                    radius = halfSize,
                                    alpha = alpha
                                )
                            }
                            ParticleShape.SQUARE -> {
                                drawRect(
                                    color = p.color,
                                    topLeft = Offset(-halfSize, -halfSize),
                                    size = Size(p.size, p.size),
                                    alpha = alpha
                                )
                            }
                            ParticleShape.CROSS -> {
                                val thickness = p.size * 0.25f
                                // Vertical Bar
                                drawRect(
                                    color = p.color,
                                    topLeft = Offset(-thickness / 2, -halfSize),
                                    size = Size(thickness, p.size),
                                    alpha = alpha
                                )
                                // Horizontal Bar
                                drawRect(
                                    color = p.color,
                                    topLeft = Offset(-halfSize, -thickness / 2),
                                    size = Size(p.size, thickness),
                                    alpha = alpha
                                )
                            }
                            ParticleShape.ZIGZAG -> {
                                withTransform({
                                    scale(p.size)
                                }) {
                                    drawPath(
                                        path = lightningPath,
                                        color = p.color,
                                        alpha = alpha
                                    )
                                }
                            }
                            ParticleShape.CURVE -> {
                                withTransform({
                                    scale(p.size)
                                }) {
                                    drawPath(
                                        path = curvePath,
                                        color = p.color,
                                        alpha = alpha,
                                        style = Stroke(
                                            width = 0.2f,
                                            cap = StrokeCap.Round
                                        )
                                    )
                                }
                            }
                            ParticleShape.STRIP -> {
                                val stripWidth = p.size * 0.3f
                                val stripHeight = p.size * 1.2f
                                drawRect(
                                    color = p.color,
                                    topLeft = Offset(-stripWidth / 2, -stripHeight / 2),
                                    size = Size(stripWidth, stripHeight),
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

@Preview(showBackground = true)
@Composable
private fun SuccessAnimationDetailedPreview() {
    com.example.mypracticeapplication.presentation.theme.MyPracticeApplicationTheme {
        SuccessAnimationScreen(navController = rememberNavController())
    }
}


