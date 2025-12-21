package com.example.mypracticeapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NavButton(
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToCanvas: () -> Unit = {},
    onNavigateToOffer: () -> Unit = {},
    onNavigateToCustomOnboarding: () -> Unit = {},
    onNavigateToExperiment: () -> Unit = {},
    onNavigateToOnboardingV2: () -> Unit = {},
    onNavigateToCompare: () -> Unit = {},
    onNavigateToViewBatchImage: () -> Unit = {},
    onNavigateToResult: () -> Unit = {},
    onNavigateToFitPhoto: () -> Unit = {},
    onNavigateToBatchCrop: () -> Unit = {}
) {
    val navButtons = listOf(
        NavButton("Profile", Color.White, Color(0xFF667eea), onNavigateToProfile),
        NavButton("Settings", Color.White.copy(alpha = 0.2f), Color.White, onNavigateToSettings),
        NavButton("Canvas", Color(0xFFf093fb), Color.White, onNavigateToCanvas),
        NavButton("Offers", Color(0xFF43e97b), Color.White, onNavigateToOffer),
        NavButton("Onboarding", Color(0xFFff6b6b), Color.White, onNavigateToCustomOnboarding),
        NavButton("Experiment", Color(0xFF11998e), Color.White, onNavigateToExperiment),
        NavButton("Onboarding V2", Color(0xFFF554FF), Color.White, onNavigateToOnboardingV2),
        NavButton("Compare", Color(0xFF5C6BC0), Color.White, onNavigateToCompare),
        NavButton("Batch Image", Color(0xFFFF7043), Color.White, onNavigateToViewBatchImage),
        NavButton("Result", Color(0xFF00BCD4), Color.White, onNavigateToResult),
        NavButton("Fit Photo", Color(0xFF9C27B0), Color.White, onNavigateToFitPhoto),
        NavButton("Batch Crop", Color(0xFF4CAF50), Color.White, onNavigateToBatchCrop)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Icon container
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome Home",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Navigation Demo App",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation buttons in grid
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                navButtons.chunked(2).forEach { rowButtons ->
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowButtons.forEach { button ->
                            Button(
                                onClick = button.onClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = button.containerColor,
                                    contentColor = button.contentColor
                                )
                            ) {
                                Text(
                                    text = button.label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        // Add empty space if odd number of buttons in last row
                        if (rowButtons.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToProfile = {},
        onNavigateToSettings = {},
        onNavigateToCanvas = {},
        onNavigateToOffer = {},
        onNavigateToCustomOnboarding = {},
        onNavigateToExperiment = {},
        onNavigateToOnboardingV2 = {},
        onNavigateToCompare = {},
        onNavigateToViewBatchImage = {},
        onNavigateToResult = {},
        onNavigateToFitPhoto = {},
        onNavigateToBatchCrop = {}
    )
}
