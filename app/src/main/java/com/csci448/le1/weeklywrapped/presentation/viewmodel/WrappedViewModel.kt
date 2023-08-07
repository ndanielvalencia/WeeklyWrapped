package com.csci448.le1.weeklywrapped.presentation.viewmodel

import android.graphics.Camera
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.data.*
import com.csci448.le1.weeklywrapped.util.screenshot.ImageResult
import com.csci448.le1.weeklywrapped.util.screenshot.rememberScreenshotState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.cancel
//import com.csci448.le1.weeklywrapped.data.WrappedRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class WrappedViewModel(
    private val wrappedRepo: WrappedRepo,
    private val photographRepository: PhotographRepository,
) : ViewModel(), IWrappedViewModel {
    companion object {
        private const val LOG_TAG = "448.WrappedViewModel"

    }
    // Onboarding info
    val onboardingItems = listOf(
        OnBoardingItem(imageId = R.drawable.onboarding_1, titleId = R.string.info_page1_title, descriptionId = R.string.info_page1_text, iconId = R.drawable.camera_icon),
        OnBoardingItem(imageId = R.drawable.onboarding_2, titleId = R.string.info_page2_title, descriptionId = R.string.info_page2_text, iconId = R.drawable.collage_icon)
    )

    // Current Screenshot ImageBitmap, for collage displayed in dialog when saving
    private val mCurrentCollageImageResult : MutableStateFlow<ImageResult?> = MutableStateFlow(null)
    override val currentCollageImageResult: StateFlow<ImageResult?>
        get() = mCurrentCollageImageResult.asStateFlow()

    override fun setCurrentCollageImageResult(imageBitmap: ImageResult?) {
        mCurrentCollageImageResult.value = imageBitmap
    }

    // Photographs
    private val mPhotographListStateFlow: MutableStateFlow<List<Photograph>> = MutableStateFlow(emptyList())
    val photographListStateFlow: StateFlow<List<Photograph>>
        get() = mPhotographListStateFlow.asStateFlow()

    private val mPhotographIdStateFlow: MutableStateFlow<UUID> = MutableStateFlow(UUID.randomUUID())
    private val mCurrentPhotographStateFlow: MutableStateFlow<Photograph?> = MutableStateFlow(null)
    val currentPhotographStateFlow: StateFlow<Photograph?>
        get() = mCurrentPhotographStateFlow.asStateFlow()

    // Camera Enabled states
    private val mCameraEnabled: MutableStateFlow<CameraEnabled> = MutableStateFlow(CameraEnabled(enabled = true))
    val cameraEnabled: StateFlow<CameraEnabled>
        get() = mCameraEnabled.asStateFlow()

    // Collages
    private val mCollageListStateFlow: MutableStateFlow<List<Collage>> = MutableStateFlow(emptyList())
    val collageListStateFlow: StateFlow<List<Collage>>
        get() = mCollageListStateFlow.asStateFlow()

    init {
        // Collect photographs
        Log.d(LOG_TAG, "PhotographViewModel initializing")
        viewModelScope.launch {
            photographRepository.getPhotographs().collect { photographList ->
                Log.d(LOG_TAG, "collected ${photographList.size} photographs")
                mPhotographListStateFlow.update { photographList }
            }
        }
        // Collect currently taken photograph
        viewModelScope.launch {
            mPhotographIdStateFlow
                .map { uuid -> photographRepository.getPhotograph(uuid) }
                .collect { photograph ->
                    Log.d(LOG_TAG, "collected photograph ${photograph?.id}")
                    mCurrentPhotographStateFlow.update { photograph }
                }
        }

        // Collect collages
        viewModelScope.launch {
            photographRepository.getCollages().collect { collageList ->
                Log.d(LOG_TAG, "collected ${collageList.size} collages")
                mCollageListStateFlow.update { collageList }
            }
        }
        // Collect camera is enabled state
        viewModelScope.launch {
            wrappedRepo.getCameraEnabled().collect {cameraEnabledList ->
                if (cameraEnabledList.isEmpty()) {
                    wrappedRepo.addCameraEnabled(mCameraEnabled.value)
                }
            }
        }
    }

    // Photograph functions
    override fun onCleared() {
        Log.d(LOG_TAG, "onCleared() called")
        viewModelScope.cancel()
        super.onCleared()
    }

    fun loadPhotographById(uuid: UUID) {
        mPhotographIdStateFlow.update { uuid }
    }


    override fun addPhotograph(photograph: Photograph) {
        photographRepository.addPhotograph(photograph)
    }


    override fun deletePhotograph(photograph: Photograph) {
        if(photograph.id == mCurrentPhotographStateFlow.value?.id) {
            mCurrentPhotographStateFlow.update { null }
        }
        photographRepository.deletePhotograph(photograph)
    }

    override suspend fun deletePhotographs(){
        photographRepository.deletePhotographs()
    }

    // Collage functions
    override fun addCollage(collage: Collage) {
        photographRepository.addCollage(collage)
    }


    override fun deleteCollage(collage: Collage) {
        photographRepository.deleteCollage(collage)
    }

    override suspend fun deleteCollages(){
        photographRepository.deleteCollages()
    }
    // Enable camera

    override fun enableCamera(enabled: Boolean) {
        val cameraEnabled = CameraEnabled(enabled = enabled)
        wrappedRepo.updateCameraEnabled(cameraEnabled)
        mCameraEnabled.value = cameraEnabled
    }


    // Dialog state
    private val mOpenDialogState = mutableStateOf(false) // Open Dialog variable\

    override val openDialogState: State<Boolean>
        get() = mOpenDialogState

    override fun setDialogState(boolean: Boolean) {
        mOpenDialogState.value = boolean
    }

    // Snackbar state
    private val mSnackbarHostState = mutableStateOf(SnackbarHostState())

    override val snackbarHostState: State<SnackbarHostState>
        get() = mSnackbarHostState
}