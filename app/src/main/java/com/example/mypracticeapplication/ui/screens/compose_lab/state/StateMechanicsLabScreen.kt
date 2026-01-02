package com.example.mypracticeapplication.ui.screens.compose_lab.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

/**
 * StateMechanicsLab demonstrates the behavioral differences between:
 * 1. remember vs rememberSaveable (configuration change survival)
 * 2. Raw boolean vs derivedStateOf (recomposition efficiency)
 *
 * This is an interactive lab where you can SEE the differences in real-time.
 */
@Composable
fun StateMechanicsLabScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Text(
            text = "State Mechanics Lab",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Interactive experiments to understand Compose state",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ══════════════════════════════════════════════════════════════
        // ZONE A: THE SURVIVAL TEST (Configuration Changes)
        // ══════════════════════════════════════════════════════════════
        SurvivalTestZone()

        // ══════════════════════════════════════════════════════════════
        // ZONE B: THE EFFICIENCY TEST (Recomposition Optimization)
        // ══════════════════════════════════════════════════════════════
        EfficiencyTestZone()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * ZONE A: Compares remember vs rememberSaveable behavior during rotation.
 *
 * KEY CONCEPT:
 * - `remember`: Stores value in Composition. Lost on configuration change (rotation).
 * - `rememberSaveable`: Stores value in Bundle. Survives configuration changes.
 *
 * When you rotate the device:
 * - The Activity is destroyed and recreated
 * - `remember` loses its value (Composition is destroyed)
 * - `rememberSaveable` restores from savedInstanceState Bundle
 */
@Composable
private fun SurvivalTestZone() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3E5F5) // Light purple
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Zone Title
            Text(
                text = "🔄 Zone A: The Survival Test",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A1B9A)
            )

            Text(
                text = "Increment both counters, then rotate your device to see who survives!",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF7B1FA2)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // LEFT: remember (Dies on Rotate)
                CounterCard(
                    modifier = Modifier.weight(1f),
                    title = "RAM",
                    subtitle = "(Dies on Rotate)",
                    useRememberSaveable = false,
                    backgroundColor = Color(0xFFFFCDD2), // Light red - BAD
                    accentColor = Color(0xFFC62828)
                )

                // RIGHT: rememberSaveable (Survives Rotate)
                CounterCard(
                    modifier = Modifier.weight(1f),
                    title = "Disk",
                    subtitle = "(Survives Rotate)",
                    useRememberSaveable = true,
                    backgroundColor = Color(0xFFC8E6C9), // Light green - GOOD
                    accentColor = Color(0xFF2E7D32)
                )
            }

            // Explanation
            Text(
                text = "💡 remember stores in Composition memory (RAM)\n" +
                        "💾 rememberSaveable stores in Bundle (Disk)",
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                color = Color(0xFF6A1B9A)
            )
        }
    }
}

/**
 * Reusable counter card that demonstrates remember vs rememberSaveable.
 */
@Composable
private fun CounterCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    useRememberSaveable: Boolean,
    backgroundColor: Color,
    accentColor: Color
) {
    // THE KEY DIFFERENCE:
    // - remember: Value lost on configuration change
    // - rememberSaveable: Value persists across configuration changes
    var count by if (useRememberSaveable) {
        rememberSaveable { mutableIntStateOf(0) }
    } else {
        remember { mutableIntStateOf(0) }
    }

    Column(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = accentColor.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Text(
            text = "$count",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )

        Button(
            onClick = { count++ },
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("+1")
        }
    }
}

/**
 * ZONE B: Demonstrates recomposition efficiency with derivedStateOf.
 *
 * KEY CONCEPT - THE REAL DIFFERENCE:
 * - Passing a RAW PRIMITIVE (Float) that changes every frame → FORCES recomposition
 * - Passing a STABLE STATE OBJECT (State<Float>) → allows Compose to SKIP recomposition
 *
 * WHY?
 * Compose checks parameter equality to decide if recomposition is needed.
 * - Raw Float: 25.1f → 25.2f → 25.3f → each is a NEW value → recompose every frame!
 * - State<Float>: Same object reference → Compose sees "no change" → SKIP!
 *
 * The derivedStateOf inside the optimized case only triggers when isHigh flips.
 */
