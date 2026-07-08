package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryColor = Color(0xFFFF6B6B)
private val SecondaryColor = Color(0xFF4ECDC4)

// Bangladesh flag colors
private val BangladeshGreen = Color(0xFF006A4E)
private val BangladeshRed = Color(0xFFF42A41)

/**
 * Data class for city markers on the map
 */
@Immutable
data class CityMarker(
    val name: String,
    val x: Float,
    val y: Float,
    val isCapital: Boolean = false
)

private val MajorCities = listOf(
    CityMarker("Dhaka", 0.52f, 0.48f, isCapital = true),
    CityMarker("Chittagong", 0.75f, 0.68f),
    CityMarker("Khulna", 0.32f, 0.58f),
    CityMarker("Rajshahi", 0.18f, 0.32f),
    CityMarker("Sylhet", 0.72f, 0.25f),
    CityMarker("Rangpur", 0.25f, 0.12f),
    CityMarker("Barisal", 0.42f, 0.68f),
    CityMarker("Mymensingh", 0.55f, 0.35f)
)

private val BangladeshiBorderPoints = listOf(
    // Start at Rangpur region (northwest) - the distinctive bulge
    Pair(0.12f, 0.18f),
    Pair(0.08f, 0.14f),
    Pair(0.10f, 0.08f),
    Pair(0.18f, 0.04f),
    Pair(0.28f, 0.02f),
    Pair(0.35f, 0.06f),

    // Northern border moving east
    Pair(0.42f, 0.12f),
    Pair(0.48f, 0.16f),
    Pair(0.52f, 0.14f),
    Pair(0.58f, 0.18f),

    // Sylhet region (northeast) - bump outward
    Pair(0.65f, 0.15f),
    Pair(0.72f, 0.12f),
    Pair(0.80f, 0.14f),
    Pair(0.88f, 0.18f),
    Pair(0.92f, 0.24f),
    Pair(0.88f, 0.30f),
    Pair(0.82f, 0.32f),

    // Eastern border - moves south with indentations
    Pair(0.78f, 0.38f),
    Pair(0.72f, 0.42f),
    Pair(0.68f, 0.48f),
    Pair(0.72f, 0.52f),
    Pair(0.78f, 0.55f),

    // Chittagong region - distinctive eastern protrusion
    Pair(0.82f, 0.58f),
    Pair(0.85f, 0.62f),
    Pair(0.88f, 0.68f),
    Pair(0.86f, 0.75f),
    Pair(0.82f, 0.82f),
    Pair(0.78f, 0.88f),
    Pair(0.75f, 0.94f),
    Pair(0.70f, 0.98f),

    // Southern coast - Bay of Bengal with delta features
    Pair(0.62f, 0.95f),
    Pair(0.55f, 0.88f),
    Pair(0.50f, 0.82f),
    Pair(0.45f, 0.78f),
    Pair(0.42f, 0.85f),
    Pair(0.38f, 0.90f),

    // Sundarbans delta region (southwest) - multiple channels
    Pair(0.32f, 0.92f),
    Pair(0.28f, 0.88f),
    Pair(0.22f, 0.90f),
    Pair(0.18f, 0.85f),
    Pair(0.15f, 0.80f),
    Pair(0.12f, 0.75f),

    // Western border - moving north along India border
    Pair(0.08f, 0.68f),
    Pair(0.05f, 0.60f),
    Pair(0.08f, 0.52f),
    Pair(0.12f, 0.45f),
    Pair(0.08f, 0.38f),
    Pair(0.05f, 0.32f),
    Pair(0.08f, 0.25f),
    Pair(0.12f, 0.18f)
)

/**
 * Animated Bangladesh Map with accurate country outline
 */
