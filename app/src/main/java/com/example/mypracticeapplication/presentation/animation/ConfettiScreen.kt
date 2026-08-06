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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// Vibrant confetti colors
private val ConfettiColors = listOf(
    Color(0xFFFF6B6B), // Red
    Color(0xFF4ECDC4), // Teal
    Color(0xFFFFE66D), // Yellow
    Color(0xFF95E1D3), // Mint
    Color(0xFFF38181), // Coral
    Color(0xFFAA96DA), // Purple
    Color(0xFF7FDBFF), // Sky Blue
    Color(0xFFFF9F43), // Orange
    Color(0xFF2ECC71), // Green
    Color(0xFFE056FD), // Pink
)

// Confetti particle data class
private data class ConfettiParticle(
    val id: Int,
    val startX: Float,
    val startY: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float,
    val horizontalDrift: Float,
    val fallSpeed: Float,
    val shape: ConfettiShape,
    val oscillationAmplitude: Float,
    val oscillationFrequency: Float
)

private enum class ConfettiShape {
    RECTANGLE, CIRCLE, TRIANGLE, STAR
}

@Composable
fun ConfettiScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(false) }
    var particles by remember { mutableStateOf(emptyList<ConfettiParticle>()) }
    val animationProgress = remember { Animatable(0f) }

    // Pre-create normalized paths to avoid allocations in the draw loop
    val trianglePath = remember {
        Path().apply {
            moveTo(0f, -0.5f)
            lineTo(-0.5f, 0.5f)
            lineTo(0.5f, 0.5f)
            close()
        }
    }

    val starPath = remember {
        createStarPath(
            centerX = 0f,
            centerY = 0f,
            outerRadius = 0.5f,
            innerRadius = 0.25f
        )
    }

    // Generate particles when animation starts
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            particles = generateConfettiParticles(150)
            animationProgress.snapTo(0f)
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 4000,
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
        ConfettiHeader(onNavigateBack = onNavigateBack)
        
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
                                Color(0xFF1A1A2E),
                                Color(0xFF16213E),
                                Color(0xFF0F3460)
                            )
                        )
                    )
            )
            
            // Confetti Canvas
            if (isPlaying) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val progress = animationProgress.value
                    val canvasHeight = size.height

                    // Use indexed loop to avoid iterator allocation per frame
                    for (i in particles.indices) {
                        val particle = particles[i]

                        // Calculate current position
                        val time = progress * 4f // Scale time
                        val gravity = 0.5f

                        val currentX = particle.startX +
                                particle.horizontalDrift * time * 100 +
                                sin(time * particle.oscillationFrequency) * particle.oscillationAmplitude

                        val currentY = particle.startY +
                                particle.fallSpeed * time * canvasHeight * 0.3f +
                                gravity * time * time * 200

                        // Skip if out of bounds
                        if (currentY > canvasHeight + 50) continue

                        val rotation = time * particle.rotationSpeed * 360
                        val alpha = (1f - (progress * 0.5f)).coerceIn(0f, 1f)

                        // ⚡ Optimization: Use withTransform to handle translation, rotation, and scaling
                        // This allows us to use cached, normalized paths instead of creating new ones every frame.
                        withTransform({
                            translate(currentX, currentY)
                            rotate(rotation, Offset.Zero)
                            scale(particle.size, particle.size, Offset.Zero)
                        }) {
                            when (particle.shape) {
                                ConfettiShape.RECTANGLE -> {
                                    drawRect(
                                        color = particle.color,
                                        alpha = alpha,
                                        topLeft = Offset(-0.5f, -0.25f),
                                        size = Size(1f, 0.5f)
                                    )
                                }
                                ConfettiShape.CIRCLE -> {
                                    drawCircle(
                                        color = particle.color,
                                        alpha = alpha,
                                        radius = 0.5f,
                                        center = Offset.Zero
                                    )
                                }
                                ConfettiShape.TRIANGLE -> {
                                    drawPath(
                                        path = trianglePath,
                                        color = particle.color,
                                        alpha = alpha
                                    )
                                }
                                ConfettiShape.STAR -> {
                                    drawPath(
                                        path = starPath,
                                        color = particle.color,
                                        alpha = alpha
                                    )
                                }
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
                    text = "🎊",
                    style = MaterialTheme.typography.displayLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Confetti Celebration!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Tap the button to launch confetti",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { isPlaying = true },
                    enabled = !isPlaying,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B),
                        disabledContainerColor = Color(0xFFFF6B6B).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text(
                        text = if (isPlaying) "🎉 Celebrating..." else "🎉 Launch Confetti!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfettiHeader(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF6B6B),
                        Color(0xFFAA96DA)
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
                text = "🎊 Confetti Animation",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Particle-based celebration effect",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

private fun generateConfettiParticles(count: Int): List<ConfettiParticle> {
    return List(count) { index ->
        ConfettiParticle(
            id = index,
            startX = Random.nextFloat() * 1000 + 100, // Spread across screen
            startY = Random.nextFloat() * -200 - 50, // Start above screen
            color = ConfettiColors.random(),
            size = Random.nextFloat() * 15 + 8, // 8-23 size
            rotationSpeed = Random.nextFloat() * 2 - 1, // -1 to 1
            horizontalDrift = Random.nextFloat() * 2 - 1, // -1 to 1
            fallSpeed = Random.nextFloat() * 0.5f + 0.5f, // 0.5 to 1.0
            shape = ConfettiShape.entries.random(),
            oscillationAmplitude = Random.nextFloat() * 30 + 10,
            oscillationFrequency = Random.nextFloat() * 5 + 2
        )
    }
}

private fun createStarPath(
    centerX: Float,
    centerY: Float,
    outerRadius: Float,
    innerRadius: Float,
    points: Int = 5
): Path {
    val path = Path()
    val angleStep = Math.PI / points
    
    for (i in 0 until points * 2) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = i * angleStep - Math.PI / 2
        val x = centerX + (radius * cos(angle)).toFloat()
        val y = centerY + (radius * sin(angle)).toFloat()
        
        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    return path
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ConfettiScreenPreview() {
    ConfettiScreen()
}


