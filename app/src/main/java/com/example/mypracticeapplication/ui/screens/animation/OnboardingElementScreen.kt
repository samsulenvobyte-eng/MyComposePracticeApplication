package com.example.mypracticeapplication.ui.screens.animation


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sin

// Colors
private val BubbleColor = Color(0xFFE84E66) // Comparable pink/red
private val BubbleShadowColor = Color(0xFFA62C41) // Darker shade for depth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingElementScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "3D Heart Bubble",
                        color = Color.White
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
            HeartBubble3D()
        }
    }
}

@Composable
fun HeartBubble3D() {
    // Demonstration of the generic component
    var count by remember { mutableIntStateOf(100) }
    
    // Auto-increment simulation for demo purposes
    LaunchedEffect(Unit) {
        delay(1000)
        repeat(47) {
            delay(50)
            count++
        }
    }
    
    DynamicStatBubble(
        icon = Icons.Default.Favorite,
        count = count,
        color = Color(0xFFE84E66),
        onCountChange = { count++ }
    )
}

@Composable
fun DynamicStatBubble(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier,
    onCountChange: () -> Unit = {}
) {
    // 3D Rotation Animation
    val infiniteTransition = rememberInfiniteTransition(label = "3d_float")
    val rotationY by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotationY"
    )
    
    val rotationX by infiniteTransition.animateFloat(
        initialValue = 5f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotationX"
    )

    Box(
        modifier = modifier
            .clickable(
                indication = null, 
                interactionSource = remember { MutableInteractionSource() }
            ) { onCountChange() }
            .graphicsLayer {
                this.rotationY = rotationY
                this.rotationX = rotationX
                cameraDistance = 12f * density
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            }
    ) {
//        // Commented out depth layers as per user preference in manual edits
//        val depthLayers = 16
//        for (i in depthLayers downTo 1) {
//            BubbleSurface(
//                color = shadowColor,
//                modifier = Modifier
//                    .offset(x = 0.dp, y = (i * 0.5f).dp)
//                    .graphicsLayer {
//                        // translationZ is not standard in Compose GraphicsLayerScope
//                    }
//            )
//        }

        // Main Faces
        BubbleSurface(
            color = color,
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start= 16.dp,end = 16.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OdometerText(
                    count = count,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun BubbleSurface(
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null
) {
    val bubbleShape = remember { BubbleShape() }
    
    Box(
        modifier = modifier
            .background(color, bubbleShape),
        contentAlignment = Alignment.Center
    ) {
        if (content != null) {
            Box(modifier = Modifier.padding(bottom = 20.dp)) {
                content()
            }
        }
    }
}

// Custom Shape Definition (Preserving user manual edits)
fun BubbleShape(): GenericShape {
    return GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val cornerRadius = 40f
        val pointerHeight = 50f
        val pointerWidth = 50f
        val rectHeight = height - pointerHeight

        addRoundRect(
            RoundRect(
                rect = Rect(offset = Offset.Zero, size = Size(width, rectHeight)),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        )

        moveTo((width / 2) - (pointerWidth / 2), rectHeight)
        lineTo(width / 2, height)
        lineTo((width / 2) + (pointerWidth / 2), rectHeight)
        close()
    }
}

@Preview
@Composable
private fun OnboardingElementScreenPreview() {
    MaterialTheme {
        OnboardingElementScreen(onNavigateBack = {})
    }
}

@Composable
fun OdometerText(
    count: Int,
    style: androidx.compose.ui.text.TextStyle,
    color: Color
) {
    val digits = count.toString().map { it }
    
    // Track previous count to determine direction
    var lastCount by remember { mutableIntStateOf(count) }
    val isCountUp = count >= lastCount
    
    SideEffect {
        lastCount = count
    }

    Row {
        digits.forEach { digit ->
            Digit(
                digit = digit,
                style = style,
                color = color,
                isCountUp = isCountUp
            )
        }
    }
}

@Composable
private fun Digit(
    digit: Char,
    style: androidx.compose.ui.text.TextStyle,
    color: Color,
    isCountUp: Boolean
) {
    AnimatedContent(
        targetState = digit,
        transitionSpec = {
            if (isCountUp) {
                // Determine direction based on global count change, not digit comparison
                (slideInVertically { height -> height } + fadeIn()).togetherWith(
                    slideOutVertically { height -> -height } + fadeOut())
            } else {
                (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                    slideOutVertically { height -> height } + fadeOut())
            }.using(SizeTransform(clip = false))
        },
        label = "digit_anim"
    ) { targetDigit ->
        Text(
            text = targetDigit.toString(),
            style = style,
            color = color
        )
    }
}
