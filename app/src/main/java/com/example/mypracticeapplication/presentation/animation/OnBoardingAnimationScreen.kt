package com.example.mypracticeapplication.presentation.animation

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.R
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private val CenterBlueColor = Color(0xFF1D60BA)
private val CenterBlueLight = Color(0xFF1C78F2)
private val OrbitRingColor = Color(0xFF1C78F2).copy(alpha = 0.4f)

private data class OrbitIcon(
    @DrawableRes val icon: Int,
    val startAngleDegrees: Float
)

private val orbitIcons = listOf(
    OrbitIcon(R.drawable.img_onboarding_scan, 0f),
    OrbitIcon(R.drawable.img_onboarding_brain, 72f),
    OrbitIcon(R.drawable.img_onboarding_translate, 144f),
    OrbitIcon(R.drawable.img_onboarding_image, 216f),
    OrbitIcon(R.drawable.img_onboarding_batch, 288f)
)

@Composable
fun OnBoardingAnimationScreen(
    onNavigateBack: () -> Unit = {}
) {
    CircularAnimationComponent()
}

@Composable
private fun CircularAnimationComponent() {
    val infiniteTransition = rememberInfiniteTransition(label = "orbit")

    // Slow rotation animation (20 seconds for full circle)
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val density = LocalDensity.current
    val orbitRadius = 140.dp
    val iconSize = 56.dp
    val centerIconSize = 156.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Draw concentric orbit rings
        Canvas(
            modifier = Modifier.size(350.dp)
        ) {
            val center = Offset(size.width / 2, size.height / 2)

            // Draw 3 concentric circles
            listOf(0.55f, 0.7f, 0.85f).forEach { radiusFraction ->
                drawCircle(
                    color = OrbitRingColor,
                    radius = size.minDimension / 2 * radiusFraction,
                    center = center,
                    style = Stroke(width = 2f)
                )
            }
        }

        // Central blue circle with icon
        Box(
            modifier = Modifier
                .size(centerIconSize)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(CenterBlueLight, CenterBlueColor),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Inner icon box
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        clip = false
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(CenterBlueLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_onboarding_scan_center),
                    contentDescription = "Center Icon",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        // Orbiting icons
        orbitIcons.forEach { orbitIcon ->
            OrbitingIcon(
                icon = orbitIcon.icon,
                angleDegrees = orbitIcon.startAngleDegrees + rotationAngle,
                orbitRadius = orbitRadius,
                iconSize = iconSize
            )
        }
    }
}


@Composable
private fun OrbitingIcon(
    @DrawableRes icon: Int,
    angleDegrees: Float,
    orbitRadius: Dp,
    iconSize: Dp
) {
    val density = LocalDensity.current
    val angleRadians = Math.toRadians(angleDegrees.toDouble())
    
    val offsetX = with(density) { 
        (orbitRadius.toPx() * cos(angleRadians)).roundToInt() 
    }
    val offsetY = with(density) { 
        (orbitRadius.toPx() * sin(angleRadians)).roundToInt() 
    }

    Image(
        painter = painterResource(icon),
        modifier = Modifier
            .offset { IntOffset(offsetX, -offsetY) }
            .size(iconSize)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(14.dp),
                clip = false
            ),
        contentDescription = null,
    )
}

@Preview(showBackground = true)
@Composable
private fun OnBoardingAnimationScreenPreview() {
    OnBoardingAnimationScreen()
}


