package com.csci448.le1.weeklywrapped.util.screenshot

import android.graphics.Bitmap
// Credit: https://github.com/SmartToolFactory/Compose-Screenshot
sealed class ImageResult {
    object Initial : ImageResult()
    data class Error(val exception: Exception) : ImageResult()
    data class Success(val data: Bitmap) : ImageResult()
}
