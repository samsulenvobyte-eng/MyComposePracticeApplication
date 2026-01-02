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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * LaunchedEffectExampleScreen - A comprehensive tutorial on LaunchedEffect
 *
 * This screen teaches:
 * 1. THE PROBLEM: Why direct side effects in composables are bugs
 * 2. THE FIX: How LaunchedEffect solves the problem
 * 3. MENTAL MODEL: How composition/recomposition works
 * 4. REAL-LIFE CASES: Practical examples
 * 5. WHEN NOT TO USE: Comparison with rememberCoroutineScope
 */
@Composable
fun LaunchedEffectExampleScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Text(
                text = "🚀 LaunchedEffect Deep Dive",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Understanding side effects in Jetpack Compose",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            // ══════════════════════════════════════════════════════════════
            // SECTION 1: THE PROBLEM
            // ══════════════════════════════════════════════════════════════
            TheProblemSection()

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            // ══════════════════════════════════════════════════════════════
            // SECTION 2: THE FIX WITH LAUNCHEDEFFECT
            // ══════════════════════════════════════════════════════════════
            TheFixSection(snackbarHostState)

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            // ══════════════════════════════════════════════════════════════
            // SECTION 3: MENTAL MODEL
            // ══════════════════════════════════════════════════════════════
            MentalModelSection()

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            // ══════════════════════════════════════════════════════════════
            // SECTION 4: KEY PARAMETER DEMO
            // ══════════════════════════════════════════════════════════════
            KeyParameterDemoSection()

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            // ══════════════════════════════════════════════════════════════
            // SECTION 5: REAL-LIFE USE CASES
            // ══════════════════════════════════════════════════════════════
            RealLifeUseCasesSection()

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            // ══════════════════════════════════════════════════════════════
            // SECTION 6: WHEN NOT TO USE
            // ══════════════════════════════════════════════════════════════
            WhenNotToUseSection()

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Snackbar host at the bottom
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
            text = "What happens when you trigger a side effect directly in a composable?",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFC62828)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Buggy code example
        CodeBlock(
            title = "🐛 Buggy Code:",
            code = """
@Composable
fun BuggyScreen(viewModel: MyViewModel) {
    val showError by viewModel.showError
    
    if (showError) {
        // ❌ BUG: This runs on EVERY recomposition!
        snackbarHostState.showSnackbar("Error!")
    }
    
    // ... rest of UI
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

        BulletPoint("Composables can recompose many times (state changes, parent recomposition)")
        BulletPoint("Each recomposition re-executes ALL code in the function")
        BulletPoint("Your snackbar/API call/navigation runs again and again!")
        BulletPoint("You might see 5 snackbars instead of 1, or make 5 API calls")

        Spacer(modifier = Modifier.height(16.dp))

        // Live demo of the problem
        TheProblemDemo()
    }
}

@Composable
private fun TheProblemDemo() {
    var counter by remember { mutableIntStateOf(0) }
    // Use a ref to count without triggering recomposition
    val buggyCallCountRef = remember { mutableIntStateOf(0) }
    // This state is only updated when counter changes, to display the count
    var displayCount by remember { mutableIntStateOf(0) }

    // This simulates the bug - code runs on every recomposition
    // We use a ref to avoid infinite loop, but the concept is the same
    SideEffect {
        buggyCallCountRef.intValue++
        Log.d("LaunchedEffectDemo", "Buggy side effect ran! Count: ${buggyCallCountRef.intValue}")
    }

    // Update display count when counter changes (after recomposition from button click)
    LaunchedEffect(counter) {
        displayCount = buggyCallCountRef.intValue
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔴 LIVE DEMO: The Bug",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Counter: $counter",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "\"Side effect\" ran: $displayCount times",
                color = Color(0xFFC62828),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "(Actual count: ${buggyCallCountRef.intValue})",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFC62828).copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { counter++ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
            ) {
                Text("Increment (triggers recomposition)")
            }

            Text(
                text = "Notice: Each click causes multiple recompositions!",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION 2: THE FIX WITH LAUNCHEDEFFECT
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun TheFixSection(snackbarHostState: SnackbarHostState) {
    SectionCard(
        title = "✅ The Fix: LaunchedEffect",
        backgroundColor = Color(0xFFE8F5E9),
        accentColor = Color(0xFF2E7D32)
    ) {
        Text(
            text = "LaunchedEffect runs your side effect safely in a coroutine scope tied to the composition lifecycle.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CodeBlock(
            title = "✅ Fixed Code:",
            code = """
@Composable
fun FixedScreen(viewModel: MyViewModel) {
    val showError by viewModel.showError
    
    // ✅ CORRECT: Runs only when showError CHANGES to true
    LaunchedEffect(showError) {
        if (showError) {
            snackbarHostState.showSnackbar("Error!")
            viewModel.clearError()
        }
    }
    
    // ... rest of UI
}
            """.trimIndent(),
            backgroundColor = Color(0xFFC8E6C9)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "How LaunchedEffect works:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.height(8.dp))

        BulletPoint("Takes one or more \"keys\" as parameters", Color(0xFF2E7D32))
        BulletPoint("Launches a coroutine when entering composition", Color(0xFF2E7D32))
        BulletPoint("Cancels and relaunches ONLY when key(s) change", Color(0xFF2E7D32))
        BulletPoint("Automatically cancels when leaving composition", Color(0xFF2E7D32))

        Spacer(modifier = Modifier.height(16.dp))

        // Live demo of the fix
        TheFixDemo(snackbarHostState)
    }
}

@Composable
private fun TheFixDemo(snackbarHostState: SnackbarHostState) {
    var showMessage by remember { mutableStateOf(false) }
    var launchCount by remember { mutableIntStateOf(0) }
    var otherState by remember { mutableIntStateOf(0) }

    // ✅ This only runs when showMessage changes!
    LaunchedEffect(showMessage) {
        if (showMessage) {
            launchCount++
            snackbarHostState.showSnackbar("Message shown! (Launch #$launchCount)")
            showMessage = false // Reset after showing
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🟢 LIVE DEMO: The Fix",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "LaunchedEffect ran: $launchCount times",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Other state: $otherState (changes don't trigger effect)",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { showMessage = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Show Snackbar")
                }

                Button(
                    onClick = { otherState++ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666666))
                ) {
                    Text("Other State +1")
                }
            }

            Text(
                text = "Notice: \"Other State\" changes don't trigger the effect!",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
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
            text = "Understanding the lifecycle:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7B1FA2)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Timeline visualization
        TimelineStep(
            number = "1",
            title = "Initial Composition",
            description = "LaunchedEffect enters → Coroutine LAUNCHES",
            color = Color(0xFF4CAF50)
        )

        TimelineStep(
            number = "2",
            title = "Recomposition (same key)",
            description = "LaunchedEffect key unchanged → Coroutine CONTINUES (no restart!)",
            color = Color(0xFF2196F3)
        )

        TimelineStep(
            number = "3",
            title = "Recomposition (key changes)",
            description = "LaunchedEffect key changed → Old coroutine CANCELLED, new one LAUNCHES",
            color = Color(0xFFFF9800)
        )

        TimelineStep(
            number = "4",
            title = "Leave Composition",
            description = "Composable removed → Coroutine CANCELLED automatically",
            color = Color(0xFFF44336)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CodeBlock(
            title = "Key Behavior:",
            code = """
// Key = Unit (or no key) → runs ONCE on enter
LaunchedEffect(Unit) { /* runs once */ }

// Key = someValue → runs when someValue changes
LaunchedEffect(userId) { loadUser(userId) }

// Multiple keys → runs when ANY key changes
LaunchedEffect(key1, key2) { /* ... */ }
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
// SECTION 4: KEY PARAMETER DEMO
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun KeyParameterDemoSection() {
    SectionCard(
        title = "🔑 Key Parameter Demo",
        backgroundColor = Color(0xFFFFF3E0),
        accentColor = Color(0xFFE65100)
    ) {
        Text(
            text = "See how different keys affect when LaunchedEffect runs:",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFE65100)
        )

        Spacer(modifier = Modifier.height(12.dp))

        KeyParameterDemo()
    }
}

