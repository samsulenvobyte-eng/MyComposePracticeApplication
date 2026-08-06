package com.example.mypracticeapplication.presentation.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

private val PrimaryColor = Color(0xFFFF6B6B)
private val SecondaryColor = Color(0xFF4ECDC4)

// ═══════════════════════════════════════════════════════════════════════════
// DONUT CHART COMPONENTS
// ═══════════════════════════════════════════════════════════════════════════

/**
 * Data class representing a single segment in the donut chart
 */
data class DonutChartSegment(
    val value: Float,
    val color: Color,
    val label: String = ""
)

/**
 * Animated Donut Chart - A reusable composable that displays an animated donut/pie chart
 */
@Composable
fun AnimatedDonutChart(
    segments: List<DonutChartSegment>,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    strokeWidth: Dp = 40.dp,
    animationDuration: Int = 1200,
    gapAngle: Float = 3f,
    showLabels: Boolean = true,
    labelTextStyle: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray
    ),
    startAngle: Float = -90f
) {
    val textMeasurer = rememberTextMeasurer()
    val animationProgress = remember { Animatable(0f) }
    
    LaunchedEffect(segments) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing
            )
        )
    }
    
    val totalValue = remember(segments) { segments.sumOf { it.value.toDouble() }.toFloat() }
    val sweepAngles = remember(segments, gapAngle) {
        segments.map { segment ->
            (segment.value / totalValue) * (360f - (segments.size * gapAngle))
        }
    }

    // Pre-measure labels to avoid expensive measurement in the draw loop
    val labelLayoutResults = remember(segments, labelTextStyle, textMeasurer) {
        segments.map { segment ->
            val displayValue = if (segment.value == segment.value.toLong().toFloat()) {
                segment.value.toLong().toString()
            } else {
                String.format("%.1f", segment.value)
            }
            textMeasurer.measure(text = displayValue, style = labelTextStyle)
        }
    }
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = this.size
            val radius = (canvasSize.minDimension - strokeWidth.toPx()) / 2
            val center = Offset(canvasSize.width / 2, canvasSize.height / 2)
            
            var currentStartAngle = startAngle
            
            // Optimization: Use standard loop to avoid Iterator allocations per frame
            for (i in 0 until segments.size) {
                val segment = segments[i]
                val sweepAngle = sweepAngles[i] * animationProgress.value
                
                drawArc(
                    color = segment.color,
                    startAngle = currentStartAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
                )
                
                if (showLabels && animationProgress.value > 0.5f) {
                    val labelAngle = currentStartAngle + (sweepAngle / 2)
                    val labelRadius = radius + strokeWidth.toPx() / 2
                    val labelX = center.x + labelRadius * cos(Math.toRadians(labelAngle.toDouble())).toFloat()
                    val labelY = center.y + labelRadius * sin(Math.toRadians(labelAngle.toDouble())).toFloat()
                    
                    val textLayoutResult = labelLayoutResults[i]
                    
                    if (sweepAngles[i] > 15f) {
                        drawText(
                            textLayoutResult = textLayoutResult,
                            topLeft = Offset(
                                labelX - textLayoutResult.size.width / 2,
                                labelY - textLayoutResult.size.height / 2
                            )
                        )
                    }
                }
                
                currentStartAngle += sweepAngle + gapAngle
            }
        }
    }
}

