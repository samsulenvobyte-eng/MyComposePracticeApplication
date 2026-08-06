package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin
import com.example.mypracticeapplication.R

// Reference Color Palette
private val DarkBackground = Color(0xFF0B0F19)
private val BarTopColor = Color(0xFFA86E90)
private val BarBottomColor = Color(0xFF0B0F19) // Fading into dark
private val BarGradient = listOf(BarTopColor, BarBottomColor)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingPage1Screen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Growth Analytics",
                        color = Color.White.copy(alpha = 0.9f)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkBackground),
            contentAlignment = Alignment.Center
        ) {
            AnimatedBarChart()
        }
    }
}

@Composable
fun AnimatedBarChart() {
    // Data definition: Relative heights [0.0 - 1.0]
    val barData = remember { listOf(0.4f, 0.55f, 0.65f, 0.85f, 0.95f) }
    
    // Animation States
    val overlayVisible = remember { Animatable(0f) }
    val bubblesVisible = remember { Animatable(0f) }
    val mainProgress = remember { Animatable(0f) } // Defines both Bar Height % and Counter %

    // Orchestration
    LaunchedEffect(Unit) {
        delay(800)
        
        // 1. Pop Images
        overlayVisible.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        
        delay(300)

        // 2. Pop Bubbles
        bubblesVisible.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        
        delay(200)

        // 3. Animate Bars and Numbers together
        mainProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )
    }

    // Infinite transition for ambient breathing
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathingPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    // Overlay Data
    val overlays = remember {
        listOf(
            ChartOverlay.Circle(
                xIndex = 0.5f,
                yPercent = 0.4f,
                radius = 55.dp,
                res = R.drawable.compress_before_1
            ),
            ChartOverlay.Pill(
                xIndex = 2.0f,
                yPercent = 0.5f, 
                width = 110.dp,
                height = 250.dp,
                res = R.drawable.img_people_portrait
            ),
            ChartOverlay.Pill( 
                xIndex = 3.5f,
                yPercent = 0.4f,
                width = 100.dp,
                height = 200.dp,
                res = R.drawable.img_people_landscape
            )
        )
    }
    
    // Load images
    val images = remember(overlays) { mutableMapOf<Int, androidx.compose.ui.graphics.ImageBitmap>() }
    val context = androidx.compose.ui.platform.LocalContext.current
    
    LaunchedEffect(overlays) {
        overlays.forEach { overlay ->
            if (!images.containsKey(overlay.res)) {
                val bitmap = android.graphics.BitmapFactory.decodeResource(context.resources, overlay.res)
                if (bitmap != null) {
                    images[overlay.res] = bitmap.asImageBitmap()
                }
            }
        }
    }

    // Shared Path for clipping to avoid per-frame allocations
    val clipPath = remember { androidx.compose.ui.graphics.Path() }

    Box(){

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 0.dp)
        ) {
            val barCount = barData.size
            val progressValue = mainProgress.value
            val phaseValue = breathingPhase
            val overlayAlpha = overlayVisible.value

            // Dynamic calculations
            val availableWidth = size.width
            val spacing = size.width * 0.05f // 5% spacing
            val barWidth = (availableWidth - (spacing * (barCount - 1))) / barCount

            // Draw Bars
            // Use manual for loop to avoid iterator allocation
            for (index in 0 until barCount) {
                val targetRelativeHeight = barData[index]

                // Calculate ambient offset using sine wave based on time (phase) and index
                val ambientOffset = if (progressValue > 0.95f) {
                    val offset = sin(phaseValue + index * 0.5f) * 0.03f
                    offset
                } else {
                    0f
                }

                // Combine heights: Target * Entrance * (1 + Ambient)
                val finalRelativeHeight = (targetRelativeHeight * progressValue + ambientOffset).coerceAtLeast(0.01f)

                val barHeight = size.height * finalRelativeHeight
                val xOffset = index * (barWidth + spacing)
                val yOffset = size.height - barHeight // Draw from bottom up

                // Draw Bar
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = BarGradient,
                        startY = yOffset,
                        endY = size.height
                    ), alpha = 0.3f,
                    topLeft = Offset(xOffset, yOffset),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(35f, 35f) // Fully rounded top
                )
            }

            // Draw Overlays
            if (overlayAlpha > 0f) {
                // Use manual for loop to avoid iterator allocation
                for (i in 0 until overlays.size) {
                    val overlay = overlays[i]
                    val image = images[overlay.res] ?: continue

                    val barCenterX = (overlay.xIndex * (barWidth + spacing)) + (barWidth / 2)
                    val centerY = size.height - (size.height * overlay.yPercent)
                    val scale = overlayAlpha

                    when (overlay) {
                        is ChartOverlay.Circle -> {
                            val radiusPx = overlay.radius.toPx() * scale
                            clipPath.reset()
                            clipPath.addOval(
                                androidx.compose.ui.geometry.Rect(
                                    center = Offset(barCenterX, centerY),
                                    radius = radiusPx
                                )
                            )
                            clipPath(clipPath) {
                                val dstWidth = radiusPx * 2
                                val dstHeight = radiusPx * 2
                                val imgWidth = image.width.toFloat()
                                val imgHeight = image.height.toFloat()
                                val scaleFactor = kotlin.math.max(dstWidth / imgWidth, dstHeight / imgHeight)
                                val scaledWidth = imgWidth * scaleFactor
                                val scaledHeight = imgHeight * scaleFactor
                                val drawX = (barCenterX - radiusPx) + (dstWidth - scaledWidth) / 2
                                val drawY = (centerY - radiusPx) + (dstHeight - scaledHeight) / 2
                                
                                drawImage(
                                    image = image,
                                    dstOffset = androidx.compose.ui.unit.IntOffset(drawX.toInt(), drawY.toInt()),
                                    dstSize = androidx.compose.ui.unit.IntSize(scaledWidth.toInt(), scaledHeight.toInt())
                                )
                            }
                        }
                        is ChartOverlay.Pill -> {
                            val widthPx = overlay.width.toPx() * scale
                            val heightPx = overlay.height.toPx() * scale
                            val topLeft = Offset(barCenterX - widthPx / 2, centerY - heightPx / 2)
                            clipPath.reset()
                            clipPath.addRoundRect(
                                androidx.compose.ui.geometry.RoundRect(
                                    rect = androidx.compose.ui.geometry.Rect(topLeft, Size(widthPx, heightPx)),
                                    cornerRadius = CornerRadius(widthPx / 2, widthPx / 2)
                                )
                            )
                            clipPath(clipPath) {
                                val dstWidth = widthPx
                                val dstHeight = heightPx
                                val imgWidth = image.width.toFloat()
                                val imgHeight = image.height.toFloat()
                                val scaleFactor = kotlin.math.max(dstWidth / imgWidth, dstHeight / imgHeight)
                                val scaledWidth = imgWidth * scaleFactor
                                val scaledHeight = imgHeight * scaleFactor
                                val drawX = topLeft.x + (dstWidth - scaledWidth) / 2
                                val drawY = topLeft.y + (dstHeight - scaledHeight) / 2
                                
                                drawImage(
                                    image = image,
                                    dstOffset = androidx.compose.ui.unit.IntOffset(drawX.toInt(), drawY.toInt()),
                                    dstSize = androidx.compose.ui.unit.IntSize(scaledWidth.toInt(), scaledHeight.toInt())
                                )
                            }
                        }
                    }
                }
            }
        }

        DynamicStatBubble(
            modifier = Modifier
                .padding(start = 32.dp)
                .rotate(-15f)
                .graphicsLayer {
                     scaleX = bubblesVisible.value
                     scaleY = bubblesVisible.value
                     alpha = bubblesVisible.value
                },
            icon = Icons.Default.Favorite,
            count = { (100 * mainProgress.value).toInt() },
            color = Color(0xFFE84E66),
            shadowColor = Color(0xFFA62C41)
        )

        DynamicStatBubble(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .padding(end = 32.dp)
                .rotate(15f)
                 .graphicsLayer {
                     scaleX = bubblesVisible.value
                     scaleY = bubblesVisible.value
                     alpha = bubblesVisible.value
                },
            icon = Icons.Default.Person,
            count = { (250 * mainProgress.value).toInt() }, // Different scale example
            color = Color(0xFF2DB3F9),
            shadowColor = Color(0xFFA62C41)
        )
    }
}


sealed class ChartOverlay(val xIndex: Float, val yPercent: Float, val res: Int) {
    class Circle(
        xIndex: Float,
        yPercent: Float,
        val radius: androidx.compose.ui.unit.Dp,
        res: Int
    ) : ChartOverlay(xIndex, yPercent, res)

    class Pill(
        xIndex: Float,
        yPercent: Float,
        val width: androidx.compose.ui.unit.Dp,
        val height: androidx.compose.ui.unit.Dp,
        res: Int
    ) : ChartOverlay(xIndex, yPercent, res)
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun OnboardingPage1ScreenPreview() {
    MaterialTheme {
        OnboardingPage1Screen(onNavigateBack = {})
    }
}


