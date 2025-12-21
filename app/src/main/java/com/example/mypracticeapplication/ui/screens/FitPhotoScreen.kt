package com.example.mypracticeapplication.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.components.GradientTopBar
import com.example.mypracticeapplication.ui.components.PagerNavigator
import com.example.mypracticeapplication.ui.components.SocailMediaAspectRatioSelector
import com.example.mypracticeapplication.ui.components.SocialAspectRatio
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//Todo: Make the text react to the click too,with socialMediaAspectRatio

sealed class BackgroundOption(val name: String) {
    object Blur : BackgroundOption("Blur")
    object Color : BackgroundOption("Color")
}

@Composable
fun FitPhotoScreen(
    onNavigateBack: () -> Unit = {}
) {
    var selectedAspectRatio by remember { mutableStateOf<SocialAspectRatio>(SocialAspectRatio.Custom) }
    var selectedBackgroundOption by remember { mutableStateOf<BackgroundOption>(BackgroundOption.Blur) }

    val sliderValue = rememberSaveable { mutableFloatStateOf(0.5f) }
    val socialAspectRatioList: ImmutableList<SocialAspectRatio> =
        persistentListOf(
            SocialAspectRatio.Custom,
            SocialAspectRatio.Instgram11,
            SocialAspectRatio.Instagram45,
            SocialAspectRatio.Youtube,
            SocialAspectRatio.Facebook,
            SocialAspectRatio.TikTok,
            SocialAspectRatio.Twitter
        )

    val state = rememberPagerState() { sampleImageMetaData.size }
    val scope = rememberCoroutineScope()

    FitPhotoScreenContent(
        onNavigateBack,
        socialAspectRatioList,
        sliderValue = sliderValue,
        imagedata = sampleImageMetaData,
        state = state,
        scope = scope,
        selectedAspectRatio = selectedAspectRatio,
        onAspectRatioSelected = { selectedAspectRatio = it },
        selectedBackgroundOption = selectedBackgroundOption,
        onSelectedBackgroundOption = { selectedBackgroundOption = it },
        onNextImage = { scope.launch { state.animateScrollToPage(page = state.currentPage + 1) } },
        onPreviousImage = { scope.launch { state.animateScrollToPage(page = state.currentPage - 1) } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitPhotoScreenContent(
    onNavigateBack: () -> Unit,
    socialAspectRatioList: ImmutableList<SocialAspectRatio>,
    sliderValue: MutableFloatState,
    imagedata: List<BatchImageMetaData>,
    state: PagerState,
    scope: CoroutineScope,
    selectedAspectRatio: SocialAspectRatio,
    onAspectRatioSelected: (SocialAspectRatio) -> Unit,
    onSelectedBackgroundOption: (BackgroundOption) -> Unit,
    selectedBackgroundOption: BackgroundOption,
    onNextImage: () -> Unit,
    modifier: Modifier = Modifier,
    isPremium: Boolean = false,
    onPreviousImage: () -> Unit
) {

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        topBar = {
            GradientTopBar("Fit Photo Size", onNavigateBack, onNavigateBack)
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color(0xFFF5F5F5))

        ) {

            HorizontalImageViewer(state, imagedata)

            Spacer(modifier = Modifier.height(16.dp))

            PagerNavigator(
                imagedata = imagedata,
                onNextImage = onNextImage,
                onPreviousImage = onPreviousImage,
                state = state
            )

            Spacer(modifier = Modifier.height(10.dp))

            Slider(sliderValue, isPremium)

            BlurColorButtons(onSelectedBackgroundOption, selectedBackgroundOption)

            Spacer(modifier = Modifier.height(16.dp))
            SocailMediaAspectRatioSelector(
                socialAspectRatioList,
                selectedAspectRatio,
                onAspectRatioSelected
            )
        }
    }
}

@Composable
private fun ColumnScope.HorizontalImageViewer(
    state: PagerState,
    imagedata: List<BatchImageMetaData>
) {
    HorizontalPager(
        state, modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {

        val image = imagedata[it].image
        Image(
            painter = painterResource(image),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.Fit, contentDescription = null
        )
    }
}

@Composable
private fun BlurColorButtons(
    onSelectedBackgroundOption: (BackgroundOption) -> Unit,
    selectedBackgroundOption: BackgroundOption
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = if (selectedBackgroundOption == BackgroundOption.Blur) horizontalGradient else SolidColor(
                            Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(onClick = { onSelectedBackgroundOption(BackgroundOption.Blur) })
                    .border(
                        width = 1.dp,
                        color = if (selectedBackgroundOption != BackgroundOption.Blur) Color(
                            0xFFEEEEEE
                        ) else Color.Unspecified,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = BackgroundOption.Blur.name,
                    modifier = Modifier.padding(
                        top = 13.5.dp,
                        bottom = 13.dp,
                        start = 43.5.dp,
                        end = 43.5.dp
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        brush = if (selectedBackgroundOption == BackgroundOption.Blur) SolidColor(
                            Color.White
                        ) else horizontalGradient
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = if (selectedBackgroundOption == BackgroundOption.Color) horizontalGradient else SolidColor(
                            Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width =  1.dp,
                        color = if (selectedBackgroundOption != BackgroundOption.Color) Color(
                            0xFFEEEEEE
                        ) else Color.Unspecified,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(onClick = { onSelectedBackgroundOption(BackgroundOption.Color) }),

                ) {

                Text(
                    text = "Color",
                    modifier = Modifier.padding(
                        top = 13.dp,
                        bottom = 13.5.dp,
                        start = 43.5.dp,
                        end = 43.5.dp
                    ),

                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        brush = if (selectedBackgroundOption == BackgroundOption.Color) SolidColor(
                            Color.White
                        ) else horizontalGradient
                    )
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Slider(
    sliderValue: MutableFloatState,
    isPremium: Boolean
) {
    Slider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        value = sliderValue.value,
        onValueChange = {
            sliderValue.value = it
        },

        thumb = {
            if (isPremium) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(color = Color.White, CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(color = Color(0xFFFFC42D), CircleShape)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
                        tint = Color.Black,
                        contentDescription = null,
                        modifier = Modifier
                            .size(10.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        },

        track = { sliderState ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = sliderState.value)
                        .fillMaxHeight()
                        .background(horizontalGradient)
                )
            }
        }
    )
}


@Preview(showBackground = true, device = "id:pixel_4")
@Composable
private fun FitPhotoScreenPreview() {

    val socialAspectRatioList: ImmutableList<SocialAspectRatio> =
        persistentListOf(
            SocialAspectRatio.Custom,
            SocialAspectRatio.Instgram11,
            SocialAspectRatio.Instagram45,
            SocialAspectRatio.Youtube,
            SocialAspectRatio.Facebook,
            SocialAspectRatio.TikTok,
            SocialAspectRatio.Twitter
        )
    FitPhotoScreenContent(
        onNavigateBack = {},
        socialAspectRatioList = socialAspectRatioList,
        sliderValue = rememberSaveable { mutableFloatStateOf(0.5f) },
        imagedata = sampleImageMetaData,
        state = rememberPagerState(pageCount = { 3 }),
        scope = rememberCoroutineScope(),
        selectedAspectRatio = SocialAspectRatio.Custom,
        onAspectRatioSelected = {},
        onSelectedBackgroundOption = {},
        selectedBackgroundOption = BackgroundOption.Blur,
        onPreviousImage = {},
        onNextImage = {}
    )
}
