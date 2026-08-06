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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

// Firework colors - bright and vibrant
private val FireworkColors = listOf(
    Color(0xFFFF1744), // Red
    Color(0xFFFFD600), // Yellow
    Color(0xFF00E676), // Green
    Color(0xFF2979FF), // Blue
    Color(0xFFFF4081), // Pink
    Color(0xFF00E5FF), // Cyan
    Color(0xFFFFAB00), // Amber
    Color(0xFFD500F9), // Purple
    Color(0xFFFFFFFF), // White
    Color(0xFF76FF03), // Lime
)

// Particle for center burst confetti
private data class BurstParticle(
    val id: Int,
    val angle: Float, // Direction in radians
    val speed: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float,
    val decay: Float // How quickly it slows down
)

// Firework data
private data class Firework(
    val id: Int,
    val startX: Float,
    val targetY: Float,
    val color: Color,
    val explosionDelay: Float, // When it explodes (0-1)
    val particleCount: Int,
    val trailLength: Int,
    val sparks: List<FireworkSpark>
)

// Firework spark particle
private data class FireworkSpark(
    val angle: Float,
    val speed: Float,
    val color: Color,
    val size: Float,
    val sparkle: Boolean
)

@Composable
fun FireworksScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(false) }
    var burstParticles by remember { mutableStateOf(emptyList<BurstParticle>()) }
    var fireworks by remember { mutableStateOf(emptyList<Firework>()) }
    val animationProgress = remember { Animatable(0f) }
    
    // Generate particles and fireworks when animation starts
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            burstParticles = generateBurstParticles(100)
            fireworks = generateFireworks(5)
            animationProgress.snapTo(0f)
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 5000,
                    easing = LinearEasing
                )
            )
            delay(500)
            isPlaying = false
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        FireworksHeader(onNavigateBack = onNavigateBack)
        
        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // Night sky background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0D0D1A),
                                Color(0xFF1A1A2E),
                                Color(0xFF0F0F23)
                            )
                        )
                    )
            )
            
            // Stars background
            Canvas(modifier = Modifier.fillMaxSize()) {
                val starCount = 50
                val random = Random(42) // Fixed seed for consistent stars
                repeat(starCount) {
                    val x = random.nextFloat() * size.width
                    val y = random.nextFloat() * size.height * 0.7f
                    val starSize = random.nextFloat() * 2 + 1
                    drawCircle(
                        color = Color.White.copy(alpha = random.nextFloat() * 0.5f + 0.3f),
                        radius = starSize,
                        center = Offset(x, y)
                    )
                }
            }
            
            // Fireworks and Burst Animation
            if (isPlaying) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val progress = animationProgress.value
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    
                    // ═══════════════════════════════════════════════════════════
                    // CENTER BURST CONFETTI
                    // ═══════════════════════════════════════════════════════════
                    if (progress < 0.7f) {
                        val burstProgress = (progress / 0.7f).coerceIn(0f, 1f)
                        
                        burstParticles.forEach { particle ->
                            val distance = particle.speed * burstProgress * 400 * 
                                (1 - burstProgress * particle.decay * 0.5f)
                            
                            val gravity = burstProgress.pow(2) * 200
                            
                            val x = centerX + cos(particle.angle) * distance
                            val y = centerY + sin(particle.angle) * distance + gravity
                            
                            val alpha = (1f - burstProgress * 0.8f).coerceIn(0f, 1f)
                            val currentSize = particle.size * (1 - burstProgress * 0.3f)
                            
                            // Draw particle
                            drawCircle(
                                color = particle.color.copy(alpha = alpha),
                                radius = currentSize,
                                center = Offset(x, y)
                            )
                            
                            // Sparkle trail
                            if (burstProgress < 0.5f) {
                                val trailX = centerX + cos(particle.angle) * distance * 0.8f
                                val trailY = centerY + sin(particle.angle) * distance * 0.8f + gravity * 0.6f
                                drawCircle(
                                    color = Color.White.copy(alpha = alpha * 0.5f),
                                    radius = currentSize * 0.5f,
                                    center = Offset(trailX, trailY)
                                )
                            }
                        }
                    }
                    
                    // ═══════════════════════════════════════════════════════════
                    // FIREWORKS
                    // ═══════════════════════════════════════════════════════════
                    fireworks.forEach { firework ->
                        val fireworkProgress = progress
                        
                        // Launch phase
                        if (fireworkProgress < firework.explosionDelay) {
                            val launchProgress = fireworkProgress / firework.explosionDelay
                            val startY = size.height + 50
                            val currentY = startY - (startY - firework.targetY) * launchProgress
                            
                            // Draw rocket trail
                            val trailPath = Path().apply {
                                moveTo(firework.startX, currentY)
                                for (i in 1..firework.trailLength) {
                                    val trailY = currentY + i * 8f
                                    val wobble = sin(i * 0.5f + fireworkProgress * 20) * 3
                                    lineTo(firework.startX + wobble, trailY)
                                }
                            }
                            drawPath(
                                path = trailPath,
                                color = firework.color.copy(alpha = 0.8f),
                                style = Stroke(width = 3f)
                            )
                            
                            // Draw rocket head
                            drawCircle(
                                color = Color.White,
                                radius = 4f,
                                center = Offset(firework.startX, currentY)
                            )
                        }
                        // Explosion phase
                        else {
                            val explosionProgress = ((fireworkProgress - firework.explosionDelay) / 
                                (1f - firework.explosionDelay)).coerceIn(0f, 1f)
                            
                            // Using pre-calculated sparks to avoid per-frame allocation
                            firework.sparks.forEach { spark ->
                                val distance = spark.speed * explosionProgress * 150
                                val gravity = explosionProgress.pow(2) * 100
                                
                                val sparkX = firework.startX + cos(spark.angle) * distance
                                val sparkY = firework.targetY + sin(spark.angle) * distance + gravity
                                
                                val alpha = (1f - explosionProgress).coerceIn(0f, 1f)
                                val sparkleAlpha = if (spark.sparkle) {
                                    alpha * (0.5f + sin(explosionProgress * 20) * 0.5f).coerceIn(0f, 1f)
                                } else alpha
                                
                                // Draw spark
                                drawCircle(
                                    color = spark.color.copy(alpha = sparkleAlpha),
                                    radius = spark.size * (1 - explosionProgress * 0.5f),
                                    center = Offset(sparkX, sparkY)
                                )
                                
                                // Trailing glow
                                if (explosionProgress < 0.6f) {
                                    drawCircle(
                                        color = spark.color.copy(alpha = sparkleAlpha * 0.3f),
                                        radius = spark.size * 2,
                                        center = Offset(sparkX, sparkY)
                                    )
                                }
                            }
                            
                            // Central flash at explosion start
                            if (explosionProgress < 0.15f) {
                                val flashAlpha = (1f - explosionProgress / 0.15f)
                                drawCircle(
                                    color = Color.White.copy(alpha = flashAlpha * 0.8f),
                                    radius = 30f * (1 + explosionProgress * 2),
                                    center = Offset(firework.startX, firework.targetY)
                                )
                            }
                        }
                    }
                }
            }
            
            // Center content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎆",
                    style = MaterialTheme.typography.displayLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Fireworks Show!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Center burst confetti + fireworks",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { isPlaying = true },
                    enabled = !isPlaying,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF4081),
                        disabledContainerColor = Color(0xFFFF4081).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text(
                        text = if (isPlaying) "🎆 Show in progress..." else "🎆 Launch Fireworks!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun FireworksHeader(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF4081),
                        Color(0xFFFF9100)
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
                text = "🎆 Fireworks Animation",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Center burst + fireworks effect",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

private fun generateBurstParticles(count: Int): List<BurstParticle> {
    return List(count) { index ->
        BurstParticle(
            id = index,
            angle = Random.nextFloat() * 2 * Math.PI.toFloat(),
            speed = Random.nextFloat() * 0.8f + 0.4f,
            color = FireworkColors.random(),
            size = Random.nextFloat() * 8 + 4,
            rotationSpeed = Random.nextFloat() * 2 - 1,
            decay = Random.nextFloat() * 0.5f + 0.3f
        )
    }
}

private fun generateFireworks(count: Int): List<Firework> {
    return List(count) { index ->
        val id = index
        val color = FireworkColors.random()
        val particleCount = Random.nextInt(30, 60)

        // Pre-calculate sparks to avoid per-frame allocation
        val sparks = List(particleCount) { i ->
            FireworkSpark(
                angle = (i.toFloat() / particleCount) * 2 * Math.PI.toFloat() +
                        Random(id * 100 + i).nextFloat() * 0.3f,
                speed = 0.5f + Random(id * 100 + i + 50).nextFloat() * 0.8f,
                color = if (Random(id * 100 + i + 100).nextFloat() > 0.7f)
                    Color.White else color,
                size = 2f + Random(id * 100 + i + 150).nextFloat() * 4f,
                sparkle = Random(id * 100 + i + 200).nextFloat() > 0.5f
            )
        }

        Firework(
            id = id,
            startX = Random.nextFloat() * 800 + 100,
            targetY = Random.nextFloat() * 300 + 150,
            color = color,
            explosionDelay = 0.15f + index * 0.15f, // Stagger explosions
            particleCount = particleCount,
            trailLength = Random.nextInt(8, 15),
            sparks = sparks
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FireworksScreenPreview() {
    FireworksScreen()
}