@Composable
private fun EfficiencyTestZone() {
    // CRUCIAL: Use the State object directly, not delegated property
    // This allows us to pass the STATE OBJECT vs the RAW VALUE
    val sliderState = remember { mutableFloatStateOf(25f) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0) // Light orange
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Zone Title
            Text(
                text = "⚡ Zone B: The Efficiency Test",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100)
            )

            Text(
                text = "Drag the slider and watch the recomposition counters!",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFF57C00)
            )

            // Slider
            Column {
                Text(
                    text = "Value: ${sliderState.floatValue.toInt()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE65100)
                )

                Slider(
                    value = sliderState.floatValue,
                    onValueChange = { sliderState.floatValue = it },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFE65100),
                        activeTrackColor = Color(0xFFE65100)
                    )
                )

                Text(
                    text = "Threshold: 50 (shows warning when value > 50)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF57C00)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // THE COMPARISON - THIS IS THE KEY DIFFERENCE!
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // LEFT: Inefficient - pass RAW PRIMITIVE VALUE
                // Every slider tick = new Float value = FORCED recomposition!
                InefficientCase(
                    modifier = Modifier.weight(1f),
                    rawValue = sliderState.floatValue // RAW FLOAT - changes every pixel!
                )

                // RIGHT: Optimized - pass STABLE STATE OBJECT
                // State object reference NEVER changes = Compose SKIPS recomposition!
                OptimizedCase(
                    modifier = Modifier.weight(1f),
                    sliderState = sliderState // STATE OBJECT - stable reference!
                )
            }

            // Explanation
            Text(
                text = "❌ Raw Float: 25.1→25.2→25.3 = new value each frame → recomposes ALWAYS\n" +
                        "✅ State<Float>: Same object reference → Compose SKIPS until isHigh changes",
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                color = Color(0xFFE65100)
            )
        }
    }
}

/**
 * INEFFICIENT CASE: Raw Float passed directly.
 *
 * WHY IT RECOMPOSES EVERY FRAME:
 * - The parameter `rawValue: Float` is a primitive that changes on every slider tick
 * - Compose sees: 25.1f → 25.2f → 25.3f = "new value!" → MUST recompose!
 * - Even dragging 1 pixel causes a recomposition because the Float changes
 * - The counter will go CRAZY as you drag the slider
 */
@Composable
private fun InefficientCase(
    modifier: Modifier = Modifier,
    rawValue: Float // RAW FLOAT - changes every single pixel!
) {
    // Calculate isHigh INSIDE this composable
    // This composable is FORCED to run on every slider change
    // because its input parameter (rawValue) changes constantly
    val isHigh = rawValue > 50

    RecompositionTracker(
        modifier = modifier,
        title = "❌ Inefficient",
        subtitle = "(Raw Float)",
        showWarning = isHigh,
        backgroundColor = Color(0xFFFFCDD2), // Light red
        accentColor = Color(0xFFC62828)
    )
}

/**
 * OPTIMIZED CASE: Stable State<Float> object passed.
 *
 * WHY IT SKIPS RECOMPOSITION:
 * - The parameter `sliderState: State<Float>` is an OBJECT REFERENCE
 * - The REFERENCE never changes (same object in memory)
 * - Compose sees: same reference → "nothing changed!" → SKIP recomposition!
 * - Only derivedStateOf reads the value and triggers when isHigh flips
 * - The counter will stay LOW - only incrementing when crossing threshold
 */
@Composable
private fun OptimizedCase(
    modifier: Modifier = Modifier,
    sliderState: androidx.compose.runtime.State<Float> // STABLE STATE OBJECT!
) {
    // derivedStateOf: Only triggers recomposition when the computed value CHANGES
    // Reading sliderState.value is OBSERVED by derivedStateOf
    // Moving from 51 to 52 doesn't change "isHigh" (still true) → NO recomposition!
    val isHigh by remember {
        derivedStateOf { sliderState.value > 50 }
    }

    RecompositionTracker(
        modifier = modifier,
        title = "✅ Optimized",
        subtitle = "(State<Float>)",
        showWarning = isHigh,
        backgroundColor = Color(0xFFC8E6C9), // Light green
        accentColor = Color(0xFF2E7D32)
    )
}

/**
 * Visual tracker that shows how many times this composable has recomposed.
 *
 * THE TRICK:
 * - We use SideEffect to increment a counter on every recomposition
 * - SideEffect runs AFTER every successful recomposition
 * - The counter visually shows how "wasteful" or "efficient" the code is
 */
@Composable
private fun RecompositionTracker(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    showWarning: Boolean,
    backgroundColor: Color,
    accentColor: Color
) {
    // This counter increments on EVERY recomposition
    // NOT remembered - intentionally reset to show fresh count in recomposition tracking
    var recomposeCount by remember { mutableIntStateOf(0) }

    // SideEffect runs after every successful recomposition
    // This is our "proof" that recomposition happened
    SideEffect {
        recomposeCount++
    }

    // Random color that changes on each recomposition (visual jitter effect)
    val jitterColor = Color(
        red = Random.nextFloat() * 0.3f + 0.7f,
        green = Random.nextFloat() * 0.3f + 0.7f,
        blue = Random.nextFloat() * 0.3f + 0.7f,
        alpha = 1f
    )

    Column(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = accentColor.copy(alpha = 0.7f)
        )

        // Recomposition counter with jitter background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(jitterColor, RoundedCornerShape(8.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Recomposed: $recomposeCount",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // Warning indicator
        Text(
            text = if (showWarning) "⚠️ HIGH VALUE!" else "Normal",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (showWarning) Color(0xFFD32F2F) else Color(0xFF388E3C)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StateMechanicsLabScreenPreview() {
    StateMechanicsLabScreen()
}
