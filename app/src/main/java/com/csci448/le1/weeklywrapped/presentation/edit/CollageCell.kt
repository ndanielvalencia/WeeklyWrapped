package com.csci448.le1.weeklywrapped.presentation.edit

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.csci448.le1.weeklywrapped.data.Photograph
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import com.csci448.le1.weeklywrapped.util.drag_object.DropTarget

@Composable
fun CollageCell(
    wvm: WrappedViewModel
) {
    val LOG_TAG = "DropTarget"

    DropTarget<Photograph>(
        modifier = Modifier
            .fillMaxSize()
    ) { isInBound, data ->


        val bgColor = if (isInBound) Color.Red else Color.Transparent // Self explanatory
        val photograph: MutableState<Photograph?> = remember { mutableStateOf(null) }

        // Scale and rotation
//        var scale by remember { mutableStateOf(1f) }
//        var offsetX by remember { mutableStateOf(0f) }
//        var offsetY by remember { mutableStateOf(0f) }
//        val minScale = 0.1f,
//        val maxScale = 5f,
//        val rotationState = remember { mutableStateOf(1f) }
        ZoomableBox(
            modifier = Modifier
                .fillMaxSize()
                .clip(RectangleShape)
                .border(1.dp, Color.Black)
                .background(color = bgColor)
        ) {
            data?.let {
                if (isInBound) photograph.value = data
            }

            if (photograph.value != null) {

                AsyncImage(
                    model = photograph.value!!.photoURI,
                    contentDescription = photograph.value!!.photographFileName,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            // Zoom limits (min 50%, max 200%)
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        ),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isInBound) ColorFilter.tint(
                        color = Color.Red.copy(alpha = 0.5f),
                        blendMode = BlendMode.Overlay
                    ) else null
                )
            }
        }
    }
}
