package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val PrimaryColor = Color(0xFFFF6B6B)
private val SecondaryColor = Color(0xFF4ECDC4)
private val AccentColor = Color(0xFFFFE66D)
private val PurpleColor = Color(0xFF9B59B6)
private val BlueColor = Color(0xFF3498DB)

@Composable
fun AnimationTypeScreen(
    onNavigateBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(PrimaryColor, SecondaryColor)))
                .padding(horizontal = 4.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
            }
            Column {
                Text("✨ Animation Types", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Different animation effects", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Fade Animation
            FadeAnimationDemo()
            
            // 2. Scale Animation
            ScaleAnimationDemo()
            
            // 3. Slide Animation
            SlideAnimationDemo()
            
            // 4. Rotation Animation
            RotationAnimationDemo()
            
            // 5. Color Animation
            ColorAnimationDemo()
            
            // 6. Bounce Animation
            BounceAnimationDemo()
            
            // 7. Pulse Animation
            PulseAnimationDemo()
            
            // 8. Shake Animation
            ShakeAnimationDemo()
            
            // 9. Flip Animation
            FlipAnimationDemo()
            
            // 10. Wave Animation
            WaveAnimationDemo()
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DemoCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

// 1. Fade Animation
@Composable
private fun FadeAnimationDemo() {
    var visible by remember { mutableStateOf(true) }
    
    DemoCard("Fade Animation", "Opacity transition") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)),
                exit = fadeOut(tween(500))
            ) {
                Box(
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(16.dp)).background(PrimaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Star, null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { visible = !visible }, colors = ButtonDefaults.buttonColors(PrimaryColor)) {
                Text(if (visible) "Fade Out" else "Fade In")
            }
        }
    }
}

// 2. Scale Animation
@Composable
private fun ScaleAnimationDemo() {
    var expanded by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (expanded) 1.5f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    DemoCard("Scale Animation", "Size transformation with bounce") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(SecondaryColor)
                    .clickable { expanded = !expanded },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Favorite, null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Tap to scale", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

// 3. Slide Animation
@Composable
private fun SlideAnimationDemo() {
    var visible by remember { mutableStateOf(true) }
    
    DemoCard("Slide Animation", "Horizontal slide transition") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally { -it } + fadeIn(),
                    exit = slideOutHorizontally { it } + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(BlueColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                }
            }
            Button(onClick = { visible = !visible }, colors = ButtonDefaults.buttonColors(BlueColor)) {
                Text(if (visible) "Slide Out" else "Slide In")
            }
        }
    }
}

// 4. Rotation Animation
@Composable
private fun RotationAnimationDemo() {
    var rotating by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (rotating) 360f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "rotation",
        finishedListener = { rotating = false }
    )
    
    DemoCard("Rotation Animation", "360° spin effect") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .rotate(rotation)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentColor)
                    .clickable { rotating = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Refresh, null, tint = Color.DarkGray, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Tap to rotate", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

// 5. Color Animation
@Composable
private fun ColorAnimationDemo() {
    var toggled by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (toggled) PurpleColor else PrimaryColor,
        animationSpec = tween(600),
        label = "color"
    )
    
    DemoCard("Color Animation", "Smooth color transition") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable { toggled = !toggled },
                contentAlignment = Alignment.Center
            ) {
                Text("Tap", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 6. Bounce Animation
@Composable
private fun BounceAnimationDemo() {
    var bouncing by remember { mutableStateOf(false) }
    val offsetY by animateDpAsState(
        targetValue = if (bouncing) (-20).dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium),
        label = "bounce",
        finishedListener = { bouncing = false }
    )
    
    DemoCard("Bounce Animation", "Spring physics bounce") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .offset(y = offsetY)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Brush.verticalGradient(listOf(PrimaryColor, SecondaryColor)))
                    .clickable { bouncing = true },
                contentAlignment = Alignment.Center
            ) {
                Text("↑", color = Color.White, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Tap to bounce", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

// 7. Pulse Animation
@Composable
private fun PulseAnimationDemo() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "pulseScale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "pulseAlpha"
    )
    
    DemoCard("Pulse Animation", "Continuous breathing effect") {
        Box(
            modifier = Modifier
                .size(70.dp)
                .scale(scale)
                .alpha(alpha)
                .clip(CircleShape)
                .background(PrimaryColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Favorite, null, tint = Color.White, modifier = Modifier.size(36.dp))
        }
    }
}

// 8. Shake Animation
@Composable
private fun ShakeAnimationDemo() {
    var shaking by remember { mutableStateOf(false) }
    val offsetX = remember { Animatable(0f) }
    
    LaunchedEffect(shaking) {
        if (shaking) {
            repeat(4) {
                offsetX.animateTo(10f, tween(50))
                offsetX.animateTo(-10f, tween(50))
            }
            offsetX.animateTo(0f, tween(50))
            shaking = false
        }
    }
    
    DemoCard("Shake Animation", "Error/attention effect") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .offset(x = offsetX.value.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE74C3C))
                    .clickable { shaking = true },
                contentAlignment = Alignment.Center
            ) {
                Text("!", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Tap to shake", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

// 9. Flip Animation
@Composable
private fun FlipAnimationDemo() {
    var flipped by remember { mutableStateOf(false) }
    val rotationY by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(600),
        label = "flip"
    )
    
    DemoCard("Flip Animation", "Card flip effect") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer { this.rotationY = rotationY; cameraDistance = 12f * density }
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (rotationY <= 90f) SecondaryColor else PurpleColor)
                    .clickable { flipped = !flipped },
                contentAlignment = Alignment.Center
            ) {
                if (rotationY <= 90f) {
                    Text("Front", color = Color.White, fontWeight = FontWeight.Bold)
                } else {
                    Text("Back", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.graphicsLayer { this.rotationY = 180f })
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Tap to flip", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

// 10. Wave Animation
@Composable
private fun WaveAnimationDemo() {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    
    DemoCard("Wave Animation", "Staggered wave effect") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(5) { index ->
                val offsetY by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = -15f,
                    animationSpec = infiniteRepeatable(
                        tween(400, delayMillis = index * 100, easing = FastOutSlowInEasing),
                        RepeatMode.Reverse
                    ),
                    label = "wave$index"
                )
                Box(
                    modifier = Modifier
                        .offset(y = offsetY.dp)
                        .size(width = 16.dp, height = 50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF6366F1),
                                    Color(0xFF8B5CF6)
                                )
                            )
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnimationTypeScreenPreview() {
    AnimationTypeScreen()
}


