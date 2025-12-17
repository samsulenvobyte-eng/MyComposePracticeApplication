package com.example.mypracticeapplication.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//todo: Remove button duplication

data class BatchImageMetaData(
    @param:DrawableRes val image: Int,
    val uri: String,
    val size: String,
    val resolution: String,
    val index: String
)

val sampleImageMetaData = listOf<BatchImageMetaData>(
    BatchImageMetaData(
        R.drawable.img_sample_2,
        "/Pictures/ImageCompressor/4651_r253klj...dfsdfljl_23.jpg",
        "245.2 KB",
        "2283 X 1712",
        "1/5"
    ),
    BatchImageMetaData(
        R.drawable.img_compare_screen,
        "/Pictures/ImageCompressor/sdfljl_23.jpg",
        "904 KB",
        "67567 X 6576",
        "5/5"
    ),
    BatchImageMetaData(
        R.drawable.img_sample_2,
        "/Pictures/ImageCompressor/4651_rljl_2454353.jpg",
        "934 MB",
        "22 X 434",
        "3/3"
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewBatchImageScreen(
    onNavigateBack: () -> Unit = {}
) {
    val imageMetaData = sampleImageMetaData
    val scope = rememberCoroutineScope()
    ViewBatchImageScreenContent(imageMetaData = imageMetaData, scope = scope)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewBatchImageScreenContent(
    scope: CoroutineScope,
    imageMetaData: List<BatchImageMetaData>,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "View Batch Image",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        val pageCount = imageMetaData.size
        val state = rememberPagerState(pageCount = { pageCount })

        val uri = imageMetaData[state.currentPage].uri
        val size = imageMetaData[state.currentPage].size
        val resolution = imageMetaData[state.currentPage].resolution
        val index = imageMetaData[state.currentPage].index

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(color = Color.Gray)
            ) {
                HorizontalPager(state) {

                    val image = imageMetaData[it].image

                    Box(modifier = Modifier.fillMaxSize()) {

                        Image(
                            painter = painterResource(image),
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.LightGray),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                    }
                }

                val showNextButton = state.currentPage < state.pageCount - 1
                val showPreviousButton = state.currentPage > 0

                if(showNextButton){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(color = Color(0x99585858).copy(alpha = 0.6f))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = Color.White),
                                onClick = {
                                    scope.launch { state.animateScrollToPage(state.currentPage + 1) }
                                })
                            .align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_next),
                            modifier = Modifier.size(14.dp),
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }

                if(showPreviousButton){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(color = Color(0x99585858).copy(alpha = 0.6f))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = Color.White),
                                onClick = {
                                    scope.launch { state.animateScrollToPage(state.currentPage - 1) }
                                })
                            .align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                            modifier = Modifier.size(14.dp),
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }


            }

            BottomMetaData(uri, size, resolution, index)
        }
    }
}

@Composable
private fun BottomMetaData(
    uri: String,
    size: String,
    resolution: String,
    index: String,
    modifier: Modifier = Modifier

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFAFAFA))
            .padding(start = 16.dp, end = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = uri,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {

            Text(
                text = size,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "|", color = Color.Blue,
                style = MaterialTheme.typography.bodySmall,
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = resolution, style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = index,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF616161)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewBatchImageScreenPreview() {
    ViewBatchImageScreen()
}
