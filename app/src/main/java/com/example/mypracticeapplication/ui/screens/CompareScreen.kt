package com.example.mypracticeapplication.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.R

enum class ComparisonState {
    BEFORE, AFTER
}

data class CompareModel(
    @param:DrawableRes val image: Int,
    val comparisonState: ComparisonState,
    val size: String,
    val saved: String
)


val compareList: List<Pair<CompareModel, CompareModel>> = listOf(

    // First Pair
    CompareModel(
        R.drawable.img_compare_screen,
        ComparisonState.BEFORE,
        "45 MB",
        "-"
    ) to CompareModel(
        R.drawable.img_compare_screen,
        ComparisonState.AFTER,
        "156 KB",
        "89% saved"
    ),

    // Second Pair
    CompareModel(
        R.drawable.img_compare_screen,
        ComparisonState.BEFORE,
        "5 MB",
        "-"
    ) to CompareModel(
        R.drawable.img_compare_screen,
        ComparisonState.AFTER,
        "200 KB",
        "50% saved"
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Compare",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            val pageCount = compareList.size
            val state = rememberPagerState(pageCount = { pageCount })



            VerticalPager(state, modifier = Modifier.fillMaxSize()) { page ->

                val comparePair = compareList.get(page)
                val beforeItem = comparePair.first
                val afterItem = comparePair.second


                Column(modifier = Modifier.fillMaxSize()) {


                    CompareItem(
                        beforeItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                    )

                    CompareItem(
                        afterItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                    )

                }

            }



        }
    }
}

@Composable
fun CompareItem(comparePair: CompareModel, modifier: Modifier = Modifier) {

    val brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF554FF),
            Color(0xFF434AFF)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )


    val isAfter = comparePair.comparisonState == ComparisonState.AFTER
    val color = if (isAfter) brush else SolidColor(Color.White)


    Box(modifier) {

        Image(
            painter = painterResource(comparePair.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp)
                .background(
                    color,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (!isAfter) Color(0xFFEEEEEE) else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .align(Alignment.TopStart)
        ) {
            Text(
                comparePair.comparisonState.name,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.5.dp),
                if (isAfter) Color.White else Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (isAfter) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .alpha(0.6f)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFEEEEEE),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .align(Alignment.TopEnd)
            ) {

                Text(
                    comparePair.saved,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.5.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .background(
                    color,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (!isAfter) Color(0xFFEEEEEE) else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .align(Alignment.BottomCenter)
        ) {

            Text(
                comparePair.size,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.5.dp),
                color = if (isAfter) Color.White else Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }


}


@Preview(showBackground = true)
@Composable
private fun CompareScreenPreview() {
    CompareScreen()
}
