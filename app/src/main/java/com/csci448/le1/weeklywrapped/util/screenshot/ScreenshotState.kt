package com.csci448.le1.weeklywrapped.util.screenshot

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

// Credit: https://github.com/SmartToolFactory/Compose-Screenshot
/**
 * Create a State of screenshot of composable that is used with that is kept on each recomposition.
 * @param delayInMillis delay before each screenshot if [liveScreenshotFlow] is collected.
 */
@Composable
fun rememberScreenshotState(delayInMillis: Long = 20) = remember {
    ScreenshotState(delayInMillis)
}

/**
 * State of screenshot of composable that is used with.
 * @param timeInMillis delay before each screenshot if [liveScreenshotFlow] is collected.
 */
class ScreenshotState internal constructor(
    private val timeInMillis: Long = 20,
) {
    val imageState = mutableStateOf<ImageResult>(ImageResult.Initial)

    internal var callback: (() -> Unit)? = null

    /**
     * Captures current state of Composables inside [ScreenshotBox]
     */
    fun capture() {
        callback?.invoke()
    }

    val liveScreenshotFlow = flow {
        while (true) {
            callback?.invoke()
            delay(timeInMillis)
            bitmapState.value?.let {
                emit(it)
            }
        }
    }
        .map {
            it.asImageBitmap()
        }
        .flowOn(Dispatchers.Default)

    internal val bitmapState = mutableStateOf<Bitmap?>(null)

    val bitmap: Bitmap?
        get() = bitmapState.value

    val imageBitmap: ImageBitmap?
        get() = bitmap?.asImageBitmap()
}