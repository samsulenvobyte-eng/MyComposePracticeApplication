package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private val GoldColor = Color(0xFFFFD700)
private val LightGoldColor = Color(0xFFFFE57C)
private val DetailColor = Color(0xFFC5A000)

// ═══════════════════════════════════════════════════════════════════════════════
// STATE MANAGEMENT
// ═══════════════════════════════════════════════════════════════════════════════

@Stable
class CoinHarvestState {
    // We use a plain ArrayList to avoid Snapshot overhead,
    // as redraws are manually triggered by the timeNanos state update.
    private val _particles = ArrayList<CoinParticle>()
    val particles: List<CoinParticle> get() = _particles

    var targetPosition by mutableStateOf(Offset.Zero)

    // FRAME TIME STATE: Forces Canvas redraw on every frame update.
    // Using mutableLongStateOf to avoid boxing.
    var timeNanos by mutableLongStateOf(0L)
        private set

    // Animation Loop Trigger
    private var lastFrameTime = 0L
    
    fun harvest(startPosition: Offset, amount: Int = 10) {
        if (targetPosition == Offset.Zero) return // No target yet

        repeat(amount) {
            _particles.add(createParticle(startPosition, targetPosition))
        }
    }

    private fun createParticle(start: Offset, target: Offset): CoinParticle {
        // Control point logic (Arc)
        val midX = (start.x + target.x) / 2
        // If starting from left, arc up-right. If right, arc up-left.
        // Generally usually arc UP.
        // We'll calculate a "Height" based on distance.
        val distance = (target - start).getDistance()
        val arcHeight = distance * (0.5f + Random.nextFloat() * 0.5f) // 50-100% of distance
        
        val controlX = midX + (Random.nextFloat() - 0.5f) * (distance * 0.5f)
        val controlY = minOf(start.y, target.y) - arcHeight
        
        return CoinParticle(
            id = Random.nextInt(),
            start = start,
            control = Offset(controlX, controlY),
            end = target,
            rotationSpeed = Random.nextFloat() * 15f + 5f,
            scaleMax = Random.nextFloat() * 0.4f + 0.8f,
            startTime = System.nanoTime(), // Will be set relative to frame time loop
            durationMs = (800 + Random.nextInt(400)).toLong() // 800-1200ms
        )
    }

    fun updateParticles(frameTimeNanos: Long) {
        // Update Time State to trigger recomposition
        timeNanos = frameTimeNanos

        // Remove finished particles using a manual backward loop to avoid iterator allocations
        for (i in _particles.size - 1 downTo 0) {
            val particle = _particles[i]
            val elapsed = (frameTimeNanos - particle.startTime) / 1_000_000f // ms
            if (elapsed >= particle.durationMs) {
                _particles.removeAt(i)
            }
        }
    }

    // Helper to set start time for new particles correctly in the loop
    fun spawn(start: Offset, amount: Int, currentTimeNanos: Long) {
        if (targetPosition == Offset.Zero) return

        repeat(amount) {
            _particles.add(createParticle(start, targetPosition).copy(startTime = currentTimeNanos))
        }
    }
}

@Composable
fun rememberCoinHarvestState(): CoinHarvestState {
    return remember { CoinHarvestState() }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DATA CLASS
// ═══════════════════════════════════════════════════════════════════════════════

data class CoinParticle(
    val id: Int,
    val start: Offset,
    val control: Offset,
    val end: Offset,
    val rotationSpeed: Float,
    val scaleMax: Float,
    val startTime: Long = 0L,
    val durationMs: Long
)

// ═══════════════════════════════════════════════════════════════════════════════
// HOST COMPOSABLE
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun CoinHarvestHost(
    state: CoinHarvestState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        // 1. The Screen Content
        content()

        // 2. The Animation Overlay
        CoinHarvestOverlay(state = state)
    }
}

@Composable
private fun CoinHarvestOverlay(
    state: CoinHarvestState,
    modifier: Modifier = Modifier
) {
    // Animation Loop
    LaunchedEffect(state) {
        while (true) {
            withFrameNanos { timeNanos ->
                state.updateParticles(timeNanos)
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        // Read State Time to ensure we redraw every frame!
        val currentTime = state.timeNanos
        val particles = state.particles

        // Manual indexed loop to avoid iterator allocations
        for (i in particles.indices) {
            val particle = particles[i]

            // Calculate progress
            // Note: In a real app we'd pass the frame time from LaunchedEffect to Canvas 
            // via a state to ensure sync, but System.nanoTime() is often "good enough" for loose particles.
            // Better: use the withFrameNanos time if possible. 
            // For simplicity here, we re-calculate.
            
            val elapsedMs = (currentTime - particle.startTime) / 1_000_000f
            val progress = (elapsedMs / particle.durationMs).coerceIn(0f, 1f)
            
            if (progress < 1f) {
                val currentPos = calculateBezierPoint(progress, particle.start, particle.control, particle.end)
                
                // Spin
                val rotation = progress * 360f * (particle.rotationSpeed / 10f)
                
                // Scale Logic: Pop In -> Stay -> Shrink Out
                val scale = when {
                    progress < 0.2f -> (progress / 0.2f) * particle.scaleMax 
                    progress > 0.8f -> ((1f - progress) / 0.2f) * particle.scaleMax 
                    else -> particle.scaleMax
                }
                
                rotate(rotation, pivot = currentPos) {
                    drawCoin(currentPos, scale)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// UTILS
// ═══════════════════════════════════════════════════════════════════════════════

private fun calculateBezierPoint(t: Float, p0: Offset, p1: Offset, p2: Offset): Offset {
    val u = 1 - t
    val tt = t * t
    val uu = u * u
    
    val x = uu * p0.x + 2 * u * t * p1.x + tt * p2.x
    val y = uu * p0.y + 2 * u * t * p1.y + tt * p2.y
    
    return Offset(x, y)
}

private fun DrawScope.drawCoin(center: Offset, scale: Float) {
    val radius = 25f * scale
    if (radius <= 0) return

    // Outer Gold Ring
    drawCircle(
        color = GoldColor,
        radius = radius,
        center = center
    )

    // Inner Light Gold (Shine)
    drawCircle(
        color = LightGoldColor,
        radius = radius * 0.8f,
        center = center
    )

    // Detail
    drawRect(
        color = DetailColor,
        topLeft = Offset(center.x - radius * 0.2f, center.y - radius * 0.5f),
        size = Size(radius * 0.4f, radius * 1f),
        alpha = 0.5f
    )
}


