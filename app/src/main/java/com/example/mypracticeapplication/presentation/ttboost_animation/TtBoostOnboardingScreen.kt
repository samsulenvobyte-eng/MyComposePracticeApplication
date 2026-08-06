package com.example.mypracticeapplication.presentation.ttboost_animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.R
import kotlinx.coroutines.delay

/**
 * TtBoost Onboarding Screen
 * A polished analytics visualization with orchestrated animations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TtBoostOnboardingScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "TtBoost Analytics",
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
                    containerColor = TtBoostTheme.DarkBackground,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = TtBoostTheme.DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(TtBoostTheme.DarkBackground),
            contentAlignment = Alignment.Center
        ) {
            TtBoostContent()
        }
    }
}

@Composable
private fun TtBoostContent() {
    // Data definition: Relative heights [0.0 - 1.0]
    val barData = remember { listOf(0.4f, 0.55f, 0.65f, 0.85f, 0.95f) }

    // Animation States
    val overlayVisible = remember { Animatable(0f) }
    val bubblesVisible = remember { Animatable(0f) }
    val mainProgress = remember { Animatable(0f) }

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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        val availableWidth = maxWidth
        val availableHeight = maxHeight
        val barCount = barData.size
        val spacing = availableWidth * 0.05f
        val barWidth = (availableWidth - (spacing * (barCount - 1))) / barCount

        // Animated Bar Chart
        AnimatedBarChart(
            barData = barData,
            entranceProgress = { mainProgress.value },
            barWidth = barWidth,
            barSpacing = spacing,
            modifier = Modifier.fillMaxSize()
        )

        // Draw Overlays
        if (overlayVisible.value > 0f) {
            val scale = overlayVisible.value

            overlays.forEach { overlay ->
                // Calculate position
                val barCenterX =
                    (overlay.xIndex * (barWidth.value + spacing.value)) + (barWidth.value / 2)
                val fullHeightVal = availableHeight.value
                val centerY = fullHeightVal - (fullHeightVal * overlay.yPercent)

                // Render overlay with animation
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = barCenterX.dp, y = centerY.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            alpha = scale
                        }
                ) {
                    OverlayRenderer(overlay)
                }
            }
        }

        // Heart Bubble (Top-Left)
        StatBubble(
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
            color = TtBoostTheme.Bubble.HeartColor,
            shadowColor = TtBoostTheme.Bubble.HeartShadow
        )

        // Person Bubble (Top-Right)
        StatBubble(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 32.dp)
                .rotate(15f)
                .graphicsLayer {
                    scaleX = bubblesVisible.value
                    scaleY = bubblesVisible.value
                    alpha = bubblesVisible.value
                },
            icon = Icons.Default.Person,
            count = { (250 * mainProgress.value).toInt() },
            color = TtBoostTheme.Bubble.PersonColor,
            shadowColor = TtBoostTheme.Bubble.PersonShadow
        )
    }
}

@Preview
@Composable
private fun TtBoostOnboardingScreenPreview() {
    MaterialTheme {
        TtBoostOnboardingScreen(onNavigateBack = {})
    }
}


