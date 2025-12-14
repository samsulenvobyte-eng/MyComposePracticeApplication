package com.example.mypracticeapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.theme.MyPracticeApplicationTheme

// Data class for onboarding pages
data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

@Composable
fun HealthOnboardingScreen(
    onSkip: () -> Unit = {},
    onGetStarted: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    // Sample onboarding pages - replace with your actual images
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.onboarding_food_1,
            title = "Bring Happiness Local Food with Freshgo",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard"
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding_food_2,
            title = "Fresh Ingredients Delivered Daily",
            description = "Get fresh, locally sourced ingredients delivered right to your doorstep every day."
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding_food_3,
            title = "Support Local Farmers",
            description = "Every purchase helps support local farmers and promotes sustainable agriculture."
        )
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Image Slider with HorizontalPager
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Image(
                    painter = painterResource(id = pages[page].imageRes),
                    contentDescription = "Onboarding Image ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Page Indicators
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pages.size) { index ->
                    PageIndicator(
                        isSelected = pagerState.currentPage == index
                    )
                    if (index < pages.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = pages[pagerState.currentPage].title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E),
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = pages[pagerState.currentPage].description,
                fontSize = 15.sp,
                color = Color(0xFF6B6B6B),
                textAlign = TextAlign.Start,
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Skip Tour Button
                Button(
                    onClick = onSkip,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E2A3A),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text(
                        text = "Skip tour",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Get Started Button
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text(
                        text = "Get started",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Link
            Text(
                text = buildAnnotatedString {
                    append("Already have account? ")
                    withStyle(style = SpanStyle(color = Color(0xFF4CAF50), fontWeight = FontWeight.SemiBold)) {
                        append("Login")
                    }
                },
                fontSize = 14.sp,
                color = Color(0xFF6B6B6B),
                modifier = Modifier.clickable { onLogin() }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PageIndicator(
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .size(
                width = if (isSelected) 24.dp else 8.dp,
                height = 8.dp
            )
            .clip(CircleShape)
            .background(
                if (isSelected) Color(0xFF4CAF50)
                else Color.White.copy(alpha = 0.5f)
            )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HealthOnboardingScreenPreview() {
    MyPracticeApplicationTheme {
        HealthOnboardingScreen()
    }
}
