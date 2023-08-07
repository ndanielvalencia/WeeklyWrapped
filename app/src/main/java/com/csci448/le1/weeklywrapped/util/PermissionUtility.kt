package com.csci448.le1.weeklywrapped.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.csci448.le1.weeklywrapped.R

class PermissionUtility(context: Context) {
    companion object {
        private const val LOG_TAG = "448.PermissionUtility"
    }

    fun checkCameraPermission(
        activity: Activity,
        permissionLauncher: ActivityResultLauncher<Array<String>>,
        takePhotoLauncher: ActivityResultLauncher<Uri>,
        photoUri: Uri,

    ){
        // check if permissions are granted
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(LOG_TAG, "Camera Permission granted")
            takePhotoLauncher.launch(photoUri)

        } // permission is currently not granted, check if we should ask for permission or not
        else if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            )
        ) { // Section 2 user already said no, don't ask again
            Log.d(LOG_TAG, "Camera denied")
            Toast.makeText(
                activity,
                activity.getString(R.string.camera_permission_denied),
                Toast.LENGTH_SHORT
            ).show()

        }// Section 3, User hasn ’ t previously declined, ask them
        else {

            Log.d(LOG_TAG, "Asking for permission")
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                )
            )
        }

    }
}