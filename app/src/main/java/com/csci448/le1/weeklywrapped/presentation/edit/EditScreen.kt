package com.csci448.le1.weeklywrapped.presentation.edit

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.csci448.le1.weeklywrapped.util.drag_object.DragTarget
import com.csci448.le1.weeklywrapped.util.drag_object.LongPressDraggable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.csci448.le1.weeklywrapped.MainActivity
import com.csci448.le1.weeklywrapped.util.screenshot.ImageResult
import com.csci448.le1.weeklywrapped.util.screenshot.ScreenshotBox
import com.csci448.le1.weeklywrapped.util.screenshot.rememberScreenshotState


@Composable
fun EditScreen(
    wvm: WrappedViewModel,
    onSaveButton: (ImageResult) -> Unit,
    context: Context
) {
    val mainActivity = context as MainActivity
    LongPressDraggable(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

            val screenshotState =
                rememberScreenshotState() // For taking screenshots of the composable
            val imageResult: ImageResult = screenshotState.imageState.value

            LaunchedEffect(key1 = imageResult) { // When screenshot is taken

                if (imageResult is ImageResult.Success || imageResult is ImageResult.Error) {
                    onSaveButton(imageResult)
                }
            }
            ScreenshotBox(screenshotState = screenshotState, modifier = Modifier.weight(0.6f)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(0.5f)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            CollageCell(wvm)
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            CollageCell(wvm)
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            CollageCell(wvm)
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            CollageCell(wvm)
                        }
                    }
                }
            }


            // Get daily photos stored in the database
            val dailyPhotos =
                wvm.photographListStateFlow.collectAsStateWithLifecycle(lifecycle = mainActivity.lifecycle).value
            LazyRow(
                modifier = Modifier
                    .weight(0.2f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(
                    items = dailyPhotos,
                    key = { index, dailyPhoto -> dailyPhoto.id }) { index, dailyPhoto ->
                    DragTarget(modifier = Modifier, dataToDrop = dailyPhoto) {
                        AsyncImage(
                            model = dailyPhoto.photoURI,
                            contentDescription = dailyPhoto.photographFileName,
                            modifier = Modifier
                                .size(100.dp, 100.dp)
                                .fillMaxWidth()
                                .clickable { }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.drag_and_drop_label),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .weight(0.1f)
                    .padding(all = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    screenshotState.capture()
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                shape = RoundedCornerShape(4.dp)
                ) {
                    Text(text = stringResource(R.string.save_button_text))
                }
            }


        }

    }
}
