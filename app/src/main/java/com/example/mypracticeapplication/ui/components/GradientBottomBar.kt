package com.example.mypracticeapplication.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import com.example.mypracticeapplication.R

sealed class BottomNavDestination {
    data object Home : BottomNavDestination()
    data object Compare : BottomNavDestination()
    data object Replace : BottomNavDestination()
    data object Share : BottomNavDestination()
}

data class BottomNavAction(
    val destination: BottomNavDestination,
    val label: String,
    @param:DrawableRes val icon: Int,
    val hasBadge: Boolean = false
)

@Composable
fun GradientBottomBar(
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavDestination) -> Unit = {}
) {
    val items = listOf(
        BottomNavAction(BottomNavDestination.Home, "Home", R.drawable.ic_home),
        BottomNavAction(BottomNavDestination.Compare, "Compare", R.drawable.ic_compare),
        BottomNavAction(BottomNavDestination.Replace, "Replace", R.drawable.ic_replace, hasBadge = true),
        BottomNavAction(BottomNavDestination.Share, "Share", R.drawable.ic_share)
    )

    // Gradient colors approximation from the design
    val gradientColors = listOf(
        Color(0xFFF554FF),
        Color(0xFF434AFF)

    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(colors = gradientColors)
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            BottomBarItem(
                item = item,
                onClick = { onItemClick(item.destination) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BottomBarItem(
    item: BottomNavAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(top = 9.dp, bottom = 10.dp)
    ) {
        Box(modifier = Modifier.size(24.dp)) {
            Icon(
                imageVector = ImageVector.vectorResource(item.icon),
                contentDescription = item.label,
                tint = Color.White,
                modifier = Modifier.size(20.dp).align(Alignment.Center)
            )

            if (item.hasBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 10.dp, y = (-3).dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD700)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
                        contentDescription = "Premium",
                        tint = Color.Black,
                        modifier = Modifier.size(8.62.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = item.label,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Preview
@Composable
private fun GradientBottomBarPreview() {
    MaterialTheme {
        GradientBottomBar()
    }
}
