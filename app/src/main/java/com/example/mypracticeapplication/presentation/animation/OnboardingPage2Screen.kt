package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.R
import kotlinx.coroutines.delay
import kotlin.math.sin

// Reference Color Palette
private val DarkBackground = Color(0xFF0B0F19)
private val BarTopColor = Color(0xFFA86E90)
private val BarBottomColor = Color(0xFF0B0F19) // Fading into dark
private val BarGradient = listOf(BarTopColor, BarBottomColor)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingPage2Screen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Growth Analytics V2",
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
            AnimatedBarChartV2(modifier = Modifier)
        }
    }
}


@Composable
fun AnimatedBarChartV2(
    modifier: Modifier = Modifier
) {
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
            ChartOverlayV2.Circle(
                xIndex = 0.5f,
                yPercent = 0.4f,
                radius = 55.dp,
                res = R.drawable.compress_before_1
            ),
            ChartOverlayV2.Pill(
                xIndex = 2.0f,
                yPercent = 0.5f,
                width = 110.dp,
                height = 250.dp,
                res = R.drawable.img_people_portrait
            ),
            ChartOverlayV2.Pill(
                xIndex = 3.5f,
                yPercent = 0.4f,
                width = 100.dp,
                height = 200.dp,
                res = R.drawable.img_people_landscape
            )
        )
    }

    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        val availableWidth = maxWidth
        val availableHeight = maxHeight
        val barCount = barData.size
        val spacing = availableWidth * 0.05f
        val barWidth = (availableWidth - (spacing * (barCount - 1))) / barCount

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // Draw Bars
            val barWidthPx = barWidth.toPx()
            val spacingPx = spacing.toPx()
            val canvasHeight = size.height
            val barDataCount = barData.size

            for (index in 0 until barDataCount) {
                val targetRelativeHeight = barData[index]
                val entranceProgress = mainProgress.value

                // Calculate ambient offset using sine wave based on time (phase) and index
                val ambientOffset = if (entranceProgress > 0.95f) {
                    sin(breathingPhase + index * 0.5f) * 0.03f
                } else {
                    0f
                }

                // Combine heights: Target * Entrance * (1 + Ambient)
                val finalRelativeHeight =
                    (targetRelativeHeight * entranceProgress + ambientOffset).coerceAtLeast(0.01f)

                val barHeight = canvasHeight * finalRelativeHeight
                val xOffset = index * (barWidthPx + spacingPx)
                val yOffset = canvasHeight - barHeight // Draw from bottom up

                // Draw Bar
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = BarGradient,
                        startY = yOffset,
                        endY = canvasHeight
                    ), alpha = 0.3f,
                    topLeft = Offset(xOffset, yOffset),
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(35f, 35f) // Fully rounded top
                )
            }
        }

        // Draw Overlays as Components
        val overlaysCount = overlays.size
        for (i in 0 until overlaysCount) {
            val overlay = overlays[i]
            // Calculate position
            val barCenterX =
                (overlay.xIndex * (barWidth.value + spacing.value)) + (barWidth.value / 2)
            val fullHeightVal = availableHeight.value
            val centerY = fullHeightVal - (fullHeightVal * overlay.yPercent)

            // Render component based on type
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset {
                        androidx.compose.ui.unit.IntOffset(
                            barCenterX.dp.roundToPx(),
                            centerY.dp.roundToPx()
                        )
                    }
                    .graphicsLayer {
                        val scale = overlayVisible.value
                        scaleX = scale
                        scaleY = scale
                        alpha = scale
                    }
            ) {
                OverlayComponent(overlay = overlay, modifier = Modifier)
            }
        }

        DynamicStatBubble(
            modifier = Modifier
                .padding(start = 32.dp)
                .rotate(-15f)
                .align(Alignment.TopStart)
                .offset(y = (-50).dp)
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

@Composable
fun OverlayComponent(
    overlay: ChartOverlayV2,
    modifier: Modifier = Modifier
) {
    val painter = androidx.compose.ui.res.painterResource(id = overlay.res)

    when (overlay) {
        is ChartOverlayV2.Circle -> {
            androidx.compose.foundation.Image(
                painter = painter,
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = modifier
                    .offset(x = -overlay.radius, y = -overlay.radius)
                    .size(overlay.radius * 2)
                    .clip(androidx.compose.foundation.shape.CircleShape)
            )
        }

        is ChartOverlayV2.Pill -> {
            androidx.compose.foundation.Image(
                painter = painter,
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = modifier
                    .offset(x = -overlay.width / 2, y = -overlay.height / 2)
                    .size(overlay.width, overlay.height)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
            )
        }

        is ChartOverlayV2.ProfileCard -> {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = modifier
                    .offset(x = -overlay.width / 2, y = -overlay.height / 2)
                    .size(overlay.width, overlay.height)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
            )
        }
    }
}

sealed class ChartOverlayV2(val xIndex: Float, val yPercent: Float, val res: Int) {
    class Circle(
        xIndex: Float,
        yPercent: Float,
        val radius: androidx.compose.ui.unit.Dp,
        res: Int
    ) : ChartOverlayV2(xIndex, yPercent, res)

    class Pill(
        xIndex: Float,
        yPercent: Float,
        val width: androidx.compose.ui.unit.Dp,
        val height: androidx.compose.ui.unit.Dp,
        res: Int
    ) : ChartOverlayV2(xIndex, yPercent, res)

    class ProfileCard(
        xIndex: Float,
        yPercent: Float,
        val width: androidx.compose.ui.unit.Dp,
        val height: androidx.compose.ui.unit.Dp,
        res: Int
    ) : ChartOverlayV2(xIndex, yPercent, res)
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun OnboardingPage2ScreenPreview() {
    MaterialTheme {
        OnboardingPage2Screen(onNavigateBack = {})
    }
}


