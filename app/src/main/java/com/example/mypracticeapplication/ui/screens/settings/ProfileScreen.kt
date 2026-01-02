package com.example.mypracticeapplication.ui.screens.settings

import android.app.Activity
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.theme.MyPracticeApplicationTheme

//Todo: use foreach loop for feature list
//Todo: remove purchase button ripple effect

enum class LineDirection {
    LeftToRight,
    RightToLeft
}

private val BrandBlue = Color(0xFF3366FF)
private val TextBlack = Color(0xFF1A1A1A)
private val DividerColor = Color(0xFFEEEEEE)
private val StarBg = Color(0xFFFFF8E1)


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

@Composable

fun PremiumScreen(
    onClose: () -> Unit = {},
    onNavigateToTrial: () -> Unit = {}
) {
    var uiState by remember { mutableStateOf(PremiumUiState()) }

    StatusBarIcons(true)

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
    modifier: Modifier = Modifier,
    onEvent: (PremiumUiEvent) -> Unit

) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = Color.White
    ) { paddingValues ->

        BoxWithConstraints(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()

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
            icon = R.drawable.ic_box_monthly,
            iconBgColor = Color(0x1AF554FF),
            isSelected = state.selectedPlan == PlanType.Monthly,
            onClick = { onEvent(PremiumUiEvent.OnPlanSelected(PlanType.Monthly)) }
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            PlanCard(
                title = "Yearly Plan",
                price = state.yearlyPrice,
                icon = R.drawable.ic_star,
                iconBgColor = StarBg,
                isSelected = state.selectedPlan == PlanType.Yearly,
                modifier = Modifier.padding(top = 8.dp),
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


    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFF554FF), Color(0xFF434AFF))
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {

        Button(
            onClick = { onEvent(PremiumUiEvent.OnStartTrialClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = gradient, shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
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

            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),

            textAlign = TextAlign.Center,

            )
    }
}

@Composable
fun PremiumHeaderSection(onClose: () -> Unit, modifier: Modifier = Modifier) {

    val gradient45 = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF554FF),
            Color(0xFF434AFF)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .fillMaxWidth()
            .background(
                brush = gradient45,

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

            Spacer(modifier = Modifier.height(height = 19.dp))

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
                    color = Color(0xFF1861FF),
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
                    color = Color(0xFF1861FF),
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
                    color = Color(0xFFF554FF),
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
                    color = Color(0xFFF554FF),
                    direction = LineDirection.LeftToRight,
                    fraction = 0.28f
                )

                FloatingIcon(
                    image = R.drawable.facebook_image,
                    size = 25.dp,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(BiasAlignment(horizontalBias = -0.9f, verticalBias = 0f))
                        .offset(y = (-8).dp)
                        .rotate(-17.29f)
                )

                FloatingPhotoCard(
                    drawableRes = R.drawable.second_girl_image,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(BiasAlignment(horizontalBias = -0.45f, verticalBias = 0f))
                        .offset(y = (-4).dp)
                        .rotate(10.59f)
                )

                FloatingIcon(
                    image = R.drawable.instagram_image,
                    size = 29.dp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .rotate(26f)
                        .padding()
                )
                FloatingPhotoCard(

                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .align(BiasAlignment(horizontalBias = 0.45f, verticalBias = 0f))
                        .offset(y = (-4).dp)
                        .rotate(-16.84f),
                    drawableRes = R.drawable.girl_image
                )

                FloatingIcon(
                    image = R.drawable.snapchat_image,
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
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int = R.drawable.second_girl_image,

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
            .height(0.4.dp)
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
fun FloatingIcon(
    @DrawableRes
    image: Int,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Image(

        painter = painterResource(image),
        contentDescription = null,
        modifier = modifier.size(size)
    )
}

@Composable
fun FeatureRow(
    @DrawableRes icon: Int,
    text: String,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF554FF), Color(0xFF434AFF))
    )
    Column(
        modifier = modifier
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
                modifier = Modifier
                    .size(20.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            // 4. Draw the actual icon
                            drawContent()
                            // 5. Draw the gradient ON TOP, keeping only the overlapping parts (SrcIn)
                            drawRect(gradient, blendMode = BlendMode.SrcIn)
                        }
                    }
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

@Composable
fun PlanCard(
    iconBgColor: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    title: String = "Monthly Plan",
    price: String = "19.99",
    @DrawableRes icon: Int = R.drawable.ic_star,
    onClick: () -> Unit = {}
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF554FF), Color(0xFF434AFF))
    )
    val borderColor = if (isSelected) BrandBlue else Color(0xFFE0E0E0) // Todo: why am I using if statement here?
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = borderWidth,
                brush = if (isSelected) gradient else SolidColor(borderColor),
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

                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color(0xFFF554FF),
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
fun Badge60Percent(modifier: Modifier = Modifier, offPercent: String = "60% OFF") {


    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF554FF), Color(0xFF434AFF))
    )

    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.paint(
            painter = painterResource(R.drawable.off_background),
            contentScale = ContentScale.FillBounds
        )
    ) {
        Text(
            text = offPercent,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            modifier = Modifier.padding(horizontal = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StatusBarIcons(useDarkIcons: Boolean) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        DisposableEffect(Unit) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            val originalState = insetsController.isAppearanceLightStatusBars
            insetsController.isAppearanceLightStatusBars = useDarkIcons
            onDispose {
                insetsController.isAppearanceLightStatusBars = originalState
            }
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:width=1080px,height=2400px"
)
@Composable
private fun PremiumScreenDesignPreview() {

    MyPracticeApplicationTheme {
        PremiumScreen(onClose = {}, onNavigateToTrial = {})
    }


}