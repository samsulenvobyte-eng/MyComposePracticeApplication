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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.presentation.theme.MyPracticeApplicationTheme
import kotlin.random.Random

// ═══════════════════════════════════════════════════════════════════════════════
// PARTICLE DATA CLASS
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Represents a single confetti particle with physics properties.
 * All values are in normalized units or pixels per second.
 * Optimized: Mutable class to allow in-place updates and eliminate allocation overhead.
 */
private class Particle(
    var x: Float,           // Current X position
    var y: Float,           // Current Y position
    var vx: Float,          // Velocity X (pixels per second)
    var vy: Float,          // Velocity Y (pixels per second, negative = upward)
    val width: Float,       // Particle width
    val height: Float,      // Particle height
    val color: Color,       // Particle color
    var rotation: Float,    // Current rotation angle (degrees)
    val rotationSpeed: Float, // Rotation speed (degrees per second)
    val alpha: Float,       // Alpha for depth simulation (0.5 - 1.0)
    val scale: Float,       // Scale factor for depth simulation (0.6 - 1.0)
    var isAlive: Boolean = true // Whether particle is still visible
)

// ═══════════════════════════════════════════════════════════════════════════════
// PHYSICS CONSTANTS
// ═══════════════════════════════════════════════════════════════════════════════

private object PhysicsConfig {
    const val GRAVITY = 980f          // Pixels per second squared (simulates ~1g)
    const val AIR_DRAG = 0.98f        // Drag coefficient (applied per frame)
    const val PARTICLE_COUNT = 150    // Number of particles to emit
    
    // Initial velocity ranges
    const val MIN_VY = -1200f         // Minimum upward velocity
    const val MAX_VY = -800f          // Maximum upward velocity
    const val MIN_VX = -400f          // Minimum horizontal velocity
    const val MAX_VX = 400f           // Maximum horizontal velocity
    
    // Particle size ranges
    const val MIN_WIDTH = 8f
    const val MAX_WIDTH = 16f
    const val MIN_HEIGHT = 6f
    const val MAX_HEIGHT = 20f
    
    // Rotation speed range (degrees per second)
    const val MIN_ROTATION_SPEED = -720f
    const val MAX_ROTATION_SPEED = 720f
}

// Vibrant color palette
private val ConfettiPalette = listOf(
    Color(0xFFE53935), // Red
    Color(0xFFFFD700), // Gold
    Color(0xFF00897B), // Teal
    Color(0xFF1E88E5), // Blue
    Color(0xFFFF6F00), // Amber
    Color(0xFF8E24AA), // Purple
)

// ═══════════════════════════════════════════════════════════════════════════════
// PARTICLE GENERATOR
// ═══════════════════════════════════════════════════════════════════════════════

