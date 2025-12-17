package com.example.mypracticeapplication.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.components.BottomNavDestination
import com.example.mypracticeapplication.ui.components.GradientBottomBar

@Composable
fun ResultScreen(
    onNavigateBack: () -> Unit = {}
) {
    val savedInfoText = "Saved 97%"
    ResultScreenContent(savedInfoText = savedInfoText)
}

@Composable
fun ResultScreenContent(savedInfoText: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Scaffold(
        modifier
            .fillMaxSize()
            .safeDrawingPadding(),
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
        }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.White)
        ) {

            Spacer(modifier = Modifier.height(13.dp))





            HeadingContent(savedInfoText)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {


                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_folder),
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(24.dp)
                        .background(Color.Blue, shape = RoundedCornerShape(4.dp))
                        .padding(4.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )


                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Saved at Internal Storage/Pictures/\nImageCompressor",
                    color = Color(0xFF333333),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 1.em)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            CompressStats()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Edited Images",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 1.29.em
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

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

                EditedImageItem()


            }


        }
    }
}

@Composable
private fun EditedImageItem() {
    Row(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                .size(54.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.img_compare_screen),
                contentDescription = null,
                modifier = Modifier

                    .size(54.dp)

                    .clip(shape = RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 6.dp, y = 3.dp)
                    .size(15.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_thick_tick),
                    contentDescription = "Selected",
                    modifier = Modifier.size(6.dp),
                    tint = Color.White
                )
            }
        }

        //todo: Add status icon

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 13.dp, end = 19.dp)
        ) {

            Text(
                text = "pexel-scott-web-2...3445654644561.jpg",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W600,
                    lineHeight = 1.em
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(modifier = Modifier) {

                    Row(modifier = Modifier) {

                        Text(
                            text = "6.1 MB",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                lineHeight = 1.em,
                                fontWeight = FontWeight.W400
                            )
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow),
                            tint = Color.Unspecified,
                            contentDescription = null,
                            modifier = Modifier
                                .size(8.dp)
                                .align(Alignment.CenterVertically)

                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "3.1 MB",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                lineHeight = 1.em,
                                fontWeight = FontWeight.W600
                            )
                        )

                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "480×864",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            lineHeight = 1.em,
                            fontWeight = FontWeight.W500

                        )
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
                            .size(20.dp)
                            .align(alignment = Alignment.Center),

                        contentDescription = null,
                        tint = Color.Black
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

            CompressionSizeStat()
            Spacer(modifier = Modifier.height(16.dp))
            CompressionSizeStat()
        }


    }
}

@Composable
private fun CompressionSizeStat() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Original", style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "15.34 MB", style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.W500
            )
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
        progress = { 0.4f },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun HeadingContent(savedInfoText: String) {
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
                )
            )
            Spacer(modifier = Modifier.width(9.dp))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_tick),
                contentDescription = null,
                tint = Color.Green,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        IconButton(
            onClick = {}, modifier = Modifier
                .padding(start = 6.dp)
                .align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = null,
                tint = Color.Black,
            )
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_4")
@Composable
private fun ResultScreenPreview() {
    ResultScreenContent("Saved 97%")
}
