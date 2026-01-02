package com.example.mypracticeapplication.ui.screens.compose_lab

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview


interface PermissionController {
    fun requestCamera()
    val granted: State<Boolean>
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeLabScreen(
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Compose Lab",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {


            val permission = rememberPermissionController()

            Button(onClick = permission::requestCamera) {
                Text("Request Camera")
            }
            Text("Camera permission granted: ${permission.granted.value}")
        }


    }
}

@Preview(showBackground = true)
@Composable
private fun ComposeLabScreenPreview() {
    ComposeLabScreen()
}


@Composable
fun rememberPermissionController(): PermissionController {

    val granted = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        granted.value = it
    }

    return remember {
        object : PermissionController {
            override val granted: State<Boolean> = granted
            override fun requestCamera() {
                launcher.launch(Manifest.permission.INTERNET)
            }
        }
    }
}