@Composable
private fun KeyParameterDemo() {
    var userId by remember { mutableIntStateOf(1) }
    var otherValue by remember { mutableIntStateOf(0) }
    var effectLog by remember { mutableStateOf("Effect not started yet") }

    // This effect only runs when userId changes
    LaunchedEffect(userId) {
        effectLog = "Loading user $userId..."
        delay(500) // Simulate API call
        effectLog = "✅ User $userId loaded!"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LaunchedEffect(userId)",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "userId = $userId")
            Text(text = "otherValue = $otherValue")

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = effectLog,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { userId++ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                ) {
                    Text("Change userId")
                }

                Button(
                    onClick = { otherValue++ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666666))
                ) {
                    Text("Change other")
                }
            }

            Text(
                text = "Only userId changes trigger the effect!",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
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
        // Use Case 1: Snackbar
        UseCaseItem(
            emoji = "📢",
            title = "Snackbar / Toast",
            code = """
LaunchedEffect(errorMessage) {
    errorMessage?.let {
        snackbarHostState.showSnackbar(it)
        viewModel.clearError()
    }
}
            """.trimIndent()
        )

        // Use Case 2: Navigation
        UseCaseItem(
            emoji = "🧭",
            title = "One-time Navigation",
            code = """
LaunchedEffect(navigateToHome) {
    if (navigateToHome) {
        navController.navigate("home")
        viewModel.onNavigated()
    }
}
            """.trimIndent()
        )

        // Use Case 3: API Call
        UseCaseItem(
            emoji = "🌐",
            title = "Calling Suspend Functions",
            code = """
LaunchedEffect(userId) {
    val user = repository.getUser(userId)
    // Update state with result
}
            """.trimIndent()
        )

        // Use Case 4: Timer
        UseCaseItem(
            emoji = "⏱️",
            title = "Delays / Timers",
            code = """
LaunchedEffect(Unit) {
    while (true) {
        delay(1000)
        tickCount++
    }
}  // Auto-cancelled on leave
            """.trimIndent()
        )

        // Use Case 5: ViewModel Events
        UseCaseItem(
            emoji = "📡",
            title = "Reacting to ViewModel Events",
            code = """
LaunchedEffect(Unit) {
    viewModel.events.collect { event ->
        when (event) {
            is Event.ShowError -> showSnackbar()
            is Event.Navigate -> navigate()
        }
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
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION 6: WHEN NOT TO USE
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun WhenNotToUseSection() {
    SectionCard(
        title = "⚠️ When NOT to Use LaunchedEffect",
        backgroundColor = Color(0xFFFFFDE7),
        accentColor = Color(0xFFF9A825)
    ) {
        Text(
            text = "Use rememberCoroutineScope instead when:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFF9A825)
        )

        Spacer(modifier = Modifier.height(12.dp))

        BulletPoint("You need to launch a coroutine from a callback (onClick, etc.)", Color(0xFFF9A825))
        BulletPoint("The side effect is triggered by user action, not state change", Color(0xFFF9A825))

        Spacer(modifier = Modifier.height(12.dp))

        CodeBlock(
            title = "❌ Wrong: LaunchedEffect for button click",
            code = """
// DON'T DO THIS
var clicked by remember { mutableStateOf(false) }
LaunchedEffect(clicked) {
    if (clicked) { doSomething() }
}
Button(onClick = { clicked = true }) { }
            """.trimIndent(),
            backgroundColor = Color(0xFFFFCDD2)
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeBlock(
            title = "✅ Correct: rememberCoroutineScope for button click",
            code = """
val scope = rememberCoroutineScope()
Button(
    onClick = {
        scope.launch { doSomething() }
    }
) { Text("Click") }
            """.trimIndent(),
            backgroundColor = Color(0xFFC8E6C9)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Live comparison demo
        ComparisonDemo()
    }
}

@Composable
private fun ComparisonDemo() {
    val scope = rememberCoroutineScope()
    var scopeClickCount by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✅ rememberCoroutineScope Demo",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF9A825)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Clicks: $scopeClickCount")

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        delay(1000) // Simulate work
                        scopeClickCount++
                        isLoading = false
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9A825))
            ) {
                Text(if (isLoading) "Loading..." else "Click Me!")
            }

            Text(
                text = "Coroutine launched from onClick callback",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic
            )
        }
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
                fontSize = 11.sp,
                lineHeight = 16.sp
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
private fun LaunchedEffectExampleScreenPreview() {
    LaunchedEffectExampleScreen()
}
