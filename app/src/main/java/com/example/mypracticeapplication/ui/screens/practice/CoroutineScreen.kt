package com.example.mypracticeapplication.ui.screens.practice

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// ═══════════════════════════════════════════════════════════════════════════════
// 🎓 COROUTINE LEARNING PROTOTYPE
// ═══════════════════════════════════════════════════════════════════════════════
// This screen teaches the 20/80 rule of Kotlin Coroutines:
// - The 20% of concepts you'll use 80% of the time in real apps
// ═══════════════════════════════════════════════════════════════════════════════

private val PrimaryColor = Color(0xFF6366F1)
private val SecondaryColor = Color(0xFF8B5CF6)
private val SuccessColor = Color(0xFF10B981)
private val ErrorColor = Color(0xFFEF4444)
private val CodeBgColor = Color(0xFF1E1E2E)

@Composable
fun CoroutineScreen(
    onNavigateBack: () -> Unit = {},
    // 📚 LEARNING: viewModel() creates/retrieves ViewModel instance
    // The ViewModel survives configuration changes (rotation)
    viewModel: CoroutineViewModel = viewModel()
) {
    // 📚 LEARNING: collectAsState() converts StateFlow to Compose State
    // UI automatically recomposes when state changes
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Header(onNavigateBack = onNavigateBack)
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title Section
            TitleSection()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Interactive Demo Section
            DemoSection(
                viewModel = viewModel,
                uiState = uiState,
                onFetchClick = viewModel::fetchUserData,
                onFetchMultipleClick = viewModel::fetchMultipleData,
                onFetchParallelClick = viewModel::fetchParallelData,
                onClearClick = viewModel::clearData
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Learning Cards
            LearningSection()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 20/80 Takeaways
            TakeawaysSection()
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Header(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(PrimaryColor, SecondaryColor)
                )
            )
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
        Column {
            Text(
                text = "🚀 Coroutine Lab",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "The 20/80 Rule of Kotlin Coroutines",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun TitleSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎯 Focus on What Matters",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Learn the 20% of coroutines you'll use 80% of the time",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DemoSection(
    uiState: CoroutineUiState,
    onFetchClick: () -> Unit,
    onFetchMultipleClick: () -> Unit,
    onFetchParallelClick: () -> Unit,
    onClearClick: () -> Unit,
    viewModel: CoroutineViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🧪 Interactive Demo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // State Display
            StateDisplay(uiState = uiState)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onFetchClick,
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fetch User")
                }
                
                OutlinedButton(
                    onClick = onClearClick,
                    enabled = !uiState.isLoading && (uiState.hasData || uiState.hasError),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Clear")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Sequential vs Parallel comparison
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onFetchMultipleClick,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sequential", fontSize = 13.sp)
                }
                
                Button(
                    onClick = onFetchParallelClick,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF059669) // Teal for parallel
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("⚡ Parallel", fontSize = 13.sp)
                }
                Button(
                    onClick = viewModel::getUserProfileUpdate,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF059669) // Teal for parallel
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("my co\n ${uiState.profileMessage}", fontSize = 13.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Sequential: ~3s | Parallel: ~2s",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Request Counter
            Text(
                text = "Total requests: ${uiState.requestCount}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun StateDisplay(uiState: CoroutineUiState) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            uiState.isLoading -> Color(0xFFFEF3C7) // Yellow
            uiState.hasError -> Color(0xFFFEE2E2) // Red
            uiState.hasData -> Color(0xFFD1FAE5) // Green
            else -> Color(0xFFF3F4F6) // Gray
        },
        animationSpec = tween(300),
        label = "bgColor"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (uiState.isLoading) 1.02f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = when {
                    uiState.isLoading -> Color(0xFFFBBF24)
                    uiState.hasError -> ErrorColor
                    uiState.hasData -> SuccessColor
                    else -> Color(0xFFD1D5DB)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                (fadeIn(tween(200)) + scaleIn(initialScale = 0.9f)) togetherWith
                    (fadeOut(tween(200)) + scaleOut(targetScale = 0.9f))
            },
            label = "stateContent"
        ) { state ->
            when {
                state.isLoading -> LoadingContent()
                state.hasError -> ErrorContent(message = state.errorMessage!!)
                state.hasData -> SuccessContent(userName = state.userName!!)
                else -> IdleContent()
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = Color(0xFFF59E0B),
            strokeWidth = 3.dp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "⏳ Loading...",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF92400E)
            )
            Text(
                text = "Coroutine running on IO thread",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFB45309)
            )
        }
    }
}

@Composable
private fun ErrorContent(message: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(ErrorColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "❌ Error",
                fontWeight = FontWeight.SemiBold,
                color = ErrorColor
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFDC2626)
            )
        }
    }
}

@Composable
private fun SuccessContent(userName: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(SuccessColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "✅ Success!",
                fontWeight = FontWeight.SemiBold,
                color = SuccessColor
            )
            Text(
                text = "Welcome, $userName",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF059669)
            )
        }
    }
}

@Composable
private fun IdleContent() {
    Text(
        text = "👆 Tap a button to trigger a coroutine",
        color = Color(0xFF6B7280),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun LearningSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "📚 Core Concepts",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        ConceptCard(
            emoji = "1️⃣",
            title = "suspend functions",
            description = "Mark functions that can pause without blocking",
            codeSnippet = "suspend fun fetchData(): String"
        )
        
        ConceptCard(
            emoji = "2️⃣",
            title = "viewModelScope",
            description = "Auto-cancels when ViewModel clears (no memory leaks!)",
            codeSnippet = "viewModelScope.launch { ... }"
        )
        
        ConceptCard(
            emoji = "3️⃣",
            title = "Dispatchers.IO",
            description = "Switch to background thread for network/database",
            codeSnippet = "withContext(Dispatchers.IO) { api.call() }"
        )
        
        ConceptCard(
            emoji = "4️⃣",
            title = "Dispatchers.Main",
            description = "Default for viewModelScope - safe for UI updates",
            codeSnippet = "_uiState.update { it.copy(loading = true) }"
        )
        
        ConceptCard(
            emoji = "5️⃣",
            title = "Structured Concurrency",
            description = "Child coroutines are tied to parent's lifecycle",
            codeSnippet = "// Navigate away → ViewModel cleared → coroutines cancelled"
        )
        
        ConceptCard(
            emoji = "6️⃣",
            title = "async/await",
            description = "Run coroutines in PARALLEL and combine results",
            codeSnippet = """val a = async { fetchUser() }
val b = async { fetchPosts() }
val result = a.await() + b.await()"""
        )
    }
}

@Composable
private fun ConceptCard(
    emoji: String,
    title: String,
    description: String,
    codeSnippet: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emoji,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = PrimaryColor
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CodeBgColor)
                    .padding(12.dp)
            ) {
                Text(
                    text = codeSnippet,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color(0xFFA5D6FF)
                )
            }
        }
    }
}

@Composable
private fun TakeawaysSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFECFDF5)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🎯 20/80 Takeaways",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SuccessColor
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val takeaways = listOf(
                "Use viewModelScope.launch { } for almost everything",
                "Use withContext(Dispatchers.IO) for network/database",
                "Use async/await for parallel operations",
                "Don't worry about cancellation - it's automatic!",
                "Keep UI state in a single StateFlow<UiState>",
                "Mark long-running functions as suspend"
            )
            
            takeaways.forEachIndexed { index, takeaway ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "✓",
                        color = SuccessColor,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = takeaway,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF065F46)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider(color = SuccessColor.copy(alpha = 0.3f))
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "💡 That's it! You now know enough to handle 80% of real-world coroutine use cases.",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF047857)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CoroutineScreenPreview() {
    CoroutineScreen()
}
