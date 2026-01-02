package com.example.mypracticeapplication.ui.screens.image

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasScreen(
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Canvas Examples",
                        fontWeight = FontWeight.Bold,
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
                    containerColor = Color(0xFF667eea)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2)
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Basic Shapes Example
            CanvasCard(title = "Basic Shapes") {
                BasicShapesCanvas()
            }

            // Lines and Strokes Example
            CanvasCard(title = "Lines & Strokes") {
                LinesAndStrokesCanvas()
            }

            // Gradient Shapes Example
            CanvasCard(title = "Gradient Shapes") {
                GradientShapesCanvas()
            }

            // Arc and Pie Chart Example
            CanvasCard(title = "Arcs & Pie Chart") {
                ArcAndPieChartCanvas()
            }

            // Path Drawing Example
            CanvasCard(title = "Custom Path - Star") {
                StarPathCanvas()
            }

            // Bezier Curve Example
            CanvasCard(title = "Bezier Curves") {
                BezierCurveCanvas()
            }

            // Grid Pattern Example
            CanvasCard(title = "Grid Pattern") {
                GridPatternCanvas()
            }

            // Rotating Shape Example
            CanvasCard(title = "Rotated Rectangles") {
                RotatedShapesCanvas()
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CanvasCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = Color(0xFFF8F9FA),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

@Composable
private fun BasicShapesCanvas() {
    Canvas(
        modifier = Modifier.size(180.dp)
    ) {
        // Circle
        drawCircle(
            color = Color(0xFF667eea),
            radius = 40f,
            center = Offset(60f, 80f)
        )

        // Rectangle
        drawRect(
            color = Color(0xFF764ba2),
            topLeft = Offset(120f, 40f),
            size = Size(80f, 80f)
        )

        // Rounded Rectangle
        drawRoundRect(
            color = Color(0xFFf093fb),
            topLeft = Offset(220f, 40f),
            size = Size(80f, 80f),
            cornerRadius = CornerRadius(16f, 16f)
        )

        // Oval
        drawOval(
            color = Color(0xFF4facfe),
            topLeft = Offset(60f, 140f),
            size = Size(100f, 60f)
        )

        // Another Circle with different position
        drawCircle(
            color = Color(0xFFfa709a),
            radius = 30f,
            center = Offset(260f, 170f)
        )
    }
}

@Composable
private fun LinesAndStrokesCanvas() {
    Canvas(
        modifier = Modifier.size(180.dp)
    ) {
        // Simple line
        drawLine(
            color = Color(0xFF667eea),
            start = Offset(20f, 40f),
            end = Offset(300f, 40f),
            strokeWidth = 4f
        )

        // Dashed-like thick line
        drawLine(
            color = Color(0xFF764ba2),
            start = Offset(20f, 80f),
            end = Offset(300f, 80f),
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )

        // Multiple connected lines
        val points = listOf(
            Offset(20f, 130f),
            Offset(80f, 180f),
            Offset(140f, 120f),
            Offset(200f, 170f),
            Offset(260f, 110f),
            Offset(300f, 160f)
        )
        
        for (i in 0 until points.size - 1) {
            drawLine(
                color = Color(0xFFf093fb),
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = Color(0xFF4facfe),
                radius = 8f,
                center = point
            )
        }
    }
}

@Composable
private fun GradientShapesCanvas() {
    Canvas(
        modifier = Modifier.size(180.dp)
    ) {
        // Horizontal gradient rectangle
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
            ),
            topLeft = Offset(20f, 30f),
            size = Size(120f, 60f)
        )

        // Vertical gradient circle
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFf093fb), Color(0xFFf5576c))
            ),
            radius = 45f,
            center = Offset(240f, 60f)
        )

        // Radial gradient oval
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF4facfe), Color(0xFF00f2fe)),
                center = Offset(80f, 160f),
                radius = 80f
            ),
            topLeft = Offset(20f, 120f),
            size = Size(120f, 80f)
        )

        // Sweep gradient rounded rectangle
        drawRoundRect(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFFfa709a),
                    Color(0xFFfee140),
                    Color(0xFF43e97b),
                    Color(0xFF38f9d7),
                    Color(0xFF4facfe),
                    Color(0xFFfa709a)
                ),
                center = Offset(220f, 160f)
            ),
            topLeft = Offset(160f, 110f),
            size = Size(120f, 100f),
            cornerRadius = CornerRadius(20f)
        )
    }
}

