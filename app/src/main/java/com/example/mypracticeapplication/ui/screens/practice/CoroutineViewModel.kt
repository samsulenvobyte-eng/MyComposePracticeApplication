package com.example.mypracticeapplication.ui.screens.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ═══════════════════════════════════════════════════════════════════════════════
// 📚 COROUTINE 20/80 RULE: WHAT YOU'LL ACTUALLY USE IN REAL APPS
// ═══════════════════════════════════════════════════════════════════════════════
//
// 1. suspend - Mark functions that can pause and resume (like API calls)
// 2. viewModelScope - Launch coroutines that auto-cancel when ViewModel clears
// 3. Dispatchers.Main - Run on UI thread (updating state, UI operations)
// 4. Dispatchers.IO - Run on background thread (network, database, file I/O)
// 5. Structured Concurrency - Child coroutines are tied to parent's lifecycle
//
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * UI State holder - Represents ALL possible states of the screen
 * This is simpler and safer than having multiple mutable variables
 */
data class CoroutineUiState(
    val isLoading: Boolean = false,
    val userName: String? = null,
    val errorMessage: String? = null,
    val requestCount: Int = 0
) {
    // Derived state - computed from existing state
    val hasData: Boolean get() = userName != null
    val hasError: Boolean get() = errorMessage != null
}

/**
 * ViewModel demonstrating the 20/80 of coroutines
 * 
 * KEY CONCEPT: viewModelScope
 * - Provided by AndroidX lifecycle-viewmodel-ktx
 * - Automatically cancels all coroutines when ViewModel is cleared
 * - This is STRUCTURED CONCURRENCY - child coroutines tied to parent lifecycle
 * - No memory leaks! No manual cleanup needed!
 */
class CoroutineViewModel : ViewModel() {
    
    // Private mutable state - only ViewModel can modify
    private val _uiState = MutableStateFlow(CoroutineUiState())
    
    // Public read-only state - UI observes this
    val uiState: StateFlow<CoroutineUiState> = _uiState.asStateFlow()
    
    /**
     * Fetch user data - simulates a typical API call pattern
     * 
     * KEY CONCEPT: viewModelScope.launch
     * - Starts a new coroutine in the ViewModel's scope
     * - Uses Dispatchers.Main by default (safe to update UI state)
     * - Auto-cancels if user navigates away (ViewModel cleared)
     */
    fun fetchUserData() {
        // 🚀 Launch a coroutine in viewModelScope
        // This is the pattern you'll use 90% of the time!
        viewModelScope.launch {
            // Update state to show loading
            // (We're on Main dispatcher, so this is safe for UI)
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // Call suspend function to fetch data
                val result = simulateApiCall()
                
                // Update state with success
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        userName = result,
                        requestCount = currentState.requestCount + 1
                    )
                }
            } catch (e: Exception) {
                // Update state with error
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = e.message ?: "Unknown error"
                    ) 
                }
            }
        }
    }
    
    /**
     * Simulates an API call that might fail
     * 
     * KEY CONCEPT: suspend function
     * - Can pause execution without blocking the thread
     * - Can only be called from another suspend function or a coroutine
     * - The 'suspend' keyword is a CONTRACT: "I might take time"
     */
    private suspend fun simulateApiCall(): String {
        // KEY CONCEPT: withContext(Dispatchers.IO)
        // - Switches to IO thread pool for blocking operations
        // - Network calls, database queries, file I/O go here
        // - Automatically switches back to original dispatcher when done
        return withContext(Dispatchers.IO) {
            // Simulate network delay (2 seconds)
            delay(2000)
            
            // Simulate random success/failure (80% success rate)
            if ((1..10).random() <= 8) {
                "John Doe" // Success!
            } else {
                throw Exception("Network timeout - please try again")
            }
        }
    }
    
    /**
     * Demonstrates multiple concurrent operations
     * 
     * KEY CONCEPT: Multiple coroutines can run in parallel
     * All are still tied to viewModelScope (structured concurrency)
     */
    fun fetchMultipleData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // These two operations run SEQUENTIALLY (one after another)
                val user = simulateApiCall()
                val extra = simulateExtraData()
                
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        userName = "$user - $extra",
                        requestCount = currentState.requestCount + 1
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, errorMessage = e.message) 
                }
            }
        }
    }
    
    private suspend fun simulateExtraData(): String {
        return withContext(Dispatchers.IO) {
            delay(1000)
            "Premium User"
        }
    }
    
    /**
     * Clear the current state
     */
    fun clearData() {
        _uiState.update { 
            CoroutineUiState(requestCount = it.requestCount) 
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // 🛡️ COROUTINE CANCELLATION (Happens automatically!)
    // ═══════════════════════════════════════════════════════════════════════════
    // 
    // When user navigates away:
    // 1. ViewModel.onCleared() is called
    // 2. viewModelScope is cancelled
    // 3. All child coroutines are cancelled (structured concurrency!)
    // 4. delay(), withContext(), etc. throw CancellationException
    // 5. Resources are cleaned up automatically
    //
    // YOU DON'T NEED TO DO ANYTHING! It just works.
    // ═══════════════════════════════════════════════════════════════════════════
}
