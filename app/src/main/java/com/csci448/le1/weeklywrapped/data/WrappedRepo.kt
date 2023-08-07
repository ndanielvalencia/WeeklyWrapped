package com.csci448.le1.weeklywrapped.data

import android.content.Context
import android.util.Log
import com.csci448.le1.weeklywrapped.data.database.Wrapped.WrappedDao
import com.csci448.le1.weeklywrapped.data.database.Wrapped.WrappedDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WrappedRepo @OptIn(DelicateCoroutinesApi::class) private constructor(
    private val wrappedDao: WrappedDao, private val coroutineScope: CoroutineScope = GlobalScope
) {
    companion object {
        private const val LOG_TAG = "448.WrappedRepository"

        @Volatile private var INSTANCE: WrappedRepo? = null

        fun getInstance(context: Context): WrappedRepo {
            synchronized(this) {
                Log.d(LOG_TAG, "getInstance(Context) called")
                var instance = INSTANCE
                if (instance == null) {
                    Log.d(LOG_TAG, "creating WrappedRepo instance")
                    val database = WrappedDatabase.getInstance(context)
                    instance = WrappedRepo( database.wrappedDao )
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun getCameraEnabled() = wrappedDao.getCameraEnabled()

    fun addCameraEnabled(cameraEnabled: CameraEnabled) {
        coroutineScope.launch {
            wrappedDao.addCameraEnabled(cameraEnabled)
        }
    }

    fun updateCameraEnabled(cameraEnabled: CameraEnabled) {
        coroutineScope.launch {
            wrappedDao.updateCameraEnabled(cameraEnabled)
        }
    }
}