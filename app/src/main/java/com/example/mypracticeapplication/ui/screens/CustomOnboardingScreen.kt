package com.example.mypracticeapplication.ui.screens

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
val onBoardingList = listOf<OnBoardingModel>(
    OnBoardingModel(
        R.drawable.img_smart_compress,
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
        R.drawable.img_fit_phone,
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
        R.drawable.img_convert_format,
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
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val pageCount = onBoardingList.size
    val state = rememberPagerState(pageCount = { pageCount })
    val image = onBoardingList.get(state.currentPage).image
    val title = onBoardingList.get(state.currentPage).title
    val subtitle = onBoardingList.get(state.currentPage).subtitle
    val buttonText = onBoardingList.get(state.currentPage).buttonText
    val scrope = rememberCoroutineScope()


    if (isLandscape) {
        LandscapeMode(state, pageCount, image, title, subtitle, buttonText, scrope)

    } else {

        PortraitMode(state, pageCount, image, title, subtitle, buttonText, scrope)
    }


}

@Composable
fun LandscapeMode(
    state: PagerState,
    pageCount: Int,
    image: Int,
    title: AnnotatedString,
    subtitle: String,
    buttonText: String,
    scrope: CoroutineScope,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding(),

        ) {

        VerticalPager(state) { page ->

            val contentPadding = PaddingValues(top = 16.dp, start = 16.dp, bottom = 16.dp)

            Box(
                modifier = Modifier
                    .width(400.dp)
                    .fillMaxHeight()
                    .padding(contentPadding)
                    .clip(RoundedCornerShape(8.dp))
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


                Box(
                    modifier = Modifier
                        .fillMaxSize()


                ) {
                    ComparePreviewBefore(modifier = Modifier.fillMaxHeight(0.4f))


                    ComparePreview(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                end = 32.dp, bottom = 32.dp
                            )
                    )
                }


            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            repeat(pageCount) { it ->
                5
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
                .padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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

            Button(
                onClick = { scrope.launch { state.animateScrollToPage(state.currentPage + 1) } },
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
            Spacer(modifier = Modifier.height(16.dp))


        }


    }

}

@Composable
fun PortraitMode(
    state: PagerState,
    pageCount: Int,
    image: Int,
    title: AnnotatedString,
    subtitle: String,
    buttonText: String,
    scrope: CoroutineScope,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding(),

        ) {

        HorizontalPager(state) { page ->

            val contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(contentPadding)
                    .clip(RoundedCornerShape(8.dp))
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


                Box(
                    modifier = Modifier
                        .fillMaxSize()


                ) {
                    ComparePreviewBefore(modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(
                            start = 32.dp, top = 32.dp
                        ))


                    ComparePreview(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                end = 32.dp, bottom = 32.dp
                            )
                    )
                }


            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            repeat(pageCount) { it ->
                5
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
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { scrope.launch { state.animateScrollToPage(state.currentPage + 1) } },
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
fun ComparePreviewBefore(modifier: Modifier = Modifier) {


    Column(modifier) {

        Box(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .align(Alignment.End)
                .background(Color.White, shape = RoundedCornerShape(8.dp))

        ) {
            AfterTexts("Before", Color.Black)
        }
        Image(
            painter = painterResource(R.drawable.img_girl),
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = null,
        )

        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .align(Alignment.Start)
                .background(Color.White, shape = RoundedCornerShape(8.dp))

        ) {
            AfterTexts("4.1 MB", Color.DarkGray)
        }

    }
}


@Composable
fun ComparePreview(modifier: Modifier = Modifier) {


    Column(modifier) {

        Box(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .align(Alignment.End)
                .background(brush, shape = RoundedCornerShape(8.dp))

        ) {
            AfterTexts("After", Color.White)
        }
        Image(
            painter = painterResource(R.drawable.img_girl),
            modifier = Modifier
                .heightIn(max = 178.dp)
                .widthIn(max = 141.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = null,
        )

        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .align(Alignment.Start)
                .background(brush, shape = RoundedCornerShape(8.dp))

        ) {
            AfterTexts("240 KB", Color.White)
        }

    }
}

@Composable
private fun AfterTexts(text: String = "After", color: Color) {
    Text(
        text = text,
        color = color,
        modifier = Modifier.padding(3.5.dp),
        style = MaterialTheme.typography.bodySmall
    )
}


@Preview(showBackground = true, device = "spec:parent=pixel_4,orientation=landscape")
@Composable
private fun CustomOnboardingScreenPreview() {
    CustomOnboardingScreen()
}
