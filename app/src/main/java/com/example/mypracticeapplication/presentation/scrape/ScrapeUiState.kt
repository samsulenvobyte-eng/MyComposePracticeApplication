package com.example.mypracticeapplication.presentation.scrape

/**
 * ScrapeUiState.kt - UI State for Web Scraping Demo
 *
 * This sealed interface represents all possible UI states for the scraping screen.
 * Using a sealed interface ensures type-safe state handling and exhaustive when() blocks.
 *
 * LEARNING POINTS:
 * 1. Sealed interfaces/classes restrict inheritance to the same file
 * 2. Each state carries only the data relevant to that state
 * 3. This pattern works great with StateFlow for reactive UI updates
 */
sealed interface ScrapeUiState {

    /**
     * Initial state when the screen first loads.
     * No data has been fetched yet, and the user hasn't initiated any action.
     */
    data object Idle : ScrapeUiState

    /**
     * Loading state while fetching and parsing HTML from the website.
     * UI should show a progress indicator during this state.
     */
    data object Loading : ScrapeUiState

    /**
     * Success state containing the scraped sentences.
     *
     * @param sentences List of extracted sentence examples from the website
     */
    data class Success(val sentences: List<String>) : ScrapeUiState

    /**
     * Error state with a user-friendly error message.
     *
     * @param message Human-readable error description for display in UI
     */
    data class Error(val message: String) : ScrapeUiState
}


