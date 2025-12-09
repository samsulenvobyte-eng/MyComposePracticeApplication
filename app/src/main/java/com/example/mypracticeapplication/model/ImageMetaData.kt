package com.example.mypracticeapplication.model

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.exifinterface.media.ExifInterface

data class ImageMetaData(
    val width: Int,
    val height: Int,
    val mimeType: String,
    val sizeInBytes: Long,
    val orientation: Int, // Exif Orientation tag
    val rotationDegrees: Int // Converted to degrees (0, 90, 180, 270)
) {
    // Helper to get a human-readable size
    val sizeReadable: String
        get() = "${sizeInBytes / 1024} KB"

    // Helper to check if image is portrait or landscape
    val aspectRatio: Float
        get() = if (height != 0) width.toFloat() / height else 0f

}

object ImageMetadataUtils {

    /**
     * Extracts metadata from a given Uri without loading the whole image into memory.
     */
    fun getImageMetaData(context: Context, uri: Uri): ImageMetaData? {
        return try {
            val contentResolver = context.contentResolver

            // 1. Get File Size (Cursor query is faster than opening a stream)
            var size: Long = 0
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (cursor.moveToFirst() && sizeIndex != -1) {
                    size = cursor.getLong(sizeIndex)
                }
            }

            // 2. Decode Bounds & MimeType (Efficiently)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true // CRITICAL: Does not allocate memory for pixels
            }

            contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, options)
            }

            // 3. Extract Exif Data (Rotation, etc.)
            var orientation = ExifInterface.ORIENTATION_UNDEFINED
            var rotation = 0

            contentResolver.openInputStream(uri)?.use { stream ->
                val exif = ExifInterface(stream)
                orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )
                rotation = exif.rotationDegrees
            }

            // Return the aggregated data
            ImageMetaData(
                width = options.outWidth,
                height = options.outHeight,
                mimeType = options.outMimeType ?: "unknown",
                sizeInBytes = size,
                orientation = orientation,
                rotationDegrees = rotation
            )

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}