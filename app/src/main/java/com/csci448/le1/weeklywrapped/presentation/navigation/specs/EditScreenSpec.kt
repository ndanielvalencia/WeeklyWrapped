package com.csci448.le1.weeklywrapped.presentation.navigation.specs

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.le1.weeklywrapped.BuildConfig
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.data.Collage
import com.csci448.le1.weeklywrapped.data.Photograph
import com.csci448.le1.weeklywrapped.presentation.edit.EditScreen
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import com.csci448.le1.weeklywrapped.util.screenshot.ImageResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


object EditScreenSpec : IScreenSpec {

    override val route = "edit"
    override val arguments: List<NamedNavArgument> = emptyList()
    override fun buildRoute(vararg args: String?) = route
    override val title = R.string.top_bar_title


    @Composable
    override fun Content(
        wvm: WrappedViewModel,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
        context: Context,
        takePicture: () -> Unit,
        scheduleNotification: () -> Unit,
        coroutineScope: CoroutineScope,
        snackbarCoroutineScope: CoroutineScope,
    ) {

        val currentImageResultState = wvm.currentCollageImageResult.collectAsStateWithLifecycle()
        EditScreen(
            wvm = wvm,
            onSaveButton = { imageResult ->
                wvm.setCurrentCollageImageResult(imageResult)
                wvm.setDialogState(true)
            },
            context = context,
        )
        if (wvm.openDialogState.value && currentImageResultState.value != null) {
            val currentImageResult = currentImageResultState.value
            androidx.compose.material3.AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back button.
                    wvm.setDialogState(false)
                },
                title = {
                    Text(text = stringResource(id = R.string.edit_screen_dialog_title))
                },
                text = {
                    Column(
                        modifier = Modifier.padding(all = 5.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        when (currentImageResult) {
                            is ImageResult.Success -> {
                                Row(
                                    modifier = Modifier.padding(all = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .height(150.dp),
                                        bitmap = currentImageResult.data.asImageBitmap(),
                                        contentDescription = null
                                    )
                                }

                            }
                            is ImageResult.Error -> {
                                Row(
                                    modifier = Modifier.padding(all = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "${currentImageResult.exception.message}")
                                }

                            }
                            else -> {}
                        }

                        Row(
                            modifier = Modifier.padding(all = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(
                                    id = R.string.edit_screen_dialog_message,
                                )
                            )
                        }

                    }
                },

                confirmButton = {
                    when (currentImageResult) {
                        is ImageResult.Success -> {
                            TextButton(
                                onClick = {
                                    coroutineScope.launch {
                                        val collageName = "IMG_${Date()}.JPG"
                                        val collageFile =
                                            File(context.filesDir, collageName) // local file
                                        collageFile.delete() // In case there is an existing file there
                                        withContext(Dispatchers.IO) {
                                            collageFile.createNewFile()
                                            val fileOutputStream = collageFile.outputStream()
                                            val byteArrayOutputStream = ByteArrayOutputStream()
                                            val imageBitmap =
                                                currentImageResult.data.asImageBitmap()
                                            val bitmap = imageBitmap.asAndroidBitmap()
                                            bitmap.compress(
                                                Bitmap.CompressFormat.PNG,
                                                100,
                                                byteArrayOutputStream
                                            )
                                            val bytearray = byteArrayOutputStream.toByteArray()
                                            fileOutputStream.write(bytearray)
                                            fileOutputStream.flush()
                                            fileOutputStream.close()
                                            byteArrayOutputStream.close()
                                            //val URI = collageFile.toURI()
                                            val collageUri = FileProvider.getUriForFile(
                                                Objects.requireNonNull(context.getApplicationContext()),
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                collageFile
                                            )

                                            val collage =
                                                Collage(
                                                    collageFileName = collageName,
                                                    collageURI = collageUri.toString()
                                                )
                                            wvm.addCollage(collage)
                                            wvm.setDialogState(false)
                                        }
                                    }

                                    snackbarCoroutineScope.launch {
                                        wvm.snackbarHostState.value.showSnackbar(
                                            message = context.resources.getString(
                                                R.string.collage_saved_succesfully
                                            ), duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            ) {
                                Text(stringResource(id = R.string.edit_screen_dialog_positive_label))
                            }
                        }

                        else -> {
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            wvm.setDialogState(false)
                        }) {
                        Text(stringResource(id = R.string.edit_screen_dialog_negative_label))

                    }
                }
            )
        }
    }

    @Composable
    override fun TopAppBarActions(
        wvm: WrappedViewModel,
        navHostController: NavHostController,
        navBackStackEntry: NavBackStackEntry?,
        context: Context
    ) {
        IconButton(onClick = {
            navHostController.navigate("info")
        }) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = stringResource(id = R.string.info_button_desc)
            )
        }
    }
}