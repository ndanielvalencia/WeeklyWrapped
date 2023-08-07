package com.csci448.le1.weeklywrapped.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "collage")
data class Collage(
    @ColumnInfo(name = "collagefilename")
    val collageFileName: String,
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val collageURI: String
)