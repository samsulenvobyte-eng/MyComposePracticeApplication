package com.example.mycomposablepractice.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.theme.MyPracticeApplicationTheme

enum class LineDirection {
    LeftToRight,
    RightToLeft
}
private val BrandBlue = Color(0xFF3366FF)

private val BrandBlueGradientStart = Color(0xFF0252FF)

private val BrandBlueGradientEnd = Color(0xFF0348DE)

private val TextBlack = Color(0xFF1A1A1A)

private val TextGrey = Color(0xFF666666)

private val DividerColor = Color(0xFFEEEEEE)

private val StarYellow = Color(0xFFFFD54F)

private val StarBg = Color(0xFFFFF8E1)

private val BoxBg = Color(0xFFE8F0FE)

private val FacebookBlue = Color(0xFF1877F2)

private val InstagramPink = Color(0xFFE1306C)

private val SnapchatYellow = Color(0xFFFFFC00)


// --- 2. Architecture: States & Events ---

enum class PlanType { Monthly, Yearly }


data class PremiumUiState(

    val selectedPlan: PlanType = PlanType.Yearly,

    val monthlyPrice: String = "3.99",

    val yearlyPrice: String = "19.99"

)


sealed interface PremiumUiEvent {

    data object OnCloseClicked : PremiumUiEvent

    data class OnPlanSelected(val plan: PlanType) : PremiumUiEvent

    data object OnStartTrialClicked : PremiumUiEvent

}


// --- 3. Main Screen Composable ---

@Composable

fun PremiumScreen(

    onClose: () -> Unit = {},

    onNavigateToTrial: () -> Unit = {}

) {

    var uiState by remember { mutableStateOf(PremiumUiState()) }



    PremiumContent(

        state = uiState,

        onEvent = { event ->

            when (event) {

                is PremiumUiEvent.OnCloseClicked -> onClose()

                is PremiumUiEvent.OnStartTrialClicked -> onNavigateToTrial()

                is PremiumUiEvent.OnPlanSelected -> uiState =
                    uiState.copy(selectedPlan = event.plan)

            }

        }

    )

}


@Composable
fun PremiumContent(
    state: PremiumUiState,
    onEvent: (PremiumUiEvent) -> Unit

) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            val screenHeight = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            ) {

                PremiumHeaderSection(
                    onClose = { onEvent(PremiumUiEvent.OnCloseClicked) }
                )

                FeatureList()
                Spacer(modifier = Modifier.height(20.dp))

                Spacer(modifier = Modifier.weight(1f))

                PurchaseOptions(state, onEvent)

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(20.dp))
                Footer(onEvent)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable

private fun FeatureList() {

    Column(

        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                Color(0xFFFAFAFA),
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                )
    ) {

        FeatureRow(R.drawable.ic_compress, "Advanced Compression")
        FeatureRow(R.drawable.ic_batch, "Batch Compression")
        FeatureRow(R.drawable.ic_target_size, "Target File Size")
        FeatureRow(R.drawable.ic_resize, "Batch Resize & Convert")
        FeatureRow(R.drawable.ic_batch_crop, "Batch Crop & Fit Photo")
        FeatureRow(R.drawable.ic_select_all, "Select All Photos at once")
        FeatureRow(R.drawable.ic_ad, "Ad-free Experience", showDivider = false)

    }

}


@Composable

private fun PurchaseOptions(
    state: PremiumUiState,
    onEvent: (PremiumUiEvent) -> Unit

) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)

    ) {

        PlanCard(

            title = "Monthly Plan",

            price = state.monthlyPrice,

            icon = Icons.Outlined.Inventory2,

            iconBgColor = BoxBg,

            iconTint = BrandBlue,

            isSelected = state.selectedPlan == PlanType.Monthly,

            onClick = { onEvent(PremiumUiEvent.OnPlanSelected(PlanType.Monthly)) }

        )

        Box(modifier = Modifier.fillMaxWidth()) {

            PlanCard(

                title = "Yearly Plan",

                price = state.yearlyPrice,

                icon = Icons.Rounded.Star,

                iconBgColor = StarBg,

                iconTint = StarYellow,

                isSelected = state.selectedPlan == PlanType.Yearly,

                modifier = Modifier.padding(top = 10.dp),

                onClick = { onEvent(PremiumUiEvent.OnPlanSelected(PlanType.Yearly)) }

            )

            Badge60Percent(

                modifier = Modifier

                    .align(Alignment.TopEnd)

                    .padding(end = 24.dp)

                    .zIndex(1f)
            )
        }

    }

}


@Composable