private fun generateParticles(emissionX: Float, emissionY: Float, list: ArrayList<Particle>) {
    list.clear()
    list.ensureCapacity(PhysicsConfig.PARTICLE_COUNT)
    for (i in 0 until PhysicsConfig.PARTICLE_COUNT) {
        list.add(
            Particle(
                x = emissionX,
                y = emissionY,
                vx = Random.nextFloat() * (PhysicsConfig.MAX_VX - PhysicsConfig.MIN_VX) + PhysicsConfig.MIN_VX,
                vy = Random.nextFloat() * (PhysicsConfig.MAX_VY - PhysicsConfig.MIN_VY) + PhysicsConfig.MIN_VY,
                width = Random.nextFloat() * (PhysicsConfig.MAX_WIDTH - PhysicsConfig.MIN_WIDTH) + PhysicsConfig.MIN_WIDTH,
                height = Random.nextFloat() * (PhysicsConfig.MAX_HEIGHT - PhysicsConfig.MIN_HEIGHT) + PhysicsConfig.MIN_HEIGHT,
                color = ConfettiPalette.random(),
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * (PhysicsConfig.MAX_ROTATION_SPEED - PhysicsConfig.MIN_ROTATION_SPEED) + PhysicsConfig.MIN_ROTATION_SPEED,
                alpha = Random.nextFloat() * 0.5f + 0.5f, // 0.5 - 1.0
                scale = Random.nextFloat() * 0.4f + 0.6f  // 0.6 - 1.0
            )
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// REUSABLE CONFETTI EXPLOSION COMPOSABLE
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * A high-performance confetti explosion animation using Canvas.
 * 
 * @param modifier Modifier for the composable
 * @param isVisible When true, triggers the explosion animation
 * @param onAnimationEnd Optional callback when animation completes
 */
@Composable
fun ConfettiExplosion(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onAnimationEnd: (() -> Unit)? = null
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val canvasWidthPx = with(density) { maxWidth.toPx() }
        val canvasHeightPx = with(density) { maxHeight.toPx() }
        
        // Particle state: Optimized with ArrayList backing pool and frameTick to trigger recomposition
        val particles = remember { ArrayList<Particle>(PhysicsConfig.PARTICLE_COUNT) }
        var frameTick by remember { mutableLongStateOf(0L) }
        var lastFrameTime by remember { mutableLongStateOf(0L) }
        var isAnimating by remember { mutableStateOf(false) }
        
        // Trigger animation when isVisible becomes true
        LaunchedEffect(isVisible) {
            if (isVisible && !isAnimating) {
                // Initialize particles with known dimensions
                val emissionX = canvasWidthPx / 2
                val emissionY = canvasHeightPx // Bottom center
                generateParticles(emissionX, emissionY, particles)
                lastFrameTime = 0L
                frameTick = 0L
                isAnimating = true
            }
        }
        
        // Animation loop using withFrameNanos for optimal performance
        LaunchedEffect(isAnimating) {
            if (!isAnimating) return@LaunchedEffect
            
            while (isAnimating) {
                withFrameNanos { frameTimeNanos ->
                    if (lastFrameTime == 0L) {
                        lastFrameTime = frameTimeNanos
                        return@withFrameNanos
                    }
                    
                    // Calculate delta time in seconds
                    val deltaTime = (frameTimeNanos - lastFrameTime) / 1_000_000_000f
                    lastFrameTime = frameTimeNanos
                    
                    // Update particles in-place with physics
                    var anyAlive = false
                    val size = particles.size
                    for (i in 0 until size) {
                        val particle = particles[i]
                        if (!particle.isAlive) continue
                        
                        // Apply gravity
                        val newVy = particle.vy + PhysicsConfig.GRAVITY * deltaTime
                        
                        // Apply air drag
                        val draggedVx = particle.vx * PhysicsConfig.AIR_DRAG
                        val draggedVy = newVy * PhysicsConfig.AIR_DRAG
                        
                        // Update position
                        val newX = particle.x + draggedVx * deltaTime
                        val newY = particle.y + draggedVy * deltaTime
                        
                        // Update rotation
                        val newRotation = particle.rotation + particle.rotationSpeed * deltaTime
                        
                        // Check if particle is still visible (below screen + buffer)
                        val stillAlive = newY <= canvasHeightPx + 100f
                        if (stillAlive) anyAlive = true
                        
                        // Update in place (No GC allocation pressure)
                        particle.x = newX
                        particle.y = newY
                        particle.vx = draggedVx
                        particle.vy = draggedVy
                        particle.rotation = newRotation
                        particle.isAlive = stillAlive
                    }
                    
                    // Update the state tick to trigger redrawing
                    frameTick++

                    // End animation when all particles are dead
                    if (!anyAlive && particles.isNotEmpty()) {
                        isAnimating = false
                        particles.clear()
                        onAnimationEnd?.invoke()
                    }
                }
            }
        }
        
        // Canvas for high-performance rendering
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Read frameTick to declare state dependency for recomposition/redrawing
            val tick = frameTick

            // Draw all particles using manual indexed loop to avoid iterator allocation
            val size = particles.size
            for (i in 0 until size) {
                val particle = particles[i]
                if (!particle.isAlive) continue
                
                val scaledWidth = particle.width * particle.scale
                val scaledHeight = particle.height * particle.scale
                
                rotate(
                    degrees = particle.rotation,
                    pivot = Offset(particle.x, particle.y)
                ) {
                    drawRect(
                        color = particle.color,
                        topLeft = Offset(
                            particle.x - scaledWidth / 2,
                            particle.y - scaledHeight / 2
                        ),
                        size = Size(scaledWidth, scaledHeight),
                        alpha = particle.alpha // Optimized: Use drawRect's native alpha parameter to avoid Color.copy() allocation
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SCREEN COMPOSABLE
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun ConfettiBurstScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    var startExplosion by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        ConfettiBurstHeader(onNavigateBack = onNavigateBack)
        
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
            
            // Confetti Explosion Overlay
            ConfettiExplosion(
                modifier = Modifier.fillMaxSize(),
                isVisible = startExplosion,
                onAnimationEnd = { startExplosion = false }
            )
            
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
                    text = "Confetti Burst",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "High-performance fountain effect",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "• Canvas + withFrameNanos\n• Physics: Gravity + Drag\n• Tumbling rotation\n• Depth simulation",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { startExplosion = true },
                    enabled = !startExplosion,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935),
                        disabledContainerColor = Color(0xFFE53935).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text(
                        text = if (startExplosion) "🎊 Bursting..." else "🎊 Trigger Burst!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfettiBurstHeader(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFE53935),
                        Color(0xFFFFD700)
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
                text = "🎊 Confetti Burst",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Fountain effect with physics simulation",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ConfettiBurstScreenPreview() {
    MyPracticeApplicationTheme {
        ConfettiBurstScreen()
    }
}
