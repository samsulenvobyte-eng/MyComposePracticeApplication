package com.example.mypracticeapplication.ui.screens.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.components.GradientBottomBar

//Note: Shorten the image title
//Note: Will top progress alway have gradient?
//Note: Bigger allow icon:
//Note: click effect on the edit image item icon buttons


enum class ResultStatus {
    Success,
    Failed
}

@Immutable
data class ResultDataModel(
    @param:DrawableRes val image: Int,
    val title: String,
    val originalSize: String,
    val afterSize: String,
    val resolution: String,
    val status: ResultStatus = ResultStatus.Success
)

val sampleResultData = listOf(
    ResultDataModel(
        R.drawable.img_compare_screen,
        "pexel-scott-web-2...3445654644561.jpg",
        "6.34 MB",
        "456 KB",
        "480×864"
    ), ResultDataModel(
        R.drawable.img_sample_2,
        "pexel-dfsdfsdf...3445654644561.jpg",
        "6.34 MB",
        "456 KB",
        "480×864"
    ), ResultDataModel(
        R.drawable.img_compare_screen,
        "pexel-scottdf644561.jpg",
        "6.34 MB",
        "456 KB",
        "480×864"
    ), ResultDataModel(
        R.drawable.img_sample_2,
        "pexel-scott-web-2...3445654644561.jpg",
        "6.34 MB",
        "456 KB",
        "480×864"
    )
)

val verticalGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFF554FF),
        Color(0xFF434AFF),
    )

)
val horizontalGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFF554FF),
        Color(0xFF434AFF),
    )

)

@Composable
fun ResultScreen(
    onNavigateBack: () -> Unit = {}
) {
    val savedInfoText = "Saved 97%"
    val resultDataList = sampleResultData



    ResultScreenContent(savedInfoText = savedInfoText, resultDataList = resultDataList)
}

@Composable
fun ResultScreenContent(
    savedInfoText: String,
    modifier: Modifier = Modifier,
    resultDataList: List<ResultDataModel>
) {
    Scaffold(
        modifier
            .fillMaxSize()
            .background(color = Color.White)
            .safeDrawingPadding(),
        bottomBar = {


            GradientBottomBar { }

        })


    { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(13.dp))

            HeadingContent(savedInfoText)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 16.dp)
            ) {


                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_folder),
                    modifier = Modifier
                        .size(24.dp)
                        .background(brush = verticalGradient, shape = RoundedCornerShape(4.dp))
                        .padding(4.dp)
                        .align(Alignment.CenterVertically),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )


                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Saved at Internal Storage/Pictures/\nImageCompressor",
                    color = Color(0xFF333333),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            CompressStats()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Edited Images",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 1.29.em
                ),
                color = Color(0xFF212121)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column() {

                resultDataList.forEach {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = Color(0xFFEEEEEE),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        EditedImageItem(resultDataModel = it)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }


            }


        }
    }
}

@Composable
private fun EditedImageItem(resultDataModel: ResultDataModel) {

    val statusIcon = when (resultDataModel.status) {

        ResultStatus.Success -> ImageVector.vectorResource(R.drawable.ic_thick_tick)
        ResultStatus.Failed -> ImageVector.vectorResource(R.drawable.ic_close)
    }

    Row(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                .size(54.dp)
        ) {

            Image(
                painter = painterResource(resultDataModel.image),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .size(15.dp)
                    .offset(x = 5.dp, y = 2.dp)
                    .background(brush = verticalGradient, shape = CircleShape)
                    .align(alignment = Alignment.BottomEnd)

            ) {

                Icon(
                    imageVector = statusIcon,
                    modifier = Modifier
                        .size(6.dp)
                        .align(Alignment.Center),
                    contentDescription = null,
                    tint = Color.White

                )
            }

        }

        Spacer(modifier = Modifier.width(13.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 13.dp, end = 19.dp)
        ) {

            Text(
                text = resultDataModel.title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W600,
                    lineHeight = 1.em
                ),
                color = Color(0xFF212121)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(modifier = Modifier) {

                    Row(modifier = Modifier) {

                        Text(
                            text = resultDataModel.originalSize,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                lineHeight = 1.em,
                                fontWeight = FontWeight.W400
                            ),
                            color = Color(0xFF424242)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow),
                            tint = Color.Unspecified,
                            contentDescription = null,
                            modifier = Modifier
                                .size(10.dp)
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = resultDataModel.afterSize,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                lineHeight = 1.em,
                                fontWeight = FontWeight.W600, brush = verticalGradient
                            ),

                            )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = resultDataModel.resolution,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            lineHeight = 1.em,
                            fontWeight = FontWeight.W500

                        ),
                        color = Color(0xFF757575)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))


                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_compare),
                        modifier = Modifier
                            .size(22.dp)
                            .align(alignment = Alignment.Center)
                            .graphicsLayer(alpha = 0.99f)
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent() // Draws the icon first
                                    drawRect(
                                        brush = verticalGradient,
                                        blendMode = BlendMode.SrcAtop // Only draws where the icon is
                                    )
                                }
                            },

                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_share),
                        modifier = Modifier
                            .size(20.dp)
                            .align(alignment = Alignment.Center),

                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }


            }


        }

    }
}

@Composable
private fun CompressStats() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp))
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            CompressionSizeStat(
                progressTitle = "Original",
                progress = 1f,
                gradient = horizontalGradient
            )
            Spacer(modifier = Modifier.height(16.dp))
            CompressionSizeStat(
                "Compressed",
                progress = 0.4f,
                gradient = SolidColor(Color(0xFF00B154))
            )
        }


    }
}

@Composable
private fun CompressionSizeStat(
    progressTitle: String,
    modifier: Modifier = Modifier,
    progress: Float,
    gradient: Brush
) {


    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = progressTitle, style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.W500
            ),
            color = Color(0xFF212121)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "15.34 MB",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W600, brush = verticalGradient
            )
        )
    }
    Spacer(modifier = Modifier.height(4.dp))

    GradientLinearProgress(progress, gradient = gradient)

}

@Composable
fun GradientLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 4.dp, gradient: Brush
) {
    Canvas(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
    ) {
        val progressWidth = size.width * progress.coerceIn(0f, 1f)

        drawRoundRect(
            color = Color.LightGray.copy(alpha = 0.3f),
            cornerRadius = CornerRadius(size.height / 2)
        )
        drawRoundRect(
            brush = gradient,
            size = Size(progressWidth, size.height),
            cornerRadius = CornerRadius(size.height / 2)
        )
    }
}

@Composable
private fun HeadingContent(savedInfoText: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = savedInfoText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 21.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF212121)
                )
                Spacer(modifier = Modifier.width(9.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_tick),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(shape = CircleShape)
                .clickable(onClick = {})
                .align(Alignment.CenterStart)

        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.Center),
                contentDescription = null,
                tint = Color.Black,
            )
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_4")
@Composable
private fun ResultScreenPreview() {
    ResultScreenContent("Saved 97%", resultDataList = sampleResultData)
}