@Composable
fun AnimatedDonutChartWithCenter(
    segments: List<DonutChartSegment>,
    centerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    strokeWidth: Dp = 40.dp,
    animationDuration: Int = 1200,
    gapAngle: Float = 3f,
    showLabels: Boolean = true,
    startAngle: Float = -90f
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        AnimatedDonutChart(
            segments = segments,
            size = size,
            strokeWidth = strokeWidth,
            animationDuration = animationDuration,
            gapAngle = gapAngle,
            showLabels = showLabels,
            startAngle = startAngle
        )
        centerContent()
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// LINE CHART COMPONENTS
// ═══════════════════════════════════════════════════════════════════════════

/**
 * Data class representing a data point in the line chart
 */
data class LineChartDataPoint(
    val value: Float,
    val label: String = ""
)

/**
 * Data class for stat items shown below the chart
 */
data class ChartStatItem(
    val value: String,
    val label: String,
    val indicatorColor: Color = Color.Transparent
)

/**
 * Animated Line Chart - A reusable composable that displays an animated line chart
 * Uses smooth clip-based reveal animation with cubic bezier curves for ultra-smooth drawing
 *
 * @param dataPoints List of data points for the line
 * @param modifier Modifier for the chart
 * @param lineColor Color of the line
 * @param lineWidth Width of the line stroke
 * @param showGradientFill Whether to show gradient fill below the line
 * @param gradientColors Colors for the gradient fill (top to bottom)
 * @param showDataPoints Whether to show dots at data points
 * @param dataPointRadius Radius of data point dots
 * @param animationDuration Duration of the animation in milliseconds
 * @param showLabels Whether to show x-axis labels
 * @param labelTextStyle Text style for labels
 */
@Composable
fun AnimatedLineChart(
    dataPoints: List<LineChartDataPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF26C6DA),
    lineWidth: Dp = 2.dp,
    showGradientFill: Boolean = true,
    gradientColors: List<Color> = listOf(
        Color(0xFF26C6DA).copy(alpha = 0.3f),
        Color(0xFF26C6DA).copy(alpha = 0.0f)
    ),
    showDataPoints: Boolean = false,
    dataPointRadius: Dp = 4.dp,
    animationDuration: Int = 1500,
    showLabels: Boolean = true,
    labelTextStyle: TextStyle = TextStyle(
        fontSize = 11.sp,
        color = Color.Gray
    )
) {
    val textMeasurer = rememberTextMeasurer()
    
    // Main progress animation with spring for bouncy effect
    val animationProgress = remember { Animatable(0f) }
    
    // Vertical bounce animation for the line itself
    val verticalBounce = remember { Animatable(0f) }
    
    LaunchedEffect(dataPoints) {
        // Reset animations
        animationProgress.snapTo(0f)
        verticalBounce.snapTo(1f)
        
        // Start both animations
        // Horizontal reveal with spring bounce - slower stiffness
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessVeryLow // ~50f - much slower
            )
        )
    }
    
    // Separate effect for vertical bounce to run concurrently
    LaunchedEffect(dataPoints) {
        verticalBounce.snapTo(1.15f) // Start slightly lower (values are inverted in Y)
        verticalBounce.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = 30f // Even slower for a gentle vertical bounce
            )
        )
    }
    
    if (dataPoints.isEmpty()) return
    
    val minValue = remember(dataPoints) { dataPoints.minOf { it.value } }
    val maxValue = remember(dataPoints) { dataPoints.maxOf { it.value } }
    val valueRange = remember(minValue, maxValue) {
        if (maxValue - minValue == 0f) 1f else maxValue - minValue
    }

    // Pre-measure x-axis labels to avoid redundant work in Canvas
    val labelLayoutResults = remember(dataPoints, labelTextStyle, textMeasurer) {
        dataPoints.map { dataPoint ->
            if (dataPoint.label.isNotEmpty()) {
                textMeasurer.measure(text = dataPoint.label, style = labelTextStyle)
            } else null
        }
    }

    // Optimization: Pre-allocate paths and Y-coordinate array to avoid per-frame allocations
    val linePath = remember { Path() }
    val fillPath = remember { Path() }
    val yCoordinates = remember(dataPoints.size) { FloatArray(dataPoints.size) }
    
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val labelHeight = if (showLabels) 30.dp.toPx() else 0f
        val chartHeight = canvasHeight - labelHeight
        val chartPadding = 16.dp.toPx()
        
        val pointSpacing = (canvasWidth - chartPadding * 2) / (dataPoints.size - 1).coerceAtLeast(1)
        
        // Calculate all Y coordinates with vertical bounce applied
        // Optimization: Use primitive FloatArray and standard loop to avoid Offset boxing and Iterator overhead
        val baselineY = chartHeight / 2
        for (i in 0 until dataPoints.size) {
            val normalizedValue = (dataPoints[i].value - minValue) / valueRange
            val rawY = chartHeight - (normalizedValue * (chartHeight - chartPadding * 2)) - chartPadding
            
            // Apply vertical bounce: points bounce from below
            val distanceFromBaseline = rawY - baselineY
            yCoordinates[i] = baselineY + (distanceFromBaseline * verticalBounce.value)
        }
        
        if (dataPoints.size >= 2) {
            // Optimization: Reset and reuse existing Path objects
            linePath.reset()
            linePath.moveTo(chartPadding, yCoordinates[0])

            for (i in 1 until dataPoints.size) {
                // Optimization: Calculate X values inline to avoid intermediate List<Offset>
                val prevX = chartPadding + (i - 1) * pointSpacing
                val currX = chartPadding + i * pointSpacing
                val nextX = if (i < dataPoints.size - 1) chartPadding + (i + 1) * pointSpacing else currX
                val prevPrevX = if (i > 1) chartPadding + (i - 2) * pointSpacing else prevX
                
                val prevY = yCoordinates[i - 1]
                val currY = yCoordinates[i]
                val nextY = if (i < dataPoints.size - 1) yCoordinates[i + 1] else currY
                val prevPrevY = if (i > 1) yCoordinates[i - 2] else prevY

                // Calculate control points for smooth cubic bezier
                val tension = 0.3f

                // Control point 1: based on previous segment direction
                val cp1x = prevX + (currX - prevPrevX) * tension
                val cp1y = prevY + (currY - prevPrevY) * tension

                // Control point 2: based on next segment direction
                val cp2x = currX - (nextX - prevX) * tension
                val cp2y = currY - (nextY - prevY) * tension

                linePath.cubicTo(cp1x, cp1y, cp2x, cp2y, currX, currY)
            }
            
            // Create the fill path (closed shape below the line)
            fillPath.reset()
            fillPath.addPath(linePath)
            fillPath.lineTo(chartPadding + (dataPoints.size - 1) * pointSpacing, chartHeight)
            fillPath.lineTo(chartPadding, chartHeight)
            fillPath.close()
            
            // Calculate the clip width based on animation progress
            val clipWidth = chartPadding + (canvasWidth - chartPadding) * animationProgress.value
            
            // Use clipRect to smoothly reveal the chart from left to right
            clipRect(
                left = 0f,
                top = 0f,
                right = clipWidth,
                bottom = canvasHeight
            ) {
                // Draw gradient fill
                if (showGradientFill) {
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(gradientColors)
                    )
                }
                
                // Draw the line
                drawPath(
                    path = linePath,
                    color = lineColor,
                    style = Stroke(
                        width = lineWidth.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                
                // Draw data points with fade-in effect
                if (showDataPoints) {
                    // Optimization: Use standard loop to avoid Iterator allocations per frame
                    for (i in 0 until dataPoints.size) {
                        val pointProgress = (i.toFloat() / (dataPoints.size - 1))
                        if (animationProgress.value >= pointProgress) {
                            val pointAlpha = ((animationProgress.value - pointProgress) * 5f).coerceIn(0f, 1f)
                            drawCircle(
                                color = lineColor.copy(alpha = pointAlpha),
                                radius = dataPointRadius.toPx() * pointAlpha,
                                center = Offset(chartPadding + i * pointSpacing, yCoordinates[i])
                            )
                        }
                    }
                }
            }
        }
        
        // Draw x-axis labels (outside clip rect so they're always visible)
        if (showLabels) {
            // Optimization: Use standard loop to avoid Iterator allocations per frame
            for (i in 0 until dataPoints.size) {
                val textLayoutResult = labelLayoutResults[i]
                if (textLayoutResult != null) {
                    val x = chartPadding + i * pointSpacing
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            x - textLayoutResult.size.width / 2,
                            chartHeight + 8.dp.toPx()
                        )
                    )
                }
            }
        }
    }
}

/**
 * Supply Chart Card - A complete card component matching the reference design
 */
@Composable
fun SupplyChartCard(
    title: String,
    mainValue: String,
    isDown: Boolean = true,
    dataPoints: List<LineChartDataPoint>,
    stats: List<ChartStatItem>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF26C6DA),
    cardBackground: Color = Color.White
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = mainValue,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isDown) "Down" else "Up",
                            tint = Color(0xFFFFA726),
                            modifier = Modifier
                                .size(24.dp)
                                .then(
                                    if (!isDown) Modifier else Modifier
                                )
                        )
                    }
                }
                
                // Right side indicators
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(width = 4.dp, height = 16.dp)
                                .background(
                                    color = when (index) {
                                        0 -> Color(0xFF26C6DA)
                                        1 -> Color(0xFF26C6DA).copy(alpha = 0.6f)
                                        else -> Color(0xFF26C6DA).copy(alpha = 0.3f)
                                    },
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Line Chart
            AnimatedLineChart(
                dataPoints = dataPoints,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                lineColor = lineColor,
                lineWidth = 2.dp,
                showGradientFill = true,
                animationDuration = 1500
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                stats.forEach { stat ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stat.value,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E)
                        )
                        Text(
                            text = stat.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// SCREEN AND DEMO COMPONENTS
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun AnimatedGraphsScreen(
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
                    text = "📊 Animated Graphs",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Chart and Graph Animations",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Content with demos
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Demo 1: Supply Line Chart Card
            SupplyChartCard(
                title = "Supply",
                mainValue = "2928",
                isDown = true,
                dataPoints = listOf(
                    LineChartDataPoint(45f, "May"),
                    LineChartDataPoint(52f, "Jun"),
                    LineChartDataPoint(48f, "Jul"),
                    LineChartDataPoint(55f, "Aug"),
                    LineChartDataPoint(50f, "Sep"),
                    LineChartDataPoint(65f, "Oct")
                ),
                stats = listOf(
                    ChartStatItem("3852", "Warehouse"),
                    ChartStatItem("1420", "Transport"),
                    ChartStatItem("2864", "Retail")
                )
            )
            
            // Demo 2: Basic Donut Chart
            DemoCard(
                title = "Donut Chart",
                subtitle = "Animated segments with labels"
            ) {
                val segments = listOf(
                    DonutChartSegment(38f, Color(0xFFE57373), "Main"),
                    DonutChartSegment(10.9f, Color(0xFFFFE0B2), "Secondary"),
                    DonutChartSegment(7.4f, Color(0xFFCE93D8), "Tertiary"),
                    DonutChartSegment(4.1f, Color(0xFFF8BBD9), "Fourth"),
                    DonutChartSegment(2.7f, Color(0xFFFFCDD2), "Fifth")
                )
                
                AnimatedDonutChartWithCenter(
                    segments = segments,
                    size = 220.dp,
                    strokeWidth = 45.dp,
                    gapAngle = 4f,
                    centerContent = {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF333333)
                            )
                        ) {
                            Text(
                                text = "38",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                )
            }
            
            // Demo 3: Simple Line Chart
            DemoCard(
                title = "Line Chart",
                subtitle = "Smooth animated transitions"
            ) {
                AnimatedLineChart(
                    dataPoints = listOf(
                        LineChartDataPoint(30f, "Mon"),
                        LineChartDataPoint(50f, "Tue"),
                        LineChartDataPoint(40f, "Wed"),
                        LineChartDataPoint(70f, "Thu"),
                        LineChartDataPoint(55f, "Fri"),
                        LineChartDataPoint(80f, "Sat"),
                        LineChartDataPoint(65f, "Sun")
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    lineColor = Color(0xFF6366F1),
                    gradientColors = listOf(
                        Color(0xFF6366F1).copy(alpha = 0.4f),
                        Color(0xFF6366F1).copy(alpha = 0.0f)
                    ),
                    showDataPoints = true,
                    animationDuration = 1800
                )
            }
            
            // Demo 4: Thin ring donut
            DemoCard(
                title = "Progress Ring",
                subtitle = "Minimal style with center content"
            ) {
                val segments = listOf(
                    DonutChartSegment(60f, Color(0xFF00BCD4)),
                    DonutChartSegment(25f, Color(0xFFFF9800)),
                    DonutChartSegment(15f, Color(0xFF9C27B0))
                )
                
                AnimatedDonutChartWithCenter(
                    segments = segments,
                    size = 160.dp,
                    strokeWidth = 20.dp,
                    gapAngle = 6f,
                    showLabels = false,
                    centerContent = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "60%",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00BCD4)
                            )
                            Text(
                                text = "Complete",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
            Spacer(modifier = Modifier.height(20.dp))
            content()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnimatedGraphsScreenPreview() {
    AnimatedGraphsScreen()
}

@Preview(showBackground = true)
@Composable
private fun SupplyChartCardPreview() {
    SupplyChartCard(
        title = "Supply",
        mainValue = "2928",
        isDown = true,
        dataPoints = listOf(
            LineChartDataPoint(45f, "May"),
            LineChartDataPoint(52f, "Jun"),
            LineChartDataPoint(48f, "Jul"),
            LineChartDataPoint(55f, "Aug"),
            LineChartDataPoint(50f, "Sep"),
            LineChartDataPoint(65f, "Oct")
        ),
        stats = listOf(
            ChartStatItem("3852", "Warehouse"),
            ChartStatItem("1420", "Transport"),
            ChartStatItem("2864", "Retail")
        ),
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun AnimatedLineChartPreview() {
    Box(
        modifier = Modifier
            .size(300.dp, 150.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {
        AnimatedLineChart(
            dataPoints = listOf(
                LineChartDataPoint(30f, "Mon"),
                LineChartDataPoint(50f, "Tue"),
                LineChartDataPoint(40f, "Wed"),
                LineChartDataPoint(70f, "Thu"),
                LineChartDataPoint(55f, "Fri")
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}


