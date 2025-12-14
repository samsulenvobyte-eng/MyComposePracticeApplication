package com.example.mypracticeapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.theme.MyPracticeApplicationTheme

// Data class for compression slides
data class CompressionSlide(
    val imageRes: Int,
    val beforeSize: String,
    val afterSize: String
)

@Composable
fun CompressOnboardingScreen(
    onGetStarted: () -> Unit = {}
) {
    val slides = listOf(
        CompressionSlide(
            imageRes = R.drawable.compress_before_1,
            beforeSize = "4.1 MB",
            afterSize = "240 KB"
        ),
        CompressionSlide(
            imageRes = R.drawable.compress_before_2,
            beforeSize = "3.8 MB",
            afterSize = "185 KB"
        ),
        CompressionSlide(
            imageRes = R.drawable.compress_before_3,
            beforeSize = "5.2 MB",
            afterSize = "320 KB"
        )
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { slides.size }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFDF4F9),
                        Color(0xFFF8E8F3),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Image Comparison Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) { page ->
                ImageComparisonSlide(slide = slides[page])
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Page Indicators
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(slides.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(
                                width = if (pagerState.currentPage == index) 24.dp else 8.dp,
                                height = 8.dp
                            )
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (pagerState.currentPage == index)
                                    Color(0xFF4B7BF5)
                                else
                                    Color(0xFFD1D5DB)
                            )
                    )
                    if (index < slides.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title with colored words
            Text(
                text = buildAnnotatedString {
                    append("Smart ")
                    withStyle(style = SpanStyle(color = Color(0xFF4B7BF5))) {
                        append("Compression")
                    }
                    append(",")
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFFE040A0))) {
                        append("Stunning")
                    }
                    append(" Quality")
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "Compress photos up to 98% smaller\nwhile keeping clarity.",
                fontSize = 15.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Get Started Button with gradient
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(28.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF4B7BF5),
                                    Color(0xFF7C5BF5),
                                    Color(0xFFE040A0)
                                )
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ImageComparisonSlide(slide: CompressionSlide) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp),
        contentAlignment = Alignment.Center
    ) {
        // Before Image (larger, at the back-left)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 0.dp, y = 20.dp)
                .zIndex(1f)
        ) {
            // Before image container
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(280.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color(0x40000000)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = slide.imageRes),
                    contentDescription = "Before image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Before badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-8).dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE5E7EB),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Before",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151)
                )
            }

            // File size label
            Text(
                text = slide.beforeSize,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 0.dp, y = 28.dp)
            )
        }

        // After Image (smaller, at the front-right, overlapping)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 0.dp, y = (-20).dp)
                .zIndex(2f)
        ) {
            // After image container with purple border
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(220.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color(0x60000000)
                    )
                    .border(
                        width = 3.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF7C5BF5),
                                Color(0xFFE040A0)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = slide.imageRes),
                    contentDescription = "After image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // After badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp, y = (-8).dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4B7BF5),
                                Color(0xFF7C5BF5)
                            )
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "After",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            // File size label with gradient background
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 16.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF7C5BF5),
                                Color(0xFFE040A0)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = slide.afterSize,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CompressOnboardingScreenPreview() {
    MyPracticeApplicationTheme {
        CompressOnboardingScreen()
    }
}
