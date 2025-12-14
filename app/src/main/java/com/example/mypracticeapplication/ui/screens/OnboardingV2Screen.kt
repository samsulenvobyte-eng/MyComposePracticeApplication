package com.example.mypracticeapplication.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.mypracticeapplication.R
import kotlinx.coroutines.launch

// Gradient brush for text and borders
private val gradientBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFF554FF),
        Color(0xFF434AFF)
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)

// Data model for onboarding pages
data class OnboardingPageV2(
    @DrawableRes val beforeImage: Int,
    @DrawableRes val afterImage: Int,
    val beforeSize: String,
    val afterSize: String,
    val title: AnnotatedString,
    val subtitle: String,
    val buttonText: String
)

// Sample onboarding data
private val onboardingPages = listOf(
    OnboardingPageV2(
        beforeImage = R.drawable.img_girl,
        afterImage = R.drawable.img_girl,
        beforeSize = "4.1 MB",
        afterSize = "240 KB",
        title = buildAnnotatedString {
            append("Smart ")
            withStyle(SpanStyle(brush = gradientBrush, fontStyle = FontStyle.Italic)) {
                append("Compression, Stunning")
            }
            append(" Quality")
        },
        subtitle = "Compress photos up to 98% smaller while keeping clarity.",
        buttonText = "Get Started"
    ),
    OnboardingPageV2(
        beforeImage = R.drawable.img_girl,
        afterImage = R.drawable.img_girl,
        beforeSize = "5.2 MB",
        afterSize = "180 KB",
        title = buildAnnotatedString {
            append("Fit Photo for Any ")
            withStyle(SpanStyle(brush = gradientBrush, fontStyle = FontStyle.Italic)) {
                append("Social Platform")
            }
        },
        subtitle = "Perfect size for Instagram, TikTok, Facebook, Pinterest.",
        buttonText = "Continue"
    ),
    OnboardingPageV2(
        beforeImage = R.drawable.img_girl,
        afterImage = R.drawable.img_girl,
        beforeSize = "3.8 MB",
        afterSize = "95 KB",
        title = buildAnnotatedString {
            append("Convert ")
            withStyle(SpanStyle(brush = gradientBrush, fontStyle = FontStyle.Italic)) {
                append("Formats")
            }
            append(" with Ease")
        },
        subtitle = "Convert between JPG, PNG, WebP, HEIC, AVIF, PDF, and more.",
        buttonText = "Continue"
    )
)

@Composable
fun OnboardingV2Screen(
    onNavigateBack: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        // Image pager section - takes 55% of screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val pageData = onboardingPages[page]
                OnboardingImageContainer(
                    beforeImage = pageData.beforeImage,
                    afterImage = pageData.afterImage,
                    beforeSize = pageData.beforeSize,
                    afterSize = pageData.afterSize
                )
            }
        }

        // Page indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(onboardingPages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(
                            width = if (isSelected) 24.dp else 8.dp,
                            height = 8.dp
                        )
                        .background(
                            color = if (isSelected) Color(0xFFF554FF) else Color(0xFFE1EBFF),
                            shape = CircleShape
                        )
                )
            }
        }

        // Text content section - takes 30% of screen
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.30f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val currentPage = onboardingPages[pagerState.currentPage]

            Text(
                text = currentPage.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 1.3.em
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentPage.subtitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray
                )
            )
        }

        // Button section - takes 15% of screen
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.15f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val currentPage = onboardingPages[pagerState.currentPage]

            Button(
                onClick = {
                    if (pagerState.currentPage < onboardingPages.size - 1) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onComplete()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(brush = gradientBrush, shape = RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = currentPage.buttonText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
private fun OnboardingImageContainer(
    @DrawableRes beforeImage: Int,
    @DrawableRes afterImage: Int,
    beforeSize: String,
    afterSize: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFEF1FF))
    ) {
        // Background decorative icon
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_compress),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .scale(1.5f),
            colorFilter = ColorFilter.tint(Color(0xFFF554FF)),
            alpha = 0.08f
        )

        // Image comparison layout using BoxWithConstraints for adaptive sizing
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val containerWidth = maxWidth
            val containerHeight = maxHeight

            // Aspect ratio 50:63 (width:height)
            val aspectRatio = 50f / 63f

            // Before image: 60% of container width
            val beforeWidth = containerWidth * 0.60f
            val beforeHeight = beforeWidth / aspectRatio

            // After image: 50% of container width
            val afterWidth = containerWidth * 0.50f
            val afterHeight = afterWidth / aspectRatio

            val cornerRadius = 12.dp

            // Before image (larger, top-left)
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .zIndex(1f)
            ) {
                // "Before" label
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 6.dp)
                        .background(Color.White, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Before",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    )
                }

                // Before image
                Image(
                    painter = painterResource(beforeImage),
                    contentDescription = "Before image",
                    modifier = Modifier
                        .width(beforeWidth)
                        .height(beforeHeight)
                        .shadow(8.dp, RoundedCornerShape(cornerRadius))
                        .clip(RoundedCornerShape(cornerRadius)),
                    contentScale = ContentScale.Crop
                )

                // Size label
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .background(Color.White, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = beforeSize,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        )
                    )
                }
            }

            // After image (smaller, bottom-right with gradient border)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .zIndex(2f),
                horizontalAlignment = Alignment.End
            ) {
                // "After" label with gradient background
                Box(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .background(gradientBrush, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "After",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )
                }

                // After image with gradient border
                Box(
                    modifier = Modifier
                        .width(afterWidth)
                        .height(afterHeight)
                        .shadow(12.dp, RoundedCornerShape(cornerRadius))
                        .border(
                            width = 3.dp,
                            brush = gradientBrush,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                        .clip(RoundedCornerShape(cornerRadius))
                ) {
                    Image(
                        painter = painterResource(afterImage),
                        contentDescription = "After image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Size label with gradient background
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(gradientBrush, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = afterSize,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
private fun OnboardingV2ScreenPreview() {
    OnboardingV2Screen()
}
