package com.example.mypracticeapplication.ui.screens.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAnimationScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Overlay & Emitters", color = Color.White.copy(alpha = 0.9f)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->

        val overlays = remember {
            listOf(
                CustomChartOverlay.Circle(
                    _xIndex = 0.5f,
                    _yPercent = 0.4f,
                    radius = 55.dp,
                    _res = R.drawable.img_coin_onboarding,
                    _type = PillCircleType.COIN,
                    _colors = GoldenMoneyPalette,
                    _icon= Icons.Default.Paid,
                    _duartion = 100L
                ),
                CustomChartOverlay.Pill(
                    _xIndex = 2.0f,
                    _yPercent = 0.5f,
                    width = 110.dp,
                    height = 250.dp,
                    _res = R.drawable.img_heart_onboarding,
                    _type = PillCircleType.LIKE,
                    _colors = LovePalette,
                    _icon = Icons.Default.Favorite,
                    _duartion = 1200L
                ),
                CustomChartOverlay.Pill(
                    _xIndex = 3.5f,
                    _yPercent = 0.4f,
                    width = 100.dp,
                    height = 200.dp,
                    _res = R.drawable.img_follow_onboarding,
                    _type = PillCircleType.FOLLOW,
                    _colors = GreenMoneyPalette,
                    _icon = Icons.Default.PersonAdd,
                    _duartion = 1000L
                )
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkBackground)
        ) {
            val density = LocalDensity.current
            val canvasHeight = constraints.maxHeight.toFloat()
            // Arbitrary values simulating a bar chart layout
            val barWidth = with(density) { 60.dp.toPx() }
            val spacing = with(density) { 20.dp.toPx() }

            // Render all overlays
            overlays.forEach { overlay ->
                CustomOverlayItemView(
                    overlay = overlay,
                    barWidth = barWidth,
                    spacing = spacing,
                    canvasHeight = canvasHeight,
                    isVisible = true,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CustomAnimationScreenPreview() {
    MaterialTheme {
        CustomAnimationScreen(onNavigateBack = {})
    }
}
