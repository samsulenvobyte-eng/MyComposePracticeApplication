package com.example.mycomposablepractice.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.border

import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.layout.BoxWithConstraints

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Close

import androidx.compose.material.icons.outlined.*

import androidx.compose.material.icons.rounded.Star

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip

import androidx.compose.ui.draw.rotate

import androidx.compose.ui.draw.shadow
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


// --- 1. Design System Constants ---

private val BrandBlue = Color(0xFF3366FF)

private val BrandBlueGradientStart = Color(0xFF4A7DFF)

private val BrandBlueGradientEnd = Color(0xFF2855E0)

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

// horizontalAlignment = Alignment.CenterHorizontally

            ) {

// 1. Header Section (Unlock PRO Access)

                PremiumHeaderSection(

                    onClose = { onEvent(PremiumUiEvent.OnCloseClicked) }

                )


// 2. Feature List

                FeatureList()

                Spacer(modifier = Modifier.height(20.dp))

// Flexible spacer to center PurchaseOptions between FeatureList and Footer

                Spacer(modifier = Modifier.weight(1f))


// 3. Plans Section

                PurchaseOptions(state, onEvent)


// Flexible spacer to center PurchaseOptions between FeatureList and Footer

                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.height(20.dp))

// 4. CTA Button & Footer

                Footer(onEvent)


//Spacer(modifier = Modifier.height(12.dp))

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

        FeatureRow(Icons.Outlined.Tune, "Advanced Compression")

        FeatureRow(Icons.Outlined.PhotoLibrary, "Batch Compression")

        FeatureRow(Icons.Outlined.InsertDriveFile, "Target File Size")

        FeatureRow(Icons.Outlined.AspectRatio, "Batch Resize & Convert")

        FeatureRow(Icons.Outlined.CropRotate, "Batch Crop & Fit Photo")

        FeatureRow(Icons.Outlined.SelectAll, "Select All Photos at once")

        FeatureRow(Icons.Outlined.Block, "Ad-free Experience", showDivider = false)

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

// Monthly Plan

        PlanCard(

            title = "Monthly Plan",

            price = state.monthlyPrice,

            icon = Icons.Outlined.Inventory2,

            iconBgColor = BoxBg,

            iconTint = BrandBlue,

            isSelected = state.selectedPlan == PlanType.Monthly,

            onClick = { onEvent(PremiumUiEvent.OnPlanSelected(PlanType.Monthly)) }

        )


// Yearly Plan (With Badge)

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


// The "60% OFF" Badge

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

                modifier = Modifier.padding(vertical = 10.dp),

                text = "Start Free Trial",

                fontSize = 17.sp,

                fontWeight = FontWeight.SemiBold,

                color = Color.White,

                )

        }



        Spacer(modifier = Modifier.height(12.dp))



        Text(

            modifier = Modifier.fillMaxWidth(),

            text = "Recurrent billing. Cancel anytime. You will be\ncharged after trial ends unless canceled.",

            style = MaterialTheme.typography.bodySmall,

            color = TextGrey,

            textAlign = TextAlign.Center,

            )

    }

}


// --- 4. Detailed Component: Header ---

@Composable

