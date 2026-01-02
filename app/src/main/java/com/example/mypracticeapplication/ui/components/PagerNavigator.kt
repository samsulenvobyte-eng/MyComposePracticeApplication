package com.example.mypracticeapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.screens.BatchImageMetaData

@Composable
fun PagerNavigator(
    state: PagerState,
    imagedata: List<BatchImageMetaData>,
    onNextImage: () -> Unit,
    onPreviousImage: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {


        Box(
            modifier = Modifier
                .height(24.dp)
                .background(
                    color = Color(0xFFFEF5FF),
                    shape = RoundedCornerShape(30.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFF4EEFF),
                    shape = RoundedCornerShape(30.dp)
                )
                .align(alignment = Alignment.Center),

            ) {

            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(32.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 24.dp,
                                bottomStart = 24.dp,
                                topEnd = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                        .clickable(onClick = onPreviousImage)


                ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_backward_fit_photo),
                        modifier = Modifier
                            .size(5.dp)
                            .align(alignment = Alignment.Center),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                }
                Spacer(modifier = Modifier.width(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxHeight()

                ) {


                    Text(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        text = "${state.currentPage + 1}/${imagedata.size}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 3.sp

                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))

                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(32.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                bottomStart = 0.dp,
                                topEnd = 24.dp,
                                bottomEnd = 24.dp
                            )
                        )
                        .clickable(onClick = onNextImage)

                ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_forward_fit_photo),
                        modifier = Modifier
                            .size(5.dp)
                            .align(alignment = Alignment.Center),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                }


            }


        }

    }
}