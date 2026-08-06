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
fun SuccessAnimationContent(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var isPlaying by remember { mutableStateOf(false) }
    
    // Animation States
    val circleScale = remember { Animatable(0f) }
    val checkmarkProgress = remember { Animatable(0f) }

    // Optimization: Pre-calculate the checkmark path to avoid per-frame allocation in Canvas
    val checkmarkPath = remember(density) {
        Path().apply {
            with(density) {
                moveTo(32.dp.toPx(), 58.dp.toPx())
                lineTo(48.dp.toPx(), 74.dp.toPx()) // Tip
                lineTo(82.dp.toPx(), 38.dp.toPx()) // End
            }
        }
    }

    // Optimization: Reuse PathMeasure and a second Path instance to avoid per-frame allocations
    val pathMeasure = remember { PathMeasure() }
    val animatedCheckmarkPath = remember { Path() }
    
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(400.dp)
                .clickable { if(!isPlaying) isPlaying = true }
        ) {
            // Confetti Burst Layer (Behind and On Top possibility, but usually Top is fine)
            ConfettiBurst(
                isVisible = startConfetti,
                modifier = Modifier.fillMaxSize()
            )
            
            // Green Circle Background
            Canvas(
                modifier = Modifier
                    .size(110.dp) // Matched size
                    // Optimization: Use graphicsLayer to defer animation state reading to the draw phase
                    .graphicsLayer {
                        scaleX = circleScale.value
                        scaleY = circleScale.value
                    }
            ) {
                drawCircle(
                    color = ColorGreenCircle
                )
            }
            
            // Checkmark
            Canvas(
                modifier = Modifier
                    .size(110.dp)
                    // Optimization: Use graphicsLayer to defer animation state reading to the draw phase
                    .graphicsLayer {
                        val scale = circleScale.value.coerceAtLeast(0f)
                        scaleX = scale
                        scaleY = scale
                    }
            ) {
                // Optimization: Use remembered path objects to avoid per-frame allocations
                pathMeasure.setPath(checkmarkPath, false)
                val pathLength = pathMeasure.length
                
                animatedCheckmarkPath.reset()
                pathMeasure.getSegment(0f, pathLength * checkmarkProgress.value, animatedCheckmarkPath, true)
                
                drawPath(
                    path = animatedCheckmarkPath,
                    color = Color.White,
                    style = Stroke(
                        width = 8.dp.toPx(), // Thick stroke
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
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

private data class SuccessParticle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val size: Float,
    val shape: ParticleShape,
    val rotation: Float,
    val rotationSpeed: Float,
    var life: Float = 1f, // 1.0 to 0.0
    val startDelay: Float = 0f // Some particles might pop slightly later
)

@Composable
fun ConfettiBurst(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (isVisible) {
            val density = LocalDensity.current

            // Optimization: Pre-calculate normalized paths (0.0 to 1.0 or -0.5 to 0.5)
            // to avoid per-frame path allocation in custom shape drawers.
            val zigzagPath = remember {
                Path().apply {
                    moveTo(-0.5f, -0.5f)
                    lineTo(0f, 0f)
                    lineTo(-0.5f, 0.5f)
                    lineTo(0f, 0.5f)
                    lineTo(0.5f, 0f)
                    lineTo(0f, -0.5f)
                    close()
                }
            }
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

            var particles by remember { mutableStateOf<List<SuccessParticle>>(emptyList()) }
            var lastFrameTime by remember { mutableLongStateOf(0L) }
            
            LaunchedEffect(Unit) {
                with(density) {
                    val centerX = 200.dp.toPx() // Half of 400.dp box
                    val centerY = 200.dp.toPx()
                    
                    particles = List(60) {
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
                        
                        particles = particles.mapNotNull { p ->
                            if (p.life <= 0f) return@mapNotNull null
                            
                            val nextLife = p.life - deltaTime * 1.8f // Fast fade
                            
                            // Drag physics
                            val drag = 0.95f
                            val nextVx = p.vx * drag
                            val nextVy = (p.vy * drag) + (1200f * deltaTime) // Strong gravity
                            
                            val nextX = p.x + nextVx * deltaTime
                            val nextY = p.y + nextVy * deltaTime
                            val nextRot = p.rotation + p.rotationSpeed * deltaTime
                            
                            p.copy(
                                x = nextX,
                                y = nextY,
                                vx = nextVx,
                                vy = nextVy,
                                rotation = nextRot,
                                life = nextLife
                            )
                        }
                    }
                }
            }
            
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Optimization: Use indexed loop to avoid iterator allocation
                for (i in particles.indices) {
                    val p = particles[i]
                    // Draw logic per shape
                    withTransform({
                        translate(p.x, p.y)
                        rotate(p.rotation)
                        scale(p.life) // Shrink as they die
                    }) {
                        val alpha = p.life.coerceIn(0f, 1f)
                        val color = p.color.copy(alpha = alpha)
                        val halfSize = p.size / 2
                        
                        when(p.shape) {
                            ParticleShape.CIRCLE -> {
                                drawCircle(
                                    color = color,
                                    radius = halfSize
                                )
                            }
                            ParticleShape.SQUARE -> {
                                drawRect(
                                    color = color,
                                    topLeft = Offset(-halfSize, -halfSize),
                                    size = Size(p.size, p.size)
                                )
                            }
                            ParticleShape.CROSS -> {
                                drawCross(color, p.size)
                            }
                            ParticleShape.ZIGZAG -> {
                                drawZigzag(color, p.size, zigzagPath, lightningPath)
                            }
                            ParticleShape.CURVE -> {
                                drawCurve(color, p.size, curvePath)
                            }
                            ParticleShape.STRIP -> {
                                drawStrip(color, p.size)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// CUSTOM SHAPE DRAWERS
// -----------------------------------------------------------------------------

fun DrawScope.drawCross(color: Color, size: Float) {
    val thickness = size * 0.25f
    val halfSize = size / 2
    
    // Vertical Bar
    drawRect(
        color = color,
        topLeft = Offset(-thickness/2, -halfSize),
        size = Size(thickness, size)
    )
    // Horizontal Bar
    drawRect(
        color = color,
        topLeft = Offset(-halfSize, -thickness/2),
        size = Size(size, thickness)
    )
}

fun DrawScope.drawZigzag(color: Color, size: Float, zigzagPath: Path, lightningPath: Path) {
    // Optimization: Reuse pre-calculated normalized paths and scale them here
    // to avoid per-frame allocations.
    withTransform({
        scale(size, size, Offset.Zero)
    }) {
        // We use lightningPath as the main filled shape
        drawPath(path = lightningPath, color = color)
    }
}

fun DrawScope.drawCurve(color: Color, size: Float, curvePath: Path) {
    // Optimization: Reuse pre-calculated normalized path and scale it here
    withTransform({
        scale(size, size, Offset.Zero)
    }) {
        drawPath(
            path = curvePath,
            color = color,
            style = Stroke(
                width = 0.2f, // width relative to normalized size 1.0
                cap = StrokeCap.Round
            )
        )
    }
}

fun DrawScope.drawStrip(color: Color, size: Float) {
    // a long thin rectangle (Strip)
    val width = size * 0.3f
    val height = size * 1.2f
    drawRect(
        color = color,
        topLeft = Offset(-width/2, -height/2),
        size = Size(width, height)
    )
}

@Preview(showBackground = true)
@Composable
private fun SuccessAnimationDetailedPreview() {
    MaterialTheme {
        SuccessAnimationScreen(navController = rememberNavController())
    }
}


