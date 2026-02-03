package com.example.mypracticeapplication.ui.screens.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.math.sin
import kotlin.random.Random



// --- Heart Emitter Component Models ---

data class HeartData(
    val id: UUID = UUID.randomUUID(),
    val x: Float,
    val y: Float,
    val rotation: Float,
    val color: Color,
    val scaleTo: Float = Random.nextFloat() * 0.4f + 0.8f
)

@Stable
class HeartEmitterState {
    val hearts: SnapshotStateList<HeartData> = mutableStateListOf()

    fun emit(x: Float, y: Float) {
        hearts.add(
            HeartData(
                x = x,
                y = y,
                rotation = Random.nextFloat() * 60f - 30f,
                color = LovePalette[Random.nextInt(LovePalette.size)]
            )
        )
    }

    fun remove(heart: HeartData) {
        hearts.remove(heart)
    }
}

@Composable
fun rememberHeartEmitterState() = remember { HeartEmitterState() }

// --- Heart Emitter Component ---

@Composable
fun HeartEmitter(
    state: HeartEmitterState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        // Base Content
        content()

        // Overlay Particles
        state.hearts.forEach { heart ->
            FloatingHeartView(
                heart = heart,
                onAnimationFinished = { state.remove(heart) }
            )
        }
    }
}

// --- Internal Animation View ---

@Composable
private fun FloatingHeartView(
    heart: HeartData,
    onAnimationFinished: () -> Unit
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val yOffset = remember { Animatable(0f) }
    val xWiggle = remember { Animatable(0f) }

    LaunchedEffect(heart.id) {
        // 1. Entrance Pop
        async {
            scale.animateTo(
                targetValue = heart.scaleTo * 1.2f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            scale.animateTo(
                targetValue = heart.scaleTo,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }

        // 2. Float Upward
        async {
            yOffset.animateTo(
                targetValue = -300f - Random.nextFloat() * 200f,
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
        }

        // 3. Wiggle
        async {
            val startWiggle = Random.nextFloat() * 40f - 20f
            xWiggle.animateTo(targetValue = startWiggle, animationSpec = tween(800, easing = SineEasing))
            xWiggle.animateTo(targetValue = -startWiggle, animationSpec = tween(700, easing = SineEasing))
        }

        // 4. Fade & Cleanup
        async {
            delay(1000)
            alpha.animateTo(0f, animationSpec = tween(500))
            onAnimationFinished()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = heart.color,
            modifier = Modifier
                .size(48.dp)
                .offset {
                    IntOffset(
                        x = (heart.x - 24.dp.toPx()).toInt() + xWiggle.value.toInt(),
                        y = (heart.y - 24.dp.toPx()).toInt() + yOffset.value.toInt()
                    )
                }
                .scale(scale.value)
                .rotate(heart.rotation)
                .alpha(alpha.value)
        )
    }
}

private val SineEasing = Easing { x -> sin(x * Math.PI).toFloat() }

// --- Screen Implementation ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyAnimationScreen(
    onNavigateBack: () -> Unit
) {
    val heartState = rememberHeartEmitterState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Heart Emitter Lab", color = Color.White.copy(alpha = 0.9f)) },
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
        HeartEmitter(
            state = heartState,
            modifier = Modifier
                .size(300.dp)
                .padding(paddingValues)
                .background(Color.Cyan)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        heartState.emit(offset.x, offset.y)
                    }
                }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tap anywhere to celebrate! ❤️",
                    color = Color.White.copy(alpha = 0.3f),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
private fun EmptyAnimationScreenPreview() {
    MaterialTheme {
        EmptyAnimationScreen(onNavigateBack = {})
    }
}