@Composable
fun AnimatedBangladeshMap(
    modifier: Modifier = Modifier,
    showCities: Boolean = true,
    showAnimation: Boolean = true,
    fillColor: Color = BangladeshGreen.copy(alpha = 0.3f),
    strokeColor: Color = BangladeshGreen,
    cityColor: Color = BangladeshRed,
    animationDuration: Int = 2000
) {
    val textMeasurer = rememberTextMeasurer()
    
    val drawProgress = remember { Animatable(if (showAnimation) 0f else 1f) }
    val fillProgress = remember { Animatable(if (showAnimation) 0f else 1f) }
    val cityProgress = remember { Animatable(if (showAnimation) 0f else 1f) }
    
    LaunchedEffect(showAnimation) {
        if (showAnimation) {
            drawProgress.snapTo(0f)
            fillProgress.snapTo(0f)
            cityProgress.snapTo(0f)

            drawProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing)
            )
            fillProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
            cityProgress.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
        }
    }

    val cityTextLayouts = remember(textMeasurer) {
        MajorCities.map { city ->
            val textStyle = TextStyle(
                fontSize = if (city.isCapital) 11.sp else 9.sp,
                fontWeight = if (city.isCapital) FontWeight.Bold else FontWeight.Normal,
                color = Color.DarkGray
            )
            textMeasurer.measure(city.name, textStyle)
        }
    }

    androidx.compose.foundation.layout.Spacer(
        modifier = modifier.drawWithCache {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val padding = 16.dp.toPx()

            val mapWidth = canvasWidth - padding * 2
            val mapHeight = canvasHeight - padding * 2

            val mapPath = Path().apply {
                val firstPoint = BangladeshiBorderPoints.first()
                moveTo(
                    padding + firstPoint.first * mapWidth,
                    padding + firstPoint.second * mapHeight
                )

                for (i in 1 until BangladeshiBorderPoints.size) {
                    val curr = BangladeshiBorderPoints[i]
                    val currX = padding + curr.first * mapWidth
                    val currY = padding + curr.second * mapHeight
                    lineTo(currX, currY)
                }
                close()
            }

            val capitalMarkerSize = 10.dp.toPx()
            val regularMarkerSize = 6.dp.toPx()
            val strokeWidth = 2.5f.dp.toPx()
            val labelPadding = 3.dp.toPx()

            onDrawBehind {
                // Draw filled map
                if (fillProgress.value > 0) {
                    drawPath(
                        path = mapPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(fillColor, fillColor), // Optimization: use same color for base brush
                            startY = 0f,
                            endY = size.height
                        ),
                        alpha = fillProgress.value,
                        style = Fill
                    )
                }

                // Draw map outline with animation
                val clipWidth = size.width * drawProgress.value
                clipRect(left = 0f, top = 0f, right = clipWidth, bottom = size.height) {
                    drawPath(
                        path = mapPath,
                        color = strokeColor,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }

                // Draw cities
                if (showCities && cityProgress.value > 0) {
                    for (i in MajorCities.indices) {
                        val city = MajorCities[i]
                        val cityX = padding + city.x * mapWidth
                        val cityY = padding + city.y * mapHeight
                        val markerSize = if (city.isCapital) capitalMarkerSize else regularMarkerSize
                        val animatedSize = markerSize * cityProgress.value

                        drawCircle(
                            color = if (city.isCapital) BangladeshRed else cityColor,
                            alpha = if (city.isCapital) 1f else 0.8f,
                            radius = animatedSize / 2,
                            center = Offset(cityX, cityY)
                        )

                        if (city.isCapital) {
                            drawCircle(
                                color = Color.White,
                                radius = animatedSize / 4,
                                center = Offset(cityX, cityY)
                            )
                        }

                        if (cityProgress.value > 0.5f) {
                            val textAlpha = ((cityProgress.value - 0.5f) * 2f).coerceIn(0f, 1f)
                            val textLayoutResult = cityTextLayouts[i]

                            drawText(
                                textLayoutResult = textLayoutResult,
                                alpha = textAlpha,
                                topLeft = Offset(
                                    cityX - textLayoutResult.size.width / 2,
                                    cityY + animatedSize / 2 + labelPadding
                                )
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(colors = listOf(PrimaryColor, SecondaryColor)))
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
                    text = "🗺️ Maps",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Map Animations & Interactions",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🇧🇩 Bangladesh",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BangladeshGreen
                    )
                    Text(
                        text = "People's Republic of Bangladesh",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    AnimatedBangladeshMap(
                        modifier = Modifier.fillMaxWidth().aspectRatio(0.75f),
                        showCities = true,
                        showAnimation = true,
                        animationDuration = 2500
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LegendItem(color = BangladeshRed, label = "Capital")
                        LegendItem(color = BangladeshRed.copy(alpha = 0.8f), label = "Major Cities")
                    }
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BangladeshGreen.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "About Bangladesh",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BangladeshGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow("Capital", "Dhaka")
                    InfoRow("Population", "~170 million")
                    InfoRow("Area", "147,570 km²")
                    InfoRow("Language", "Bengali (Bangla)")
                    InfoRow("Currency", "Taka (৳)")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MapsScreenPreview() {
    MapsScreen()
}


