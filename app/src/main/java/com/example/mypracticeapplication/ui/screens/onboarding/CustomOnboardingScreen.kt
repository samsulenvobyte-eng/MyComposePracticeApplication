package com.example.mypracticeapplication.ui.screens.onboarding

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.mypracticeapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//todo: fix weight mismanagement
//todo: Make the screen scrollable for bigger fonts

data class OnBoardingModel(
    @param:DrawableRes val image: Int,
    val title: AnnotatedString,
    val subtitle: String,
    val buttonText: String
)

val brush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFF554FF),
        Color(0xFF434AFF)
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)
val onBoardingList = listOf(
    OnBoardingModel(
        R.drawable.img_slider_foreground,
        buildAnnotatedString {
            append("Smart")
            append(" ")
            withStyle(
                style = SpanStyle(brush)
            ) {
                append("Compression, Stunning")
            }
            append(" ")
            append("Quality")
        },
        "Compress photos up to 98% smaller while keeping clarity.",
        "Get Started"
    ),
    OnBoardingModel(
        R.drawable.img_slider_foreground_2,
        buildAnnotatedString {
            append("Fit Photo for Any")
            append(" ")
            withStyle(
                style = SpanStyle(brush)
            ) {
                append("Social Platform")
            }
        },
        "Perfect size for Instagram, TikTok, Facebook, Pinterest.",
        "Continue"
    ),

    OnBoardingModel(
        R.drawable.img_slider_foreground_3,
        buildAnnotatedString {
            append("Convert")
            append(" ")
            withStyle(
                style = SpanStyle(brush)
            ) {
                append("Formats")
            }
            append(" ")
            append("with Ease")
        },
        "Convert between JPG, PNG, WebP, HEIC, AVIF, PDF, and more",
        "Continue"
    )
)



@Composable
fun CustomOnboardingScreen(
    onNavigateToResult: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val pageCount = onBoardingList.size
    val state = rememberPagerState(pageCount = { pageCount })
    val title = onBoardingList[state.currentPage].title
    val subtitle = onBoardingList[state.currentPage].subtitle
    val buttonText = onBoardingList[state.currentPage].buttonText
    val scope = rememberCoroutineScope()

    OnBoardingScreenContent(
        isLandscape,
        title,
        subtitle,
        buttonText,
        state,
        pageCount,
        buttonClick = {
            scope.launch {
                if (state.currentPage < state.pageCount - 1) {
                    state.animateScrollToPage(state.currentPage + 1)
                } else {
                    onNavigateToResult()
                }
            }
        })
}
@Composable
fun OnBoardingScreenContent(
    isLandscape: Boolean,
    title: AnnotatedString,
    subtitle: String,
    buttonText: String,
    state: PagerState,
    pageCount: Int,
    buttonClick: () -> Unit
) {

    if (isLandscape) {
        LandscapeMode(state, pageCount, title, subtitle, buttonText, buttonClick)

    } else {

        PortraitMode(state, pageCount, title, subtitle, buttonText, buttonClick)
    }
}


@Composable
fun LandscapeMode(
    state: PagerState,
    pageCount: Int,
    title: AnnotatedString,
    subtitle: String,
    buttonText: String,
    buttonClick: () -> Unit,
    modifier: Modifier = Modifier

) {

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding(),

        ) {

        VerticalPager(state, modifier = Modifier.weight(0.5f)) { page ->

            val contentPadding = PaddingValues(top = 16.dp, start = 16.dp, bottom = 16.dp)
            val pageImage = onBoardingList[page].image

            Box(
                modifier = Modifier
                    .width(400.dp)
                    .fillMaxHeight()
                    .padding(contentPadding)
                    .background(color = Color(0xFFFEF1FF), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    modifier = Modifier
                        .matchParentSize()
                        .scale(1.5f),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_compress),
                    colorFilter = ColorFilter.tint(Color(0xFFF554FF)),
                    contentDescription = null,
                    alpha = 0.1f
                )


                Image(
                    painter = painterResource(pageImage),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            repeat(pageCount) {
                val color = if (state.currentPage == it) Color(0xFFF554FF) else Color(0xFFE1EBFF)
                val height = if (state.currentPage == it) 27.dp else 8.dp

                Box(
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                        .background(color = color, shape = CircleShape)
                        .size(width = 8.dp, height = height)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f)
                .padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 55.dp, start = 16.dp, end = 16.dp),
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.W700,
                    fontSize = 24.sp, lineHeight = 1.4.em
                )
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 11.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = buttonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(brush, shape = CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = buttonText, modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(55.dp))
        }
    }

}

@Composable
fun PortraitMode(
    state: PagerState,
    pageCount: Int,
    title: AnnotatedString,
    subtitle: String,
    buttonText: String,
    buttonClick: () -> Unit,
    modifier: Modifier = Modifier

) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),

        ) {

        HorizontalPager(state, modifier = Modifier.weight(0.6f)) { page ->

            val pageImage = onBoardingList[page].image

            val contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
                    .background(color = Color(0xFFFEF1FF), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    modifier = Modifier
                        .matchParentSize()
                        .scale(1.5f),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_compress),
                    colorFilter = ColorFilter.tint(Color(0xFFF554FF)),
                    contentDescription = null,
                    alpha = 0.09f
                )

                Image(
                    painter = painterResource(pageImage),
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .padding(16.dp),

                    contentDescription = null
                )
            }
        }

        TextContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f),
            title = title,
            subtitle = subtitle,
            pageCount = pageCount,
            state = state
        )

        Button(
            onClick = buttonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .background(brush = brush, shape = RoundedCornerShape(50)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        ) {
            Text(
                text = buttonText, modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)

            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
private fun TextContent(
    title: AnnotatedString,
    subtitle: String,
    pageCount: Int,
    state: PagerState,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            repeat(pageCount) {
                val color = if (state.currentPage == it) Color(0xFFF554FF) else Color(0xFFE1EBFF)
                val width = if (state.currentPage == it) 27.dp else 8.dp

                Box(
                    modifier = Modifier
                        .padding(end = 3.dp)
                        .background(color = color, shape = CircleShape)
                        .size(width = width, height = 8.dp)
                )
            }
        }


        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 33.dp, start = 16.dp, end = 16.dp),
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W700,
                fontSize = 24.sp, lineHeight = 1.4.em
            )
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 11.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
private fun CustomOnboardingScreenPreview() {
    CustomOnboardingScreen()
}
