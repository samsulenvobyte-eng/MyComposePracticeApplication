package com.example.mypracticeapplication.ui.screens.practice.ui_architecture

import android.renderscript.RenderScript
import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for Level1Screen (Task Details)
 */
class Level1ViewModel : ViewModel() {

//    private val _uiState = MutableStateFlow(Level1UiState())
//    val uiState: StateFlow<Level1UiState> = _uiState.asStateFlow()

    private val _uiState = MutableStateFlow(Level1UiState())
    val uiState: StateFlow<Level1UiState> = _uiState.asStateFlow()



    // TODO: Implement your UI architecture logic here

    fun onStatusSelected(status: Status) {
        _uiState.update { it.copy(status = status) }
    }

    fun onPrioritySelected(priority: RenderScript.Priority) {
        // TODO
    }

    fun onCompletedChange(isCompleted: Boolean) {
        // TODO
    }

    fun onSaveClick() {
        // TODO
    }

    fun onDeleteClick() {
        // TODO
    }

    fun onBackClick() {
        // TODO
    }
}
