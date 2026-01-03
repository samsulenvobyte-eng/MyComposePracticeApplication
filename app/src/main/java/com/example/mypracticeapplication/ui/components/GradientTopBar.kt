package com.example.mypracticeapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypracticeapplication.ui.screens.image.horizontalGradient

@Composable
fun GradientTopBar(
    title: String,
    onCloseClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(horizontalGradient)
            // statusBarsPadding()
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }

        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )

        IconButton(onClick = onConfirmClick) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Confirm",
                tint = Color.White
            )
        }
    }
}