@Composable
private fun ArcAndPieChartCanvas() {
    Canvas(
        modifier = Modifier.size(180.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = 80f

        // Pie chart segments
        val segments = listOf(
            Pair(0f, 90f) to Color(0xFF667eea),
            Pair(90f, 120f) to Color(0xFF764ba2),
            Pair(210f, 80f) to Color(0xFFf093fb),
            Pair(290f, 70f) to Color(0xFF4facfe)
        )

        segments.forEach { (angles, color) ->
            drawArc(
                color = color,
                startAngle = angles.first,
                sweepAngle = angles.second,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
        }

        // Center circle for donut effect
        drawCircle(
            color = Color(0xFFF8F9FA),
            radius = 40f,
            center = center
        )
    }
}

@Composable
private fun StarPathCanvas() {
    Canvas(
        modifier = Modifier.size(180.dp)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val outerRadius = 70f
        val innerRadius = 35f
        val points = 5

        val path = Path()
        
        for (i in 0 until points * 2) {
            val radius = if (i % 2 == 0) outerRadius else innerRadius
            val angle = Math.toRadians((i * 360.0 / (points * 2)) - 90)
            val x = centerX + (radius * cos(angle)).toFloat()
            val y = centerY + (radius * sin(angle)).toFloat()
            
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()

        // Fill star
        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFfee140), Color(0xFFfa709a))
            )
        )

        // Stroke star
        drawPath(
            path = path,
            color = Color(0xFFf5576c),
            style = Stroke(
                width = 3f,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
private fun BezierCurveCanvas() {
    Canvas(
        modifier = Modifier.size(180.dp)
    ) {
        // Quadratic Bezier curve
        val quadPath = Path().apply {
            moveTo(20f, 150f)
            quadraticTo(160f, 20f, 300f, 150f)
        }
        
        drawPath(
            path = quadPath,
            color = Color(0xFF667eea),
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )

        // Cubic Bezier curve
        val cubicPath = Path().apply {
            moveTo(20f, 180f)
            cubicTo(80f, 50f, 240f, 250f, 300f, 100f)
        }
        
        drawPath(
            path = cubicPath,
            color = Color(0xFFf093fb),
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )

        // Control points visualization
        val controlPoints = listOf(
            Offset(160f, 20f),   // Quad control
            Offset(80f, 50f),    // Cubic control 1
            Offset(240f, 250f)   // Cubic control 2
        )
        
        controlPoints.forEach { point ->
            drawCircle(
                color = Color(0xFFfa709a),
                radius = 6f,
                center = point
            )
        }
    }
}

@Composable
private fun GridPatternCanvas() {
    Canvas(
        modifier = Modifier.size(180.dp)
    ) {
        val cellSize = 40f
        val cols = (size.width / cellSize).toInt()
        val rows = (size.height / cellSize).toInt()

        // Draw grid lines
        for (i in 0..cols) {
            drawLine(
                color = Color(0xFFE0E0E0),
                start = Offset(i * cellSize, 0f),
                end = Offset(i * cellSize, size.height),
                strokeWidth = 1f
            )
        }
        
        for (i in 0..rows) {
            drawLine(
                color = Color(0xFFE0E0E0),
                start = Offset(0f, i * cellSize),
                end = Offset(size.width, i * cellSize),
                strokeWidth = 1f
            )
        }

        // Draw colored cells in a pattern
        val colors = listOf(
            Color(0xFF667eea),
            Color(0xFF764ba2),
            Color(0xFFf093fb),
            Color(0xFF4facfe)
        )

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if ((row + col) % 3 == 0) {
                    drawRect(
                        color = colors[(row + col) % colors.size].copy(alpha = 0.5f),
                        topLeft = Offset(col * cellSize, row * cellSize),
                        size = Size(cellSize, cellSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun RotatedShapesCanvas() {
    Canvas(
        modifier = Modifier.size(180.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val rectSize = Size(100f, 40f)

        val colors = listOf(
            Color(0xFF667eea),
            Color(0xFF764ba2),
            Color(0xFFf093fb),
            Color(0xFF4facfe),
            Color(0xFFfa709a),
            Color(0xFFfee140)
        )

        for (i in 0 until 6) {
            rotate(
                degrees = i * 30f,
                pivot = center
            ) {
                drawRoundRect(
                    color = colors[i].copy(alpha = 0.7f),
                    topLeft = Offset(center.x - rectSize.width / 2, center.y - rectSize.height / 2),
                    size = rectSize,
                    cornerRadius = CornerRadius(8f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CanvasScreenPreview() {
    CanvasScreen()
}
