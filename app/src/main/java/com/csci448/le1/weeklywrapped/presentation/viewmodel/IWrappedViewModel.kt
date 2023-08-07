package com.csci448.le1.weeklywrapped.presentation.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.ImageBitmap
import com.csci448.le1.weeklywrapped.data.Collage
import com.csci448.le1.weeklywrapped.data.Photograph
import com.csci448.le1.weeklywrapped.util.screenshot.ImageResult
import kotlinx.coroutines.flow.StateFlow
import java.util.*

interface IWrappedViewModel {

    fun addPhotograph(photograph: Photograph)
    fun deletePhotograph(photograph: Photograph)
    fun enableCamera(enabled: Boolean)
    suspend fun deletePhotographs()
    val openDialogState: State<Boolean>
    fun setDialogState(boolean: Boolean)
    val currentCollageImageResult: StateFlow<ImageResult?>
    fun setCurrentCollageImageResult(imageBitmap: ImageResult?)
    fun addCollage(collage: Collage)
    fun deleteCollage(collage: Collage)
    suspend fun deleteCollages()
    val snackbarHostState: State<SnackbarHostState>
}