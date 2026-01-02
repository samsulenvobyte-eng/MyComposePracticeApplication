package com.example.mypracticeapplication.ui.screens.compose_lab.side_effects

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * DisposableEffectExampleScreen - A comprehensive tutorial on DisposableEffect
 *
 * This screen teaches:
 * 1. THE PROBLEM: Resources that need cleanup (listeners, callbacks, observers)
 * 2. THE FIX: How DisposableEffect handles setup AND cleanup
 * 3. MENTAL MODEL: When onDispose is called
 * 4. REAL-LIFE CASES: Practical examples
 * 5. COMPARISON: DisposableEffect vs LaunchedEffect
 */
@Composable
fun DisposableEffectExampleScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Text(
            text = "🧹 DisposableEffect Deep Dive",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Managing resources that need cleanup in Compose",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        // ══════════════════════════════════════════════════════════════
        // SECTION 1: THE PROBLEM
        // ══════════════════════════════════════════════════════════════
        TheProblemSection()

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

        // ══════════════════════════════════════════════════════════════
        // SECTION 2: THE FIX WITH DISPOSABLEEFFECT
        // ══════════════════════════════════════════════════════════════
        TheFixSection()

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

        // ══════════════════════════════════════════════════════════════
        // SECTION 3: MENTAL MODEL
        // ══════════════════════════════════════════════════════════════
        MentalModelSection()

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

        // ══════════════════════════════════════════════════════════════
        // SECTION 4: LIFECYCLE OBSERVER DEMO
        // ══════════════════════════════════════════════════════════════
        LifecycleObserverDemoSection()

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

        // ══════════════════════════════════════════════════════════════
        // SECTION 5: REAL-LIFE USE CASES
        // ══════════════════════════════════════════════════════════════
        RealLifeUseCasesSection()

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

        // ══════════════════════════════════════════════════════════════
        // SECTION 6: COMPARISON WITH LAUNCHEDEFFECT
        // ══════════════════════════════════════════════════════════════
        ComparisonSection()

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION 1: THE PROBLEM
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun TheProblemSection() {
    SectionCard(
        title = "❌ The Problem",
        backgroundColor = Color(0xFFFFEBEE),
        accentColor = Color(0xFFC62828)
    ) {
        Text(
            text = "What happens when you register a listener/observer but never unregister it?",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFC62828)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CodeBlock(
            title = "🐛 Buggy Code (Memory Leak!):",
            code = """
@Composable
fun BuggyScreen(lifecycleOwner: LifecycleOwner) {
    // ❌ BUG: Observer added but NEVER removed!
    val observer = LifecycleEventObserver { _, event ->
        Log.d("Lifecycle", "Event: ${'$'}event")
    }
    
    lifecycleOwner.lifecycle.addObserver(observer)
    
    // When this composable leaves, observer is STILL attached!
    // = Memory leak + zombie callbacks
}
            """.trimIndent(),
            backgroundColor = Color(0xFFFFCDD2)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Why is this a bug?",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC62828)
        )

        Spacer(modifier = Modifier.height(8.dp))

        BulletPoint("Observer stays attached even after composable is removed")
        BulletPoint("Memory leak: Objects can't be garbage collected")
        BulletPoint("Zombie callbacks: Code runs on a \"dead\" screen")
        BulletPoint("Crashes: Accessing disposed resources")
        BulletPoint("Same problem with: sensors, broadcast receivers, callbacks, etc.")
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION 2: THE FIX WITH DISPOSABLEEFFECT
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun TheFixSection() {
    SectionCard(
        title = "✅ The Fix: DisposableEffect",
        backgroundColor = Color(0xFFE8F5E9),
        accentColor = Color(0xFF2E7D32)
    ) {
        Text(
            text = "DisposableEffect provides both setup AND cleanup in one place.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CodeBlock(
            title = "✅ Fixed Code:",
            code = """
@Composable
fun FixedScreen(lifecycleOwner: LifecycleOwner) {
    DisposableEffect(lifecycleOwner) {
        // ✅ SETUP: Runs when entering composition
        val observer = LifecycleEventObserver { _, event ->
            Log.d("Lifecycle", "Event: ${'$'}event")
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        
        // ✅ CLEANUP: Runs when leaving composition
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            Log.d("Lifecycle", "Observer removed!")
        }
    }
}
            """.trimIndent(),
            backgroundColor = Color(0xFFC8E6C9)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "How DisposableEffect works:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.height(8.dp))

        BulletPoint("Takes key(s) like LaunchedEffect", Color(0xFF2E7D32))
        BulletPoint("Block runs on enter (setup your resource)", Color(0xFF2E7D32))
        BulletPoint("onDispose {} block runs on leave (cleanup)", Color(0xFF2E7D32))
        BulletPoint("If key changes: onDispose → then re-run setup", Color(0xFF2E7D32))
        BulletPoint("MUST return onDispose {} at the end", Color(0xFF2E7D32))
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION 3: MENTAL MODEL
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun MentalModelSection() {
    SectionCard(
        title = "🧠 Mental Model",
        backgroundColor = Color(0xFFF3E5F5),
        accentColor = Color(0xFF7B1FA2)
    ) {
        Text(
            text = "Understanding when setup and cleanup run:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7B1FA2)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Timeline visualization
        TimelineStep(
            number = "1",
            title = "Enter Composition",
            description = "Setup block runs → resource registered",
            color = Color(0xFF4CAF50)
        )

        TimelineStep(
            number = "2",
            title = "Recomposition (same key)",
            description = "Nothing happens! Setup NOT re-run.",
            color = Color(0xFF2196F3)
        )

        TimelineStep(
            number = "3",
            title = "Key Changes",
            description = "onDispose runs → then setup runs again with new key",
            color = Color(0xFFFF9800)
        )

        TimelineStep(
            number = "4",
            title = "Leave Composition",
            description = "onDispose runs → resource cleaned up",
            color = Color(0xFFF44336)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CodeBlock(
            title = "The Pattern:",
            code = """
DisposableEffect(key) {
    // SETUP: Register listener/observer/callback
    val listener = createListener()
    register(listener)
    
    onDispose {
        // CLEANUP: Always called before leaving
        unregister(listener)
    }
}
            """.trimIndent(),
            backgroundColor = Color(0xFFE1BEE7)
        )
    }
}

@Composable
private fun TimelineStep(
    number: String,
    title: String,
    description: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION 4: LIFECYCLE OBSERVER DEMO
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun LifecycleObserverDemoSection() {
    SectionCard(
        title = "🔄 Live Lifecycle Observer Demo",
        backgroundColor = Color(0xFFFFF3E0),
        accentColor = Color(0xFFE65100)
    ) {
        Text(
            text = "Toggle the switch to see DisposableEffect in action:",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFE65100)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LifecycleObserverDemo()
    }
}

@Composable
private fun LifecycleObserverDemo() {
    var showObserver by remember { mutableStateOf(false) }
    var eventLog by remember { mutableStateOf(listOf("Toggle switch to start...")) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Enable Lifecycle Observer:",
                fontWeight = FontWeight.SemiBold
            )
            Switch(
                checked = showObserver,
                onCheckedChange = { 
                    showObserver = it
                    eventLog = eventLog + if (it) "⬇️ Switch ON" else "⬇️ Switch OFF"
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Conditionally render the observer
        if (showObserver) {
            LifecycleObserverChild(
                onEvent = { event ->
                    eventLog = eventLog + event
                }
            )
        }

        // Event log display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "📋 Event Log:",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
                Spacer(modifier = Modifier.height(4.dp))
                eventLog.takeLast(10).forEach { event ->
                    Text(
                        text = event,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Button(
            onClick = { eventLog = listOf("Log cleared") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Clear Log")
        }
    }
}

@Composable
private fun LifecycleObserverChild(
    onEvent: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        onEvent("🟢 SETUP: Observer registered!")

        val observer = LifecycleEventObserver { _, event ->
            onEvent("📍 Lifecycle: ${event.name}")
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            onEvent("🔴 DISPOSE: Observer removed!")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "✅ Lifecycle Observer Active",
            modifier = Modifier.padding(12.dp),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION 5: REAL-LIFE USE CASES
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun RealLifeUseCasesSection() {
    SectionCard(
        title = "💼 Real-Life Use Cases",
        backgroundColor = Color(0xFFE3F2FD),
        accentColor = Color(0xFF1565C0)
    ) {
        // Use Case 1: Lifecycle Observer
        UseCaseItem(
            emoji = "🔄",
            title = "Lifecycle Observer",
            code = """
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, e ->
        if (e == Lifecycle.Event.ON_RESUME) refresh()
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { 
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}
            """.trimIndent()
        )

        // Use Case 2: Back Handler
        UseCaseItem(
            emoji = "⬅️",
            title = "Back Press Handler",
            code = """
DisposableEffect(Unit) {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() { /* ... */ }
    }
    dispatcher.addCallback(callback)
    onDispose { callback.remove() }
}
            """.trimIndent()
        )

        // Use Case 3: Sensor Listener
        UseCaseItem(
            emoji = "📱",
            title = "Sensor Listener",
            code = """
DisposableEffect(Unit) {
    val listener = object : SensorEventListener { /*...*/ }
    sensorManager.registerListener(listener, sensor, rate)
    onDispose {
        sensorManager.unregisterListener(listener)
    }
}
            """.trimIndent()
        )

        // Use Case 4: Broadcast Receiver
        UseCaseItem(
            emoji = "📻",
            title = "Broadcast Receiver",
            code = """
DisposableEffect(Unit) {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {}
    }
    context.registerReceiver(receiver, filter)
    onDispose { context.unregisterReceiver(receiver) }
}
            """.trimIndent()
        )

        // Use Case 5: Map/View Cleanup
        UseCaseItem(
            emoji = "🗺️",
            title = "MapView / External View",
            code = """
DisposableEffect(Unit) {
    mapView.onCreate(savedInstanceState)
    onDispose {
        mapView.onDestroy()  // Cleanup resources
    }
}
            """.trimIndent()
        )
    }
}

@Composable
private fun UseCaseItem(
    emoji: String,
    title: String,
    code: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "$emoji $title",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1565C0)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFBBDEFB), RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Text(
                text = code,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                lineHeight = 14.sp
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION 6: COMPARISON WITH LAUNCHEDEFFECT
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun ComparisonSection() {
    SectionCard(
        title = "⚖️ DisposableEffect vs LaunchedEffect",
        backgroundColor = Color(0xFFFFFDE7),
        accentColor = Color(0xFFF9A825)
    ) {
        ComparisonTable()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Quick Decision:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF9A825)
        )

        Spacer(modifier = Modifier.height(8.dp))

        BulletPoint("Need to call suspend functions? → LaunchedEffect", Color(0xFF6200EE))
        BulletPoint("Need to cleanup when leaving? → DisposableEffect", Color(0xFF2E7D32))
        BulletPoint("Both? → DisposableEffect + launch coroutine inside", Color(0xFFE65100))
    }
}

@Composable
private fun ComparisonTable() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFF9A825), RoundedCornerShape(8.dp))
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF9C4))
                .padding(8.dp)
        ) {
            Text(
                text = "Feature",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                fontSize = 12.sp
            )
            Text(
                text = "LaunchedEffect",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                color = Color(0xFF6200EE)
            )
            Text(
                text = "DisposableEffect",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                color = Color(0xFF2E7D32)
            )
        }

        ComparisonRow("Coroutine scope", "✅ Yes", "❌ No")
        ComparisonRow("Suspend functions", "✅ Yes", "❌ No*")
        ComparisonRow("Cleanup callback", "❌ No", "✅ onDispose")
        ComparisonRow("Register/Unregister", "❌ No", "✅ Perfect")
        ComparisonRow("One-time API call", "✅ Ideal", "⚠️ Overkill")
    }
}

@Composable
private fun ComparisonRow(
    feature: String,
    launched: String,
    disposable: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = feature,
            modifier = Modifier.weight(1f),
            fontSize = 11.sp
        )
        Text(
            text = launched,
            modifier = Modifier.weight(1f),
            fontSize = 11.sp
        )
        Text(
            text = disposable,
            modifier = Modifier.weight(1f),
            fontSize = 11.sp
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// HELPER COMPOSABLES
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun SectionCard(
    title: String,
    backgroundColor: Color,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
private fun CodeBlock(
    title: String,
    code: String,
    backgroundColor: Color
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                text = code,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun BulletPoint(text: String, color: Color = Color(0xFFC62828)) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "• ",
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DisposableEffectExampleScreenPreview() {
    DisposableEffectExampleScreen()
}