private fun Footer(onEvent: (PremiumUiEvent) -> Unit) {

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        ) {

        Button(

            onClick = { onEvent(PremiumUiEvent.OnStartTrialClicked) },

            modifier = Modifier

                .fillMaxWidth(),

            shape = RoundedCornerShape(30.dp),

            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)

        ) {

            Text(

                modifier = Modifier.padding(vertical = 8.dp),

                text = "Start Free Trial",

                fontSize = 17.sp,

                fontWeight = FontWeight.SemiBold,

                color = Color.White,

                )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(

            modifier = Modifier.fillMaxWidth(),

            text = "Recurrent billing. Cancel anytime. You will be charged after trial ends unless canceled.",

            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.W500
            ),

            color = TextGrey,

            textAlign = TextAlign.Center,

            )
    }
}
@Composable

fun PremiumHeaderSection(onClose: () -> Unit) {

    Box(

        modifier = Modifier

            .padding(start = 16.dp, end = 16.dp, top = 8.dp)

            .fillMaxWidth()

            .background(

                brush = Brush.horizontalGradient(

                    colorStops = arrayOf(
                        0.6f to BrandBlueGradientStart,
                        1.0f to BrandBlueGradientEnd
                    )
                ),

                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                    )
                }
                Text(
                    text = "Unlock PRO Access",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.TopCenter)

                )

            }



            Spacer(modifier = Modifier.height(height = 18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                DirectionalLine(
                    modifier = Modifier.align(
                        BiasAlignment(
                            horizontalBias = 0.0f,
                            verticalBias = 0.7f
                        )
                    ),
                    direction = LineDirection.RightToLeft,
                    fraction = 0.25f
                )
                DirectionalLine(
                    modifier = Modifier.align(
                        BiasAlignment(
                            horizontalBias = 0.0f,
                            verticalBias = 0.0f
                        )
                    ),
                    direction = LineDirection.RightToLeft,
                    fraction = 0.28f
                )

                DirectionalLine(
                    modifier = Modifier.align(
                        BiasAlignment(
                            horizontalBias = 0.0f,
                            verticalBias = 0.7f
                        )
                    ),
                    direction = LineDirection.LeftToRight,
                    fraction = 0.25f
                )
                DirectionalLine(
                    modifier = Modifier.align(
                        BiasAlignment(
                            horizontalBias = 0.0f,
                            verticalBias = 0.0f
                        )
                    ),
                    direction = LineDirection.LeftToRight,
                    fraction = 0.28f
                )


                Canvas(
                    modifier = Modifier
                        .size(60.dp)
                        .align(BiasAlignment(horizontalBias = 0.55f, verticalBias = 0f))


                ) {

                    drawCircle(
                        color = Color(0xFF145EFF),
                        radius = 40.dp.toPx(),
                        center = Offset(x = size.width / 2, y = 200f)
                    )
                }

                Canvas(
                    modifier = Modifier
                        .size(69.dp)
                        .align(BiasAlignment(horizontalBias = -0.55f, verticalBias = 0f))


                ) {

                    drawCircle(
                        color = Color(0xFF145EFF),
                        radius = 40.dp.toPx(),
                        center = Offset(x = size.width / 2, y = 200f)
                    )
                }


                // 1. Facebook icon (Far Left)
                // Horizontal Bias -0.9f puts it near the left edge (Start)
                FloatingIcon(
                    image = R.drawable.facebook_image,
                    tint = Color.White,
                    bgColor = FacebookBlue,
                    size = 25.dp,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(BiasAlignment(horizontalBias = -0.9f, verticalBias = 0f))
                        .offset(y = (-8).dp)
                        .rotate(-17.29f)
                )

                // 2. Photo Card 1 (Mid Left)
                // Horizontal Bias -0.45f puts it roughly halfway between center and left edge
                FloatingPhotoCard(
                    drawableRes = R.drawable.second_girl_image,
                    modifier = Modifier
                        .align(BiasAlignment(horizontalBias = -0.45f, verticalBias = 0f))
                        .offset(y = (-4).dp)
                        .rotate(10.59f)
                )

                // 3. Instagram icon (Dead Center)
                // Standard Alignment.BottomCenter is effectively Bias(0f, 1f)
                FloatingIcon(
                    image = R.drawable.instagram_image,
                    tint = Color.White,
                    bgColor = InstagramPink,
                    size = 29.dp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .rotate(26f)
                        .padding(bottom = 4.dp)
                )

                // 4. Photo Card 2 (Mid Right)
                // Horizontal Bias 0.45f puts it roughly halfway between center and right edge
                FloatingPhotoCard(
                    R.drawable.girl_image,
                    modifier = Modifier
                        .align(BiasAlignment(horizontalBias = 0.45f, verticalBias = 0f))
                        .offset(y = (-4).dp)
                        .rotate(-16.84f)
                )

                // 5. Notification/Bell icon (Far Right)
                // Horizontal Bias 0.9f puts it near the right edge (End)
                FloatingIcon(
                    image = R.drawable.snapchat_image,
                    tint = Color.Black,
                    bgColor = SnapchatYellow,
                    size = 25.dp,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(BiasAlignment(horizontalBias = 0.9f, verticalBias = 0f))
                        .offset(y = (-8).dp)
                        .rotate(15.11f)
                )


            }


        }

    }

}


