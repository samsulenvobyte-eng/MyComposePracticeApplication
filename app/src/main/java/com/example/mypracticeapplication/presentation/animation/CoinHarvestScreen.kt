package com.example.mypracticeapplication.presentation.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun CoinHarvestScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    // 1. Create the Host State
    val harvestState = rememberCoinHarvestState()

    // We wrap everything in the CoinHarvestHost
    CoinHarvestHost(
        state = harvestState,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Header(onNavigateBack = onNavigateBack)

            // Content Area
            Box(modifier = Modifier.fillMaxSize()) {
                
                // 2. The Target (Wallet)
                // We use onGloballyPositioned to inform the state where the target is.
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 32.dp, end = 24.dp)
                        .onGloballyPositioned { coordinates ->
                            val pos = coordinates.positionInRoot()
                            val size = coordinates.size
                            // Target center
                            harvestState.targetPosition = Offset(
                                pos.x + size.width / 2f,
                                pos.y + size.height / 2f
                            )
                        }
                ) {
                    Text("💰", fontSize = 48.sp)
                }

                // 3. Different Sources
                
                // Source A: Bottom Center Button
                Button(
                    onClick = { /* Handle click in pointerInput */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700)
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 48.dp)
                        .height(56.dp)
                        .fillMaxWidth(0.6f)
                        // Capture click position relative to root
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                // Calculate root position for the harvest spawn point
                                // The offset provided here is relative to the button.
                                // We need absolute root coordinates.
                                // Simplest way: use the known Globals of the button + offset, 
                                // OR just use center of button for simplicity.
                            }
                        }
                        // Cleaner approach: Just Click Handler + Center of Global Bounds?
                        // Let's use a simpler approach: onGloballyPositioned to define "center"
                        // and standard onClick to fire.
                        .onGloballyPositioned { coordinates ->
                             // Store this if needed, or calculate on click?
                             // Compose's onClick doesn't give coordinates.
                        }
                        // Combined approach:
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { tapOffset -> 
                                    // tapOffset is local. 
                                    // We can just act as if the user tapped the button.
                                    // But we need the GLOBAL position for the particle system.
                                    // We can't easily get global from tapOffset without the layout coordinates.
                                }
                            )
                        }
                ) {
                   // Let's simplify: Standard Button with global position tracking
                }
                
                // ---------------------------------------------------------
                // Working Implementation with Tracked Sources
                // ---------------------------------------------------------
                
                // Source 1: Center
                HarvestButton(
                    onHarvest = { pos -> harvestState.harvest(pos, 10) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 100.dp),
                    text = "HARVEST CENTER"
                )

                // Source 2: Left
                HarvestButton(
                    onHarvest = { pos -> harvestState.harvest(pos, 5) },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 24.dp),
                    text = "LEFT"
                )

                // Source 3: Right
                HarvestButton(
                    onHarvest = { pos -> harvestState.harvest(pos, 5) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp),
                    text = "RIGHT"
                )
                
                // Source 4: Random Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF333333)),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .harvestClickable { pos -> harvestState.harvest(pos, 20) }
                        .padding(16.dp)
                ) {
                    Text(
                        "Click Me (Card)",
                        color = Color.White,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// HELPER COMPONENTS
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun HarvestButton(
    onHarvest: (Offset) -> Unit,
    modifier: Modifier = Modifier,
    text: String = ""
) {
    // We track the global position of this button
    var centerPos = remember { Offset.Zero }

    Button(
        onClick = {
            // Trigger harvest from the tracked center
            onHarvest(centerPos)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                val pos = coordinates.positionInRoot()
                val size = coordinates.size
                centerPos = Offset(
                    pos.x + size.width / 2f,
                    pos.y + size.height / 2f
                )
            }
    ) {
        Text(text, color = Color.Black, fontWeight = FontWeight.Bold)
    }
}

// Extension to make anything harvest-clickable
@Composable
fun Modifier.harvestClickable(onHarvest: (Offset) -> Unit): Modifier {
    var centerPos = remember { Offset.Zero }
    
    return this
        .onGloballyPositioned { coordinates ->
            val pos = coordinates.positionInRoot()
            val size = coordinates.size
            centerPos = Offset(
                pos.x + size.width / 2f,
                pos.y + size.height / 2f
            )
        }
        .clickable { onHarvest(centerPos) }
}

@Composable
private fun Header(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Text(
            text = "Reusable Coin Harvest",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview
@Composable
private fun CoinHarvestScreenPreview() {
    CoinHarvestScreen()
}

