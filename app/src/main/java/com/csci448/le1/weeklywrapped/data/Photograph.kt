package com.csci448.le1.weeklywrapped.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "photograph")
data class Photograph(
    @ColumnInfo(name = "photofilename")
    val photographFileName: String,
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val photoURI: String
)