@Composable

fun FloatingPhotoCard(
    @DrawableRes drawableRes: Int = R.drawable.second_girl_image,
    modifier: Modifier = Modifier
) {


    Image(
        painter = painterResource(drawableRes),


        contentDescription = null,

        modifier = modifier
            .size(46.dp)
            .clip(RoundedCornerShape(8.dp))
    )


}

@Composable
fun DirectionalLine(
    modifier: Modifier = Modifier,
    direction: LineDirection = LineDirection.LeftToRight,
    color: Color = Color(0xFF145EFF),
    fraction: Float = 1.0f // Optional: 1.0f = full width, 0.5f = half width
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        // Calculate the effective width based on the fraction
        val lineLength = size.width * fraction

        val startOffset: Offset
        val endOffset: Offset

        // 2. Determine coordinates based on direction
        if (direction == LineDirection.LeftToRight) {
            // Start at 0 (Left), draw towards Right
            startOffset = Offset(0f, 0f)
            endOffset = Offset(lineLength, 0f)
        } else {
            // Start at Width (Right), draw towards Left
            startOffset = Offset(size.width, 0f)
            endOffset = Offset(size.width - lineLength, 0f)
        }

        drawLine(
            color = color,
            start = startOffset,
            end = endOffset,
            strokeWidth = size.height
        )
    }
}

@Composable
fun SimpleHorizontalLine(modifier: Modifier) {
    Canvas(modifier = modifier.size(width = 100.dp, height = 1.dp)) {
        // Draw a line from (0, 25) to (300, 25)
        // Since y=25 for both, the line is horizontal
        drawLine(
            color = Color.Blue,
            start = Offset(x = 50f, y = size.height / 2),
            end = Offset(x = size.width, y = size.height / 2),
            strokeWidth = 1.dp.toPx()
        )
    }
}

@Composable

fun FloatingIcon(

    @DrawableRes
    image: Int,

    tint: Color,

    bgColor: Color,

    size: Dp,

    modifier: Modifier = Modifier

) {
    Image(

        painter = painterResource(image),
        contentDescription = null,
        modifier = modifier.size(size)

    )

}


// --- 5. Detailed Component: Features ---

@Composable

fun FeatureRow(@DrawableRes icon: Int, text: String, showDivider: Boolean = true) {

    Column(

        modifier = Modifier
            .padding(horizontal = 16.dp)

            .fillMaxWidth()

    ) {

        Row(

            modifier = Modifier

                .fillMaxWidth()

                .padding(vertical = 12.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(

                painter = painterResource(icon),

                contentDescription = null,

                tint = BrandBlue,

                modifier = Modifier.size(20.dp)

            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(

                text = text,

                fontSize = 14.sp,

                color = TextBlack,

                fontWeight = FontWeight.Normal

            )

        }

        if (showDivider) {

            HorizontalDivider(

                color = DividerColor,

                thickness = 1.dp

            )

        }

    }

}


// --- 6. Detailed Component: Plan Card ---

@Composable

fun PlanCard(

    title: String,
    price: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val borderColor = if (isSelected) BrandBlue else Color(0xFFE0E0E0)

    val borderWidth = if (isSelected) 2.dp else 1.dp



    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier

            .fillMaxWidth()

            .border(

                width = borderWidth,

                color = borderColor,

                shape = RoundedCornerShape(8.dp)

            )

            .clickable { onClick() }

    ) {

        Row(

            modifier = Modifier

                .padding(horizontal = 20.dp, vertical = 14.dp)

                .fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(

                    imageVector = icon,

                    contentDescription = null,

                    tint = iconTint,

                    modifier = Modifier.size(15.dp)

                )

            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                letterSpacing = 1.2.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFF555555),
                modifier = Modifier.weight(1f)
            )

            Row(

                verticalAlignment = Alignment.Bottom

            ) {

                Text(

                    text = "USD",

                    fontSize = 12.sp,

                    fontWeight = FontWeight.SemiBold,

                    color = Color(0xFF333333),

                    modifier = Modifier.padding(end = 8.dp),
                    textAlign = TextAlign.Center

                )

                Text(

                    text = price,

                    fontSize = 20.sp,

                    fontWeight = FontWeight.Bold,

                    color = Color(0xFF111111)

                )

            }

        }

    }

}


@Composable

fun Badge60Percent(modifier: Modifier = Modifier) {

    Surface(

        color = BrandBlue,

        shape = RoundedCornerShape(8.dp),

        modifier = modifier

    ) {


        Text(

            text = "60% OFF",

            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)

        )
    }
}

@Preview(showBackground = true, showSystemUi = true)

@Composable

fun PremiumScreenDesignPreview() {

    MyPracticeApplicationTheme {

        PremiumScreen(onClose = {}, onNavigateToTrial = {})

    }


}