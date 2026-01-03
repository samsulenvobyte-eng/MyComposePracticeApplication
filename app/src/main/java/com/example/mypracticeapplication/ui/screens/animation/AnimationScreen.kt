package com.example.mypracticeapplication.ui.screens.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypracticeapplication.R
import kotlinx.coroutines.delay

// Colors
private val PrimaryColor = Color(0xFFFF6B6B)
private val SecondaryColor = Color(0xFF4ECDC4)
private val AccentColor = Color(0xFFFFE66D)

@Composable
fun AnimationScreen(
    onNavigateBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Header(onNavigateBack = onNavigateBack)
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            // 1️⃣ AnimatedVisibility Demo
//            AnimatedVisibilityDemo()
//
//            // 2️⃣ Animate*AsState Demo
//            AnimateAsStateDemo()
//
//            // 3️⃣ AnimatedContent Demo
//            AnimatedContentDemo()
//
//            // 4️⃣ Infinite Transition Demo
//            InfiniteTransitionDemo()
//
//            // 5️⃣ Animatable Demo
//            AnimatableDemo()
//
//            // 6️⃣ animateContentSize Demo
//            AnimateContentSizeDemo()
//
            // 7️⃣ Link/Unlink Icon Animation with Canvas
            LinkUnlinkDemo()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Header(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(PrimaryColor, SecondaryColor)
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
                text = "🎬 Animation Lab",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Compose Animation APIs",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 1️⃣ AnimatedVisibility - Show/Hide with animations
// ═══════════════════════════════════════════════════════════════════════════
@Composable
private fun AnimatedVisibilityDemo() {
    var isVisible by remember { mutableStateOf(true) }
    
    DemoCard(
        title = "1️⃣ AnimatedVisibility",
        subtitle = "Show/hide elements with enter/exit animations"
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { isVisible = !isVisible },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
            ) {
                Text(if (isVisible) "Hide" else "Show")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + scaleIn() + slideInHorizontally(),
                exit = fadeOut() + scaleOut() + slideOutHorizontally()
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(PrimaryColor, SecondaryColor)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 2️⃣ animate*AsState - Animate single values
// ═══════════════════════════════════════════════════════════════════════════
@Composable
private fun AnimateAsStateDemo() {
    var isSelected by remember { mutableStateOf(false) }
    
    // Animated values
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryColor else Color.LightGray,
        animationSpec = tween(500),
        label = "bgColor"
    )
    
    val size by animateDpAsState(
        targetValue = if (isSelected) 120.dp else 80.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "size"
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (isSelected) 360f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "rotation"
    )
    
    DemoCard(
        title = "2️⃣ animate*AsState",
        subtitle = "Color, Size, Rotation with spring/tween"
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(size)
                    .rotate(rotation)
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .clickable { isSelected = !isSelected },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Tap the box!",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 3️⃣ AnimatedContent - Animate content changes
// ═══════════════════════════════════════════════════════════════════════════
@Composable
private fun AnimatedContentDemo() {
    var count by remember { mutableIntStateOf(0) }
    
    DemoCard(
        title = "3️⃣ AnimatedContent",
        subtitle = "Animate between different content states"
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    if (targetState > initialState) {
                        // Counting up: slide in from bottom
                        (fadeIn() + slideInHorizontally { it }) togetherWith
                            (fadeOut() + slideOutHorizontally { -it })
                    } else {
                        // Counting down: slide in from top
                        (fadeIn() + slideInHorizontally { -it }) togetherWith
                            (fadeOut() + slideOutHorizontally { it })
                    }
                },
                label = "counter"
            ) { targetCount ->
                Text(
                    text = "$targetCount",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        targetCount < 0 -> PrimaryColor
                        targetCount > 0 -> SecondaryColor
                        else -> Color.Gray
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { count-- },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                ) {
                    Text("- 1")
                }
                Button(
                    onClick = { count = 0 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = { count++ },
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor)
                ) {
                    Text("+ 1")
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 4️⃣ InfiniteTransition - Continuous animations
// ═══════════════════════════════════════════════════════════════════════════
@Composable
private fun InfiniteTransitionDemo() {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    
    // Pulsing scale
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    // Rotating
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Color shifting
    val colorShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )
    
    val animatedColor = lerp(PrimaryColor, SecondaryColor, colorShift)
    
    DemoCard(
        title = "4️⃣ InfiniteTransition",
        subtitle = "Continuous pulse, rotation & color shift"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Pulsing circle
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(PrimaryColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            
            // Rotating star
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .rotate(rotation)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SecondaryColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            
            // Color shifting
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(animatedColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 5️⃣ Animatable - Programmatic control
// ═══════════════════════════════════════════════════════════════════════════
@Composable
private fun AnimatableDemo() {
    val offsetX = remember { Animatable(0f) }
    var isRunning by remember { mutableStateOf(false) }
    
    LaunchedEffect(isRunning) {
        if (isRunning) {
            // Animate right
            offsetX.animateTo(
                targetValue = 200f,
                animationSpec = tween(500)
            )
            // Animate back
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            )
            isRunning = false
        }
    }
    
    DemoCard(
        title = "5️⃣ Animatable",
        subtitle = "Programmatic control with coroutines"
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = offsetX.value.dp)
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(PrimaryColor, AccentColor)
                            )
                        )
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { isRunning = true },
                enabled = !isRunning,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
            ) {
                Text("Bounce!")
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 6️⃣ animateContentSize - Auto-animate size changes
// ═══════════════════════════════════════════════════════════════════════════
@Composable
private fun AnimateContentSizeDemo() {
    var expanded by remember { mutableStateOf(false) }
    
    DemoCard(
        title = "6️⃣ animateContentSize",
        subtitle = "Automatically animate container size changes"
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Expand details", fontWeight = FontWeight.Medium)
                Switch(
                    checked = expanded,
                    onCheckedChange = { expanded = it }
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = SecondaryColor.copy(alpha = 0.2f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📱 Compose Animation",
                        fontWeight = FontWeight.Bold,
                        color = SecondaryColor
                    )
                    
                    if (expanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Jetpack Compose provides powerful animation APIs:\n\n" +
                                   "• AnimatedVisibility for enter/exit\n" +
                                   "• animate*AsState for value changes\n" +
                                   "• AnimatedContent for content swapping\n" +
                                   "• InfiniteTransition for loops\n" +
                                   "• Animatable for coroutine control\n" +
                                   "• animateContentSize for size changes",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 7️⃣ Link/Unlink Icon Animation with Vector Drawables
// ═══════════════════════════════════════════════════════════════════════════
@Composable
private fun LinkUnlinkDemo() {
    var isLinked by remember { mutableStateOf(true) }
    
    // Animated offset for separation when unlinked
    val separationOffset by animateDpAsState(
        targetValue = if (isLinked) 0.dp else 40.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "separation"
    )
    
    // Color animation for the icons
    val iconColor by animateColorAsState(
        targetValue = if (isLinked) Color(0xFF333333) else PrimaryColor,
        animationSpec = tween(300),
        label = "iconColor"
    )
    
    // Background color animation
    val backgroundColor by animateColorAsState(
        targetValue = if (isLinked) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
        animationSpec = tween(300),
        label = "bgColor"
    )
    
    // Slight rotation when unlinked for visual effect
    val rotation by animateFloatAsState(
        targetValue = if (isLinked) 0f else 8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "rotation"
    )
    
    DemoCard(
        title = "7️⃣ Link/Unlink Icon",
        subtitle = "Vector drawable animation - tap to toggle"
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(backgroundColor)
                    .clickable { isLinked = !isLinked },
                contentAlignment = Alignment.Center
            ) {
                // Container for the two link halves
                Box(
                    modifier = Modifier
                        .size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Calculate alpha for crossfade effect
                    // Link icons fade out, circle icons fade in
                    val linkAlpha = 1f - (separationOffset.value / 40f).coerceIn(0f, 1f)
                    val circleAlpha = ((separationOffset.value / 40f) - 0.3f).coerceIn(0f, 1f) / 0.7f
                    
                    // ═══════════════════════════════════════════════════════════
                    // TOP POSITION - Link fades to Circle
                    // ═══════════════════════════════════════════════════════════
                    Box(
                        modifier = Modifier
                            .size(width = 72.dp, height = 48.dp)
                            .offset(
                                x = 0.dp,
                                y = -separationOffset / 2 - 8.dp
                            )
                    ) {
                        // Top link (fades out when unlinked)
                        Image(
                            painter = painterResource(id = R.drawable.top_link),
                            contentDescription = "Top link",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = linkAlpha },
                            colorFilter = ColorFilter.tint(iconColor)
                        )
                        
                        // Circle (fades in when unlinked)
                        Image(
                            painter = painterResource(id = R.drawable.ic_circle),
                            contentDescription = "Circle",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = circleAlpha },
                            colorFilter = ColorFilter.tint(iconColor)
                        )
                    }
                    
                    // ═══════════════════════════════════════════════════════════
                    // BOTTOM POSITION - Link fades to Circle
                    // ═══════════════════════════════════════════════════════════
                    Box(
                        modifier = Modifier
                            .size(width = 72.dp, height = 48.dp)
                            .offset(
                                x = 33.dp,
                                y = separationOffset / 2 + 10.dp
                            )
                    ) {
                        // Bottom link (fades out when unlinked)
                        Image(
                            painter = painterResource(id = R.drawable.bottom_link),
                            contentDescription = "Bottom link",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = linkAlpha },
                            colorFilter = ColorFilter.tint(iconColor)
                        )
                        
                        // Circle (fades in when unlinked)
                        Image(
                            painter = painterResource(id = R.drawable.ic_circle),
                            contentDescription = "Circle",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = circleAlpha },
                            colorFilter = ColorFilter.tint(iconColor)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Status text with icon
            Text(
                text = if (isLinked) "🔗 Linked" else "🔓 Unlinked",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isLinked) Color(0xFF2E7D32) else PrimaryColor
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Tap the icon to toggle",
                fontSize = 13.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Toggle buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { isLinked = true },
                    enabled = !isLinked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        disabledContainerColor = Color(0xFF4CAF50).copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🔗 Link")
                }
                Button(
                    onClick = { isLinked = false },
                    enabled = isLinked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        disabledContainerColor = PrimaryColor.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🔓 Unlink")
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Shared Components
// ═══════════════════════════════════════════════════════════════════════════
@Composable
private fun DemoCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            content()
        }
    }
}

// Color interpolation helper
private fun lerp(start: Color, stop: Color, fraction: Float): Color {
    return Color(
        red = start.red + (stop.red - start.red) * fraction,
        green = start.green + (stop.green - start.green) * fraction,
        blue = start.blue + (stop.blue - start.blue) * fraction,
        alpha = start.alpha + (stop.alpha - start.alpha) * fraction
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnimationScreenPreview() {
    AnimationScreen()
}
