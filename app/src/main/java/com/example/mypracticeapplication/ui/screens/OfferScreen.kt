package com.example.mypracticeapplication.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposablepractice.ui.screens.StatusBarIcons
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.theme.MyPracticeApplicationTheme


data class OfferFeature(
    @param:DrawableRes
    val icon: Int, val title: String
)

val sampleFeatures = listOf(
    OfferFeature(R.drawable.ic_compress, "Advanced Compression"),
    OfferFeature(R.drawable.ic_batch, "Batch Compression"),
    OfferFeature(R.drawable.ic_target_size, "Compress by Target File Size"),
    OfferFeature(R.drawable.ic_resize, "Batch Resize & Convert"),
    OfferFeature(R.drawable.ic_batch_crop, "Batch Crop & Fit Photo"),
    OfferFeature(R.drawable.ic_select_all, "Select All Photos at once"),
    OfferFeature(R.drawable.ic_ad, "Ad-free Experience"),
)

@Composable
fun OfferScreen() {

    val featureList = sampleFeatures
    
    OfferScreenContent(featureList = featureList, onFreeTrialClicked = {}, onBackClick = {})
}


@Composable
fun OfferScreenContent(
    featureList: List<OfferFeature>,
    modifier: Modifier = Modifier,
    onFreeTrialClicked: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    StatusBarIcons(false)

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF554FF),
            Color(0xFF434AFF),
        )

    )

    Box(
        modifier
            .fillMaxSize()
            .background(gradient)
            .safeDrawingPadding()
    ) {

        BackgroundLogo()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.weight(1f))

            HeadingLogoAndText()

            Spacer(modifier = Modifier.height(27.dp))

            Features(featureList)

            Spacer(modifier = Modifier.height(27.dp))

            Spacer(modifier = Modifier.weight(1f))


            bottomElements(onFreeTrialClicked = onFreeTrialClicked)


        }

        IconButton(
            onClick = onBackClick, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = null,
                tint = Color.Unspecified,


                )


        }
    }
}

@Composable
private fun Features(featureList: List<OfferFeature>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 62.dp, end = 38.dp)
    ) {

        featureList.forEach {

            Spacer(modifier = Modifier.height(13.dp))
            FeatureItem(it)
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .padding(start = 36.dp),
                color = Color(0x2CB4B4B4)
            )
        }
    }

}

@Composable
private fun ColumnScope.HeadingLogoAndText() {
    Image(
        imageVector = ImageVector.vectorResource(R.drawable.ic_premium),
        contentDescription = null,
        modifier = Modifier
            .size(74.dp)
            .align(Alignment.CenterHorizontally)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        "Try Premium for Free",
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(16.dp),
        color = Color(0xFFFFE120),
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun BoxScope.BackgroundLogo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .aspectRatio(1f)
    ) {
        Image(
            modifier = Modifier
                .matchParentSize()
                .scale(1.3f),
            imageVector = ImageVector.vectorResource(R.drawable.ic_compress),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = null,
            alpha = 0.08f
        )
    }
}

@Composable
private fun bottomElements(onFreeTrialClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {


        Text(
            text = "FREE 3-day trial, then USD \$19.90/year.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W400),
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(18.dp))



        Button(
            onClick = onFreeTrialClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, contentColor = Color(
                    0xFF0252FF
                )
            ),
            shape = RoundedCornerShape(8.dp)


        ) {
            val gradient = Brush.horizontalGradient(

                colors = listOf(Color(0xFFF554FF), Color(0xFF0252FF))
            )


            Text(
                "Start Free Trial",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    brush = gradient,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recurrent billing. Cancel anytime. You will be charged after trial ends unless canceled.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.W500
            ),
            color = Color(0xFFE0E0E0),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FeatureItem(feature: OfferFeature) {
    Row(modifier = Modifier.fillMaxWidth()) {

        var isMultiline by remember { mutableStateOf(false) }

        Image(
            ImageVector.vectorResource(feature.icon),
            contentDescription = null,
            modifier = Modifier
                .padding(top = if (isMultiline) 8.dp else 0.dp)
                .size(20.dp)
                .align(if (isMultiline) Alignment.Top else Alignment.CenterVertically),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = feature.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500),
            color = Color(0xFFFFFFFF),
            onTextLayout = { layoutResult ->
                isMultiline = layoutResult.lineCount > 1
            }
        )
    }
}


@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:width=1080px,height=2400px",
    fontScale = 1.0f, apiLevel = 36,
)
@Composable
private fun OfferScreenPreview() {

    MyPracticeApplicationTheme() {
        OfferScreenContent(sampleFeatures)
    }

}
