package com.csci448.le1.weeklywrapped

import android.app.Activity
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.csci448.le1.weeklywrapped.presentation.navigation.WrappedNavHost
import com.csci448.le1.weeklywrapped.presentation.navigation.WrappedTopBar
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModelFactory
import com.csci448.le1.weeklywrapped.ui.theme.WeeklyWrappedTheme
import com.csci448.le1.weeklywrapped.data.*
import com.csci448.le1.weeklywrapped.util.PhotoAlarmReceiver
import com.csci448.le1.weeklywrapped.util.PermissionUtility
import java.io.File
import java.util.*

class MainActivity : ComponentActivity() {
    companion object {
        private const val LOG_TAG = "448.MainActivity"
        private const val ROUTE_LOCATION = "main"
        private const val SCHEME = "https"
        private const val HOST = "weekly-wrapped.edu"
        private const val BASE_URI = "$SCHEME://$HOST"
        private const val EXTRA_CAMERA_ENABLED = "camera_enabled"

        private fun formatUriString(): String {
            val uriStringBuilder = StringBuilder()
            uriStringBuilder.append(BASE_URI)
            uriStringBuilder.append("/$ROUTE_LOCATION/")
            return uriStringBuilder.toString()
        }

        // Pending intent for notification
        fun createPendingIntent(context: Context, cameraEnabled: Boolean):
                PendingIntent {
            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                formatUriString().toUri(),
                context,
                MainActivity::class.java
            ).apply{
                putExtra(EXTRA_CAMERA_ENABLED, cameraEnabled)
            }
            return TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(deepLinkIntent)
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }
        }
    }

    private lateinit var mWrappedViewModel: WrappedViewModel
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>
    private val photoAlarmReceiver = PhotoAlarmReceiver()

    private lateinit var photoName: String
    private lateinit var photoUri: Uri
    private val takePhotoCallback = ActivityResultCallback<Boolean> { didTakePhoto ->
        if (didTakePhoto) {
            val photograph =
                Photograph(photographFileName = photoName, photoURI = photoUri.toString())
            mWrappedViewModel.addPhotograph(photograph)
            mWrappedViewModel.enableCamera(false)
            Log.d(LOG_TAG, "cameraEnabled after takePhotoCallback: ${mWrappedViewModel.cameraEnabled.value}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate() called")

        val factory = WrappedViewModelFactory(this)
        mWrappedViewModel = ViewModelProvider(this, factory)[factory.getViewModelClass()]


        // Permissions
        val permissionUtility = PermissionUtility(context = this@MainActivity)
        // This callback will check for camera permissions
        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                // process if permissions were granted
                permissionUtility.checkCameraPermission(
                    this@MainActivity,
                    cameraPermissionLauncher,
                    takePhotoLauncher,
                    photoUri,
                )
            }

        // This callback will check for notification permissions
        notificationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                // process if permissions were granted
                photoAlarmReceiver.checkPermissionAndScheduleAlarm(
                    this@MainActivity,
                    notificationPermissionLauncher,
                )
            }

        // Photo launcher function
        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture(), takePhotoCallback)

        setContent {
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            val snackbarCoroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val cameraEnabled = intent.getBooleanExtra(EXTRA_CAMERA_ENABLED, false)
            mWrappedViewModel.enableCamera(cameraEnabled)

            WeeklyWrappedTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            WrappedTopBar(
                                wvm = mWrappedViewModel,
                                navHostController = navController,
                                context = context
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(mWrappedViewModel.snackbarHostState.value)
                        }
                    ) { contentPadding ->
                        WrappedNavHost(
                            navController = navController,
                            wvm = mWrappedViewModel,
                            takePicture = {
                                // Update current photo info
                                photoName = "IMG_${Date()}.JPG"
                                val photoFile = File(context.filesDir, photoName)
                                photoUri = FileProvider.getUriForFile(
                                    Objects.requireNonNull(context.getApplicationContext()),
                                    BuildConfig.APPLICATION_ID + ".provider", photoFile
                                )
                                // Ask for camera permission, launch the photo launcher if permissions granted
                                permissionUtility.checkCameraPermission(
                                    context as Activity,
                                    permissionLauncher = cameraPermissionLauncher,
                                    takePhotoLauncher = takePhotoLauncher,
                                    photoUri = photoUri,
                                )
                            },
                            scheduleNotification = {
                                photoAlarmReceiver.checkPermissionAndScheduleAlarm(
                                    this@MainActivity,
                                    notificationPermissionLauncher,
                                )
                            },
                            context = context,
                            coroutineScope = coroutineScope,
                            snackbarCoroutineScope = snackbarCoroutineScope,
                            modifier = Modifier.padding(contentPadding)
                        )
                    }
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume() called")
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
    }

    override fun onPostResume() {
        super.onPostResume()
        Log.d(LOG_TAG, "onPostResume() called")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(LOG_TAG, "onAttachedToWindow() called")
    }

    override fun onContentChanged() {
        super.onContentChanged()
        Log.d(LOG_TAG, "onContentChanged() called")
    }

    override fun onDetachedFromWindow() {
        Log.d(LOG_TAG, "onDetachedFromWindow() called")
        super.onDetachedFromWindow()
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        Log.d(LOG_TAG, "onEnterAnimationComplete() called")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.d(LOG_TAG, "onPostCreate() called")
    }

    override fun isFinishing(): Boolean {
        Log.d(LOG_TAG, "isFinishing() called")
        return super.isFinishing()
    }

    override fun finish() {
        Log.d(LOG_TAG, "finish() called")
        super.finish()
    }
}


