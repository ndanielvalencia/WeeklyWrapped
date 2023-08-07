package com.csci448.le1.weeklywrapped.data.database.Wrapped

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.csci448.le1.weeklywrapped.data.CameraEnabled
import com.csci448.le1.weeklywrapped.data.Photograph
import kotlinx.coroutines.flow.Flow

@Dao
interface WrappedDao {
    @Insert
    suspend fun addCameraEnabled(cameraEnabled: CameraEnabled)

    @Query("SELECT * FROM camera_enabled")
    fun getCameraEnabled(): Flow<List<CameraEnabled>>

    @Update
    suspend fun updateCameraEnabled(cameraEnabled: CameraEnabled)
}