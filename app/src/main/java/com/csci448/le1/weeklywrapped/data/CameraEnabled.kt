package com.csci448.le1.weeklywrapped.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "camera_enabled")
data class CameraEnabled(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var enabled: Boolean
)
