package com.example.mypracticeapplication.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.components.GradientTopBar
import com.example.mypracticeapplication.ui.components.PagerNavigator
import com.example.mypracticeapplication.ui.components.SocailMediaAspectRatioSelector
import com.example.mypracticeapplication.ui.components.SocialAspectRatio
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

sealed class ImageOperations(val name: String, @param:DrawableRes val icon: Int) {
    object FlipVertical : ImageOperations("Flip Vertical", R.drawable.ic_flip_vertical)
    object FlipHorizontal : ImageOperations("Flip Horizontal", R.drawable.ic_flip_horizontal)
    object RotateLeft : ImageOperations("Rotate Left", R.drawable.ic_rotate_backward)
    object RotateRight : ImageOperations("Rotate Right", R.drawable.ic_rotate_forward)
}


@Composable
fun BatchCropScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    var selectedAspectRatio by remember { mutableStateOf<SocialAspectRatio>(SocialAspectRatio.Custom) }
    val state = rememberPagerState(pageCount = { sampleImageMetaData.size })
    val scope = rememberCoroutineScope()


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

    val imageOperationsList: ImmutableList<ImageOperations> =
        persistentListOf(
            ImageOperations.FlipHorizontal,
            ImageOperations.FlipVertical,
            ImageOperations.RotateRight,
            ImageOperations.RotateLeft,

            )


    BatchCropContent(
        onNavigateBack = onNavigateBack,
        selectedAspectRatio = selectedAspectRatio,
        onAspectRatioSelected = { selectedAspectRatio = it },
        socialAspectRatioList = socialAspectRatioList,
        imageOperationsList = imageOperationsList,
        state = state,
        onNextImage = { scope.launch { state.animateScrollToPage(page = state.currentPage + 1) } },
        onPreviousImage = { scope.launch { state.animateScrollToPage(page = state.currentPage - 1) } }
    )


}


@Composable
fun BatchCropContent(
    onNavigateBack: () -> Unit,
    selectedAspectRatio: SocialAspectRatio,
    onAspectRatioSelected: (SocialAspectRatio) -> Unit,
    socialAspectRatioList: ImmutableList<SocialAspectRatio>,
    imageOperationsList: ImmutableList<ImageOperations>,
    state: PagerState,
    onPreviousImage: () -> Unit,
    modifier: Modifier = Modifier,
    onNextImage: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        topBar = {
            GradientTopBar("Batch Crop", onNavigateBack, onNavigateBack)
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            HorizontalPager(state, Modifier.weight(1f)) {

                val image = sampleImageMetaData[it].image

                Image(
                    painter = painterResource(image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop, contentDescription = null
                )
            }


            Spacer(modifier = Modifier.height(19.dp))

            PagerNavigator(
                state = state,
                imagedata = sampleImageMetaData,
                onNextImage = onNextImage,
                onPreviousImage = onPreviousImage
            )

            Spacer(modifier = Modifier.height(19.dp))


            ImageOperation(imageOperationsList)

            SocailMediaAspectRatioSelector(
                socialAspectRatioList,
                selectedAspectRatio,
                onAspectRatioSelected
            )


        }

    }
}

@Composable
private fun ImageOperation(imageOperationsList: ImmutableList<ImageOperations>) {
    Box(
        modifier = Modifier
            .background(color = Color(0xFFFEF5FF))
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val color = Color(0xFFEEEEEE)

                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )

                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }) {
        Row(
            modifier = Modifier
                .padding(top = 13.dp, bottom = 12.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(16.dp),

            ) {

            imageOperationsList.forEach {


                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(shape = CircleShape)
                        .border(
                            width = 1.dp,
                            brush = verticalGradient,
                            shape = CircleShape
                        )
                        .clickable(onClick = {}),
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(alignment = Alignment.Center),
                        imageVector = ImageVector.vectorResource(it.icon),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }

        }


    }
}

@Preview(showBackground = true)
@Composable
private fun BatchCropScreenPreview() {


    BatchCropContent(
        onNavigateBack = {},
        selectedAspectRatio = SocialAspectRatio.Custom,
        onAspectRatioSelected = {},
        socialAspectRatioList =
            persistentListOf(
                SocialAspectRatio.Custom,
                SocialAspectRatio.Instgram11,
                SocialAspectRatio.Instagram45,
                SocialAspectRatio.Youtube,
                SocialAspectRatio.Facebook,
                SocialAspectRatio.TikTok,
                SocialAspectRatio.Twitter
            ),
        imageOperationsList = persistentListOf(
            ImageOperations.FlipVertical,
            ImageOperations.FlipHorizontal,
            ImageOperations.RotateLeft,
            ImageOperations.RotateRight
        ),
        state = rememberPagerState(pageCount = { 3 }),
        onPreviousImage = {},
        onNextImage = {}
    )
}
