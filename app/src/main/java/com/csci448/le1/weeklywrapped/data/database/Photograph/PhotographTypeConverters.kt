package com.csci448.le1.weeklywrapped.data.database.Photograph

import androidx.room.TypeConverter
import java.util.*

class PhotographTypeConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID?) = uuid.toString()

    @TypeConverter
    fun toUUID(uuid: String?) = UUID.fromString(uuid)
}