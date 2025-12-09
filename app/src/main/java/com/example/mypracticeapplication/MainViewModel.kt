package com.example.mypracticeapplication

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypracticeapplication.model.ImageMetadataUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    // Example usage inside a ViewModel or CoroutineScope
    fun checkImage(context: Context, imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val metaData = ImageMetadataUtils.getImageMetaData(context, imageUri)

            metaData?.let {
                println("Image Type: ${it.mimeType}") // e.g., "image/jpeg"
                println("Dimensions: ${it.width} x ${it.height}")
                println("Rotation: ${it.rotationDegrees}°")

                if (it.sizeInBytes > 5 * 1024 * 1024) {
                    println("Warning: Image is larger than 5MB")
                }
            }
        }
    }
}