package com.example.mypracticeapplication.presentation.scrape

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * ScrapeScreen.kt - Compose UI for Web Scraping Demo
 *
 * This screen demonstrates basic website scraping with a clean, educational UI.
 * Users can enter an English word and fetch example sentences from sentencedict.com.
 *
 * LEARNING POINTS:
 * 1. collectAsState() converts StateFlow to Compose State for recomposition
 * 2. remember { mutableStateOf() } creates local UI state
 * 3. LazyColumn efficiently displays large lists with recycling
 * 4. Different UI for each state using when() expression
 */

// Color palette for the scraping screen
private val PrimaryBlue = Color(0xFF2196F3)
private val DarkBlue = Color(0xFF1565C0)
private val LightBlue = Color(0xFF64B5F6)
private val BackgroundStart = Color(0xFF1a237e)
private val BackgroundEnd = Color(0xFF283593)
private val CardBackground = Color(0xFF303F9F)
private val ErrorRed = Color(0xFFEF5350)

/**
 * Main composable for the scraping screen.
 *
 * @param onNavigateBack Callback for back navigation
 * @param viewModel ViewModel instance (created by default using viewModel())
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapeScreen(
    onNavigateBack: () -> Unit,
    viewModel: ScrapeViewModel = viewModel()
) {
    // Collect UI state from ViewModel as Compose State
    // This triggers recomposition whenever state changes
    val uiState by viewModel.uiState.collectAsState()

    // Local state for the text input field
    // This is UI-only state, so it stays in the Composable
    var searchWord by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Web Scraping Demo",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(BackgroundStart, BackgroundEnd)
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header section with icon and description
                HeaderSection()

                Spacer(modifier = Modifier.height(24.dp))

                // Search input field
                SearchInputField(
                    value = searchWord,
                    onValueChange = { searchWord = it },
                    enabled = uiState !is ScrapeUiState.Loading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fetch Data button
                FetchButton(
                    onClick = { viewModel.scrapeWord(searchWord) },
                    enabled = searchWord.isNotBlank() && uiState !is ScrapeUiState.Loading
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Content area that changes based on UI state
                ContentSection(
                    uiState = uiState,
                    onRetry = { viewModel.scrapeWord(searchWord) }
                )
            }
        }
    }
}

/**
 * Header section with icon and instructional text.
 */
@Composable
private fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Decorative icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = PrimaryBlue.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CloudDownload,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = LightBlue
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sentence Dictionary Scraper",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter an English word to fetch example sentences\nfrom sentencedict.com",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

/**
 * Styled text input field for entering the search word.
 *
 * @param value Current text value
 * @param onValueChange Callback when text changes
 * @param enabled Whether the field is enabled
 */
@Composable
private fun SearchInputField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        placeholder = {
            Text(
                text = "Enter a word (e.g., 'hello', 'beautiful')",
                color = Color.White.copy(alpha = 0.5f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = LightBlue
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = LightBlue,
            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
            cursorColor = LightBlue
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

/**
 * Styled button to trigger the scraping operation.
 *
 * @param onClick Callback when button is clicked
 * @param enabled Whether the button is enabled
 */
@Composable
private fun FetchButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryBlue,
            disabledContainerColor = PrimaryBlue.copy(alpha = 0.5f)
        )
    ) {
        Icon(
            imageVector = Icons.Default.CloudDownload,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Fetch Data",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Content section that displays different UI based on the current state.
 *
 * LEARNING POINT: Using when() with sealed interface ensures all states are handled.
 *
 * @param uiState Current UI state from ViewModel
 * @param onRetry Callback to retry the operation
 */
@Composable
private fun ContentSection(
    uiState: ScrapeUiState,
    onRetry: () -> Unit
) {
    when (uiState) {
        // Idle state - show instructions
        is ScrapeUiState.Idle -> {
            IdleContent()
        }

        // Loading state - show progress indicator
        is ScrapeUiState.Loading -> {
            LoadingContent()
        }

        // Success state - show list of sentences
        is ScrapeUiState.Success -> {
            SuccessContent(sentences = uiState.sentences)
        }

        // Error state - show error message with retry option
        is ScrapeUiState.Error -> {
            ErrorContent(
                message = uiState.message,
                onRetry = onRetry
            )
        }
    }
}

/**
 * Content shown in Idle state - waiting for user action.
 */
@Composable
private fun IdleContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Enter a word and tap 'Fetch Data' to see example sentences",
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
}

/**
 * Content shown in Loading state - progress indicator.
 */
@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = LightBlue,
            strokeWidth = 4.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Fetching sentences...",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

/**
 * Content shown in Success state - list of scraped sentences.
 *
 * LEARNING POINT: LazyColumn only renders visible items, making it efficient
 * for long lists. itemsIndexed provides both index and item.
 *
 * @param sentences List of sentences to display
 */
@Composable
private fun SuccessContent(sentences: List<String>) {
    Column {
        // Result count header
        Text(
            text = "Found ${sentences.size} sentences",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // LazyColumn for efficient list rendering
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(sentences) { index, sentence ->
                SentenceCard(
                    index = index + 1,
                    sentence = sentence
                )
            }
        }
    }
}

/**
 * Card component for displaying a single sentence.
 *
 * @param index The sentence number (1-indexed)
 * @param sentence The sentence text
 */
@Composable
private fun SentenceCard(
    index: Int,
    sentence: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Sentence number badge
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = PrimaryBlue,
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$index",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Sentence text
            Text(
                text = sentence,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}

/**
 * Content shown in Error state - error message with retry button.
 *
 * @param message Error message to display
 * @param onRetry Callback to retry the operation
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Error icon
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = ErrorRed
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Error message
        Text(
            text = message,
            color = ErrorRed,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Retry button
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = ErrorRed
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Try Again")
        }
    }
}

/**
 * Preview for the ScrapeScreen in Idle state.
 */
@Preview(showBackground = true)
@Composable
private fun ScrapeScreenPreview() {
    ScrapeScreen(
        onNavigateBack = {}
    )
}


