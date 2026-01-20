package com.example.mypracticeapplication.ui.screens.scrape

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ScrapeViewModel.kt - ViewModel for Web Scraping Demo
 *
 * This ViewModel manages UI state and coordinates between the UI and Repository.
 * It follows the MVVM (Model-View-ViewModel) architecture pattern.
 *
 * LEARNING POINTS:
 * 1. ViewModel survives configuration changes (screen rotation)
 * 2. viewModelScope automatically cancels coroutines when ViewModel is cleared
 * 3. StateFlow provides reactive, observable state for Compose UI
 * 4. Private MutableStateFlow + public StateFlow = encapsulation
 */
class ScrapeViewModel : ViewModel() {

    /**
     * Repository instance for data operations.
     *
     * NOTE: In production apps, you would inject this using Hilt/Dagger.
     * For learning purposes, we create it directly here.
     */
    private val repository = ScrapeRepository()

    /**
     * Private mutable state that only the ViewModel can modify.
     * Initial state is Idle - waiting for user action.
     */
    private val _uiState = MutableStateFlow<ScrapeUiState>(ScrapeUiState.Idle)

    /**
     * Public read-only state that the UI observes.
     * Using asStateFlow() prevents external modification.
     *
     * LEARNING POINT: This pattern ensures unidirectional data flow:
     * UI events -> ViewModel -> Repository -> ViewModel updates state -> UI recomposes
     */
    val uiState: StateFlow<ScrapeUiState> = _uiState.asStateFlow()

    /**
     * Initiates web scraping for the given word.
     *
     * @param word The English word to search for sentence examples
     *
     * LEARNING POINTS:
     * 1. viewModelScope.launch starts a coroutine tied to ViewModel lifecycle
     * 2. State transitions: Idle/Error -> Loading -> Success/Error
     * 3. Result.fold() handles both success and failure cases cleanly
     */
    fun scrapeWord(word: String) {
        // Launch coroutine in ViewModel scope
        viewModelScope.launch {
            // Transition to Loading state - UI will show progress indicator
            _uiState.value = ScrapeUiState.Loading

            // Call repository to fetch and parse data
            // Note: The repository already runs on Dispatchers.IO
            val result = repository.fetchSentences(word)

            // Handle the result using fold pattern
            // fold() takes two lambdas: one for success, one for failure
            result.fold(
                onSuccess = { sentences ->
                    // Update state with scraped sentences
                    _uiState.value = ScrapeUiState.Success(sentences)
                },
                onFailure = { exception ->
                    // Update state with error message
                    _uiState.value = ScrapeUiState.Error(
                        exception.message ?: "An unknown error occurred"
                    )
                }
            )
        }
    }

    /**
     * Resets the UI state to Idle.
     * Call this when the user wants to start fresh or clear error state.
     */
    fun resetState() {
        _uiState.value = ScrapeUiState.Idle
    }
}
