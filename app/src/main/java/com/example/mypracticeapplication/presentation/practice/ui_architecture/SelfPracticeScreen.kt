package com.example.mypracticeapplication.presentation.practice.ui_architecture

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ToastMessageEvent(val message: String)

data class UiStateSelfPractice(
    val name: String = "", val email: String = "", val message: String = "",
    val isLoading: Boolean = false, val errorMessage: String? = "", val isLoggedIn: Boolean = false
)

sealed interface LoginEvent {
    data class NameChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object LoginClicked : LoginEvent
    data object ErrorDismissed : LoginEvent
}

sealed interface UiEffect{
    data class ShowSnackbar(val value: String): UiEffect
    data object NavigateToHome: UiEffect
}


class SelfPracticeViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateSelfPractice())
    val uiState: StateFlow<UiStateSelfPractice> = _uiState.asStateFlow()
    private val _uiEffect = Channel<UiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private val _toastMessageEvent = MutableSharedFlow<ToastMessageEvent>()
    val toastMessageEvent = _toastMessageEvent.asSharedFlow()




    fun sendNotification(message: String){

        viewModelScope.launch {
            _toastMessageEvent.emit(ToastMessageEvent(message))
        }

    }

    fun onEvent(event: LoginEvent) {

        when (event) {

            is LoginEvent.NameChanged -> _uiState.update { it.copy(name = event.value) }
            is LoginEvent.PasswordChanged -> _uiState.update { it.copy(email = event.value) }
            is LoginEvent.LoginClicked -> login()
            else -> {}
        }
    }


    fun login() {
        viewModelScope.launch {
            // Start loading
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            // Simulate network delay
            delay(2000)

            // Fake login logic
            if (uiState.value.name == "admin" && uiState.value.email == "1234") {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true
                )
                _uiEffect.send(UiEffect.ShowSnackbar("Login Successful"))

            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Invalid credentials"
                )
                _uiEffect.send(UiEffect.ShowSnackbar("Login failed"))


            }
        }
    }

}


@Composable
fun SelfPracticeScreen(
    modifier: Modifier = Modifier,
    viewModel: SelfPracticeViewModel = viewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.uiState.collectAsState()
    val isLoading = uiState.value.isLoading
    val context = LocalContext.current


    LaunchedEffect(Unit) {

        viewModel.toastMessageEvent.collect { message ->
            Toast.makeText(context, message.message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {

        viewModel.toastMessageEvent.collect { message ->
            snackbarHostState.showSnackbar(message.message)
        }
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        viewModel.uiEffect.collect { effect ->
            
            when( effect){

                is UiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.value)
                is UiEffect.NavigateToHome -> {}
            }

        }
    }

    LaunchedEffect(Unit) {

        viewModel.uiEffect.collect { effect ->

            when( effect){

                is UiEffect.ShowSnackbar -> Toast.makeText(context,effect.value, Toast.LENGTH_SHORT).show()
                is UiEffect.NavigateToHome -> {}
            }

        }
    }



    if (isLoading) {
        LoadingDialog()
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {



            TextField(
                value = uiState.value.name,
                onValueChange = {
                    //viewModel.setName(it)
                    viewModel.onEvent(LoginEvent.NameChanged(it))

                    Log.i("TextField", it)
                },
                label = { Text("Name") })

            TextField(
                value = uiState.value.email,
                onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                label = { Text("Password") })

            uiState.value.errorMessage?.let { Text(it) }

            Button(onClick = { viewModel.onEvent(LoginEvent.LoginClicked) }) {

                Text("Login")
            }

            Button(onClick = { viewModel.sendNotification("Nigga wake up!!")}) {

                Text("Toast")
            }

            Text(
                text = "Name: ${uiState.value.name} \nemail: ${uiState.value.email}",
                color = Color.Green
            )

        }

    }


}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = { /* Disable dismiss */ }) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelfPracticeScreenPreview() {
    SelfPracticeScreen()
}


