package com.example.mypracticeapplication.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.screens.verticalGradient
import kotlinx.collections.immutable.ImmutableList

sealed class SocialAspectRatio(
    val name: String,
    @param:DrawableRes val image: Int, val
    height: Dp,
    val width: Dp,
    val bgColor: Brush = SolidColor(Color(0xFFFEF5FF))
) {
    object Custom : SocialAspectRatio("Custom", R.drawable.ic_custom_aspect_ratio, 40.dp, 40.dp)
    object Instgram11 :
        SocialAspectRatio("1:1", R.drawable.ic_instagram_gradient, 40.dp, 40.dp, verticalGradient)

    object Instagram45 : SocialAspectRatio("4:5", R.drawable.ic_instagram_gradient, 40.dp, 36.dp)
    object Youtube : SocialAspectRatio("16:9", R.drawable.ic_youtube, 30.dp, 49.dp)
    object Facebook : SocialAspectRatio("4:3 ", R.drawable.ic_facebook, 35.dp, 44.dp)
    object TikTok : SocialAspectRatio("9:16", R.drawable.ic_tiktok, 40.dp, 25.dp)
    object Twitter : SocialAspectRatio("3:4", R.drawable.ic_x, 28.dp, 43.dp)
}

@Composable
fun SocailMediaAspectRatioSelector(
    socialAspectRatioList: ImmutableList<SocialAspectRatio>,
    selectedAspectRatio: SocialAspectRatio,
    onAspectRatioSelected: (SocialAspectRatio) -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = Color.White)
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
            }
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.Bottom
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        socialAspectRatioList.forEach { ratio ->

            AspectRatioItem(
                ratio,
                isSelected = selectedAspectRatio == ratio,
                onClick = { onAspectRatioSelected(ratio) }
            )
            Spacer(modifier = Modifier.width(17.dp))
        }
    }
}

@Composable
fun AspectRatioItem(
    socialAspectRatio: SocialAspectRatio,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(11.dp))
        Box(
            modifier = Modifier
                .height(socialAspectRatio.height)
                .width(socialAspectRatio.width)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    brush = if (isSelected) verticalGradient else SolidColor(
                        Color(
                            0xFFFEF5FF
                        )
                    ), shape = RoundedCornerShape(4.dp)
                )
                .clickable(onClick = onClick)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(socialAspectRatio.image),
                modifier = Modifier
                    .align(Alignment.Center),
                contentDescription = null,
                tint = if (isSelected) Color.White else Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = socialAspectRatio.name,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.W500
            ),
            color = Color(0xFF626262)
        )
        Spacer(modifier = Modifier.height(11.dp))
    }
}
