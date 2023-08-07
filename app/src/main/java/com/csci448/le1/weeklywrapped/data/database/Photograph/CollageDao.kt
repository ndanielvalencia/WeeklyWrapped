package com.csci448.le1.weeklywrapped.data.database.Photograph

import androidx.room.*
import com.csci448.le1.weeklywrapped.data.Collage
import com.csci448.le1.weeklywrapped.data.Photograph
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface CollageDao {

    @Insert
    suspend fun addCollage(collage: Collage)

    @Query("SELECT * FROM collage")
    fun getCollages(): Flow<List<Collage>>

    @Query("SELECT * FROM collage WHERE id=(:id)")
    suspend fun getCollage(id: UUID): Collage?

    @Delete
    suspend fun deleteCollage(collage: Collage)

    @Query("DELETE FROM collage")
    suspend fun deleteCollages()

}