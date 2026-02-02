package com.example.mypracticeapplication.ui.screens.animation

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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
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

import com.example.mypracticeapplication.ui.components.LottieConfettiView

// ═══════════════════════════════════════════════════════════════════════════════
// CONSTANTS & COLORS
// ═══════════════════════════════════════════════════════════════════════════════

private val LottieBlueColors = listOf(
   // Color(0xFFFF4000), // Primary Blue
    Color(0xFF3080EB), // Lighter Blue
    Color(0xFFFFE120), // Darker Blue
    Color(0xFFFB8500),
)

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
    particleCount: Int,
    onParticleCountChange: (Float) -> Unit,
    sizeMultiplier: Float,
    onSizeChange: (Float) -> Unit,
    is3DEnabled: Boolean,
    on3DToggle: (Boolean) -> Unit,
    onFire: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF2C2C2C).copy(alpha = 0.9f), RoundedCornerShape(16.dp))
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

                // Particle Count Slider
                Text("Particles: $particleCount", color = Color.White, style = MaterialTheme.typography.bodySmall)
                androidx.compose.material3.Slider(
                    value = particleCount.toFloat(),
                    onValueChange = onParticleCountChange,
                    valueRange = 10f..300f,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = Color(0xFFFF4000),
                        activeTrackColor = Color(0xFFFF4000)
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
                Text("Speed: ${String.format("%.1fx", speed)}", color = Color.White, style = MaterialTheme.typography.bodySmall)
                androidx.compose.material3.Slider(
                    value = speed,
                    onValueChange = onSpeedChange,
                    valueRange = 0.5f..3.0f,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = Color(0xFF7780FD),
                        activeTrackColor = Color(0xFF7780FD)
                    )
                )

                // Size Slider
                Text("Size: ${String.format("%.1fx", sizeMultiplier)}", color = Color.White, style = MaterialTheme.typography.bodySmall)
                androidx.compose.material3.Slider(
                    value = sizeMultiplier,
                    onValueChange = onSizeChange,
                    valueRange = 0.5f..3.0f,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = Color(0xFF00F03C),
                        activeTrackColor = Color(0xFF00F03C)
                    )
                )

                // 3D Effect Toggle
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("3D Depth", color = Color.White, style = MaterialTheme.typography.bodySmall)
                    androidx.compose.material3.Switch(
                        checked = is3DEnabled,
                        onCheckedChange = on3DToggle,
                        colors = androidx.compose.material3.SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF42AAF8),
                            checkedTrackColor = Color(0xFF42AAF8).copy(alpha = 0.5f)
                        )
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
fun LottieConfettiScreen(
    onNavigateBack: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(false) }
    
    // Cannon Configuration State
    var spread by remember { mutableStateOf(90f) }
    var positionY by remember { mutableStateOf(1.0f) } 
    var forceMultiplier by remember { mutableStateOf(0.8f) } // Default power
    var speed by remember { mutableStateOf(1.0f) }
    var is3DEnabled by remember { mutableStateOf(true) }
    var particleCount by remember { mutableStateOf(50) }
    var sizeMultiplier by remember { mutableStateOf(1.0f) }
    
    // Trigger State
    var triggerCount by remember { mutableStateOf(0) }
    
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
        modifier = Modifier
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
                    .background(Color(0xFFFFFFFF))
            )
            
            // Confetti Animation
            LottieConfettiView(
                triggerKey = triggerCount,
                colors = LottieBlueColors,
                particleCount = particleCount,
                spreadAngle = spread,
                startPositionY = positionY,
                forceMultiplier = forceMultiplier,
                speedMultiplier = speed,
                is3DEnabled = is3DEnabled,
                sizeMultiplier = sizeMultiplier
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
                    particleCount = particleCount,
                    onParticleCountChange = { particleCount = it.toInt() },
                    sizeMultiplier = sizeMultiplier,
                    onSizeChange = { sizeMultiplier = it },
                    is3DEnabled = is3DEnabled,
                    on3DToggle = { is3DEnabled = it },
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
private fun LottieConfettiHeader(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
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