fun PremiumHeaderSection(onClose: () -> Unit) {

    Box(

        modifier = Modifier

            .padding(start = 16.dp, end = 16.dp, top = 8.dp)

            .fillMaxWidth()

            .background(

                brush = Brush.verticalGradient(

                    colors = listOf(BrandBlueGradientStart, BrandBlueGradientEnd)

                ),

                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)

            )

    ) {


        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {

// Title

            Box(

                modifier = Modifier.fillMaxWidth()

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

                       // modifier = Modifier.size(24.dp)

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



            Spacer(modifier = Modifier.height(height = 32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // Optional: Constrain max width for tablets so they don't drift TOO far apart
                    //.widthIn(max = 300.dp)
                    //.height(100.dp) // Ensure the box has height for the vertical alignment to work
            ) {

                Canvas(
                    modifier = Modifier
                        .size(60.dp)
                        .align(BiasAlignment(horizontalBias = 0.55f, verticalBias = 0f))


                ) {

                    drawCircle(
                        color = Color(0xFF3366FF),
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
                        color = Color(0xFF3366FF),
                        radius = 40.dp.toPx(),
                        center = Offset(x = size.width / 2, y = 200f)
                    )
                }


                // 1. Facebook icon (Far Left)
                // Horizontal Bias -0.9f puts it near the left edge (Start)
                FloatingIcon(
                    icon = Icons.Outlined.Facebook,
                    tint = Color.White,
                    bgColor = FacebookBlue,
                    size = 25.dp,
                    modifier = Modifier
                        .align(BiasAlignment(horizontalBias = -0.9f, verticalBias = 0f))
                        .offset(y = (-8).dp)
                        .rotate(-17.29f)
                )

                // 2. Photo Card 1 (Mid Left)
                // Horizontal Bias -0.45f puts it roughly halfway between center and left edge
                FloatingPhotoCard(
                    bgColor = Color(0xFFD4A574),
                    modifier = Modifier
                        .align(BiasAlignment(horizontalBias = -0.45f, verticalBias = 0f))
                        .offset(y = (-12).dp)
                        .rotate(10.59f)
                )

                // 3. Instagram icon (Dead Center)
                // Standard Alignment.BottomCenter is effectively Bias(0f, 1f)
                FloatingIcon(
                    icon = Icons.Outlined.CameraAlt,
                    tint = Color.White,
                    bgColor = InstagramPink,
                    size = 29.dp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp)
                )

                // 4. Photo Card 2 (Mid Right)
                // Horizontal Bias 0.45f puts it roughly halfway between center and right edge
                FloatingPhotoCard(
                    bgColor = Color(0xFF8BC0E0),
                    modifier = Modifier
                        .align(BiasAlignment(horizontalBias = 0.45f, verticalBias = 0f))
                        .offset(y = (-12).dp)
                        .rotate(-16.84f)
                )

                // 5. Notification/Bell icon (Far Right)
                // Horizontal Bias 0.9f puts it near the right edge (End)
                FloatingIcon(
                    icon = Icons.Outlined.Notifications,
                    tint = Color.Black,
                    bgColor = SnapchatYellow,
                    size = 25.dp,
                    modifier = Modifier
                        .align(BiasAlignment(horizontalBias = 0.9f, verticalBias = 0f))
                        .offset(y = (-8).dp)
                        .rotate(15.11f)
                )





            }


        }

    }

}


@Composable

fun FloatingPhotoCard(bgColor: Color, modifier: Modifier = Modifier) {


    Image(
        painter = painterResource(R.drawable.ic_launcher_foreground),


        contentDescription = null,

        modifier = modifier
            .size(46.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
    )


}


@Composable

fun FloatingIcon(

    icon: ImageVector,

    tint: Color,

    bgColor: Color,

    size: Dp,

    modifier: Modifier = Modifier

) {

    Box(

        modifier = modifier

            .size(size)
            .background(bgColor, CircleShape),

        contentAlignment = Alignment.Center

    ) {

        Icon(

            imageVector = icon,

            contentDescription = null,

            tint = tint,

            modifier = Modifier.size(size * 0.55f)

        )

    }

}


// --- 5. Detailed Component: Features ---

@Composable

fun FeatureRow(icon: ImageVector, text: String, showDivider: Boolean = true) {

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

                imageVector = icon,

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

        elevation = CardDefaults.cardElevation(

            defaultElevation = if (isSelected) 4.dp else 2.dp

        ),

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

                .padding(horizontal = 16.dp, vertical = 14.dp)

                .fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically

        ) {

// Icon Box

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


// Title

            Text(

                text = title,

                fontSize = 14.sp,

                fontWeight = FontWeight.Medium,

                color = TextBlack,

                modifier = Modifier.weight(1f)

            )


// Price

            Row(

                verticalAlignment = Alignment.Bottom

            ) {

                Text(

                    text = "USD",

                    fontSize = 12.sp,

                    fontWeight = FontWeight.Medium,

                    color = TextGrey,

                    modifier = Modifier.padding(bottom = 3.dp, end = 8.dp)

                )

                Text(

                    text = price,

                    fontSize = 20.sp,

                    fontWeight = FontWeight.Bold,

                    color = TextBlack

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

        shadowElevation = 2.dp,

        modifier = modifier

    ) {

        Text(

            text = "60% OFF",

            color = Color.White,

            fontSize = 9.sp,

            fontWeight = FontWeight.Bold,

            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)

        )

    }

}


// --- Preview ---

@Preview(showBackground = true, showSystemUi = true)

@Composable

fun PremiumScreenDesignPreview() {

    MyPracticeApplicationTheme {

        PremiumScreen(onClose = {}, onNavigateToTrial = {})

    }


}