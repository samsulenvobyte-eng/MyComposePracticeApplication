package com.example.mypracticeapplication.ui.screens.experiment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mypracticeapplication.ui.components.BottomNavDestination
import com.example.mypracticeapplication.ui.components.GradientBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperimentScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        bottomBar = {
            GradientBottomBar(
                onItemClick = { destination ->
                    val message = when (destination) {
                        BottomNavDestination.Home -> "Home"
                        BottomNavDestination.Compare -> "Compare"
                        BottomNavDestination.Replace -> "Replace"
                        BottomNavDestination.Share -> "Share"
                    }
                    android.widget.Toast.makeText(
                        context,
                        "Clicked: $message",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Container for the image and tick mark
            Box(
                modifier = Modifier.size(54.dp)
            ) {
                // Square image placeholder
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Image placeholder",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Gray
                    )
                }

                // Tick mark badge at bottom-right, slightly pushing out
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 6.dp, y = 6.dp)
                        .size(20.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        modifier = Modifier.size(14.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ExperimentScreenPreview() {
    ExperimentScreen()
}