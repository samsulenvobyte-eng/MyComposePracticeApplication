package com.example.mypracticeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.mypracticeapplication.model.ImageMetaData
import com.example.mypracticeapplication.model.ImageMetadataUtils
import com.example.mypracticeapplication.navigation.AppNavHost
import com.example.mypracticeapplication.ui.theme.MyPracticeApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            MyPracticeApplicationTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}



@Composable
fun ImagePickerScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State to hold the extracted metadata
    var metaData by remember { mutableStateOf<ImageMetaData?>(null) }
    // State to hold the selected image URI (optional, for display)
    var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    // The Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                imageUri = uri
                // Launch a coroutine on IO thread to prevent UI freezing
                scope.launch(Dispatchers.IO) {
                    val result = ImageMetadataUtils.getImageMetaData(context, uri)
                    // Update state (Compose automatically handles switching back to Main thread for state updates)
                    metaData = result
                }
            }
        }
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = {
                // Trigger the picker
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(20.dp))

        metaData?.let { data ->
            Text(text = "MIME Type: ${data.mimeType}")
            Text(text = "Size: ${data.sizeReadable}")
            Text(text = "Resolution: ${data.width} x ${data.height}")
            Text(text = "Rotation: ${data.rotationDegrees}°")
        } ?: Text("No image selected yet")
    }
}
