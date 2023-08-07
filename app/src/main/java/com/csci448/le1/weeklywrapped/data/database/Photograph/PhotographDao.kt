package com.csci448.le1.weeklywrapped.data.database.Photograph

import androidx.room.*
import com.csci448.le1.weeklywrapped.data.Photograph
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface PhotographDao {

    @Insert
    suspend fun addPhotograph(photograph: Photograph)

    @Query("SELECT * FROM photograph")
    fun getPhotographs(): Flow<List<Photograph>>

    @Query("SELECT * FROM photograph WHERE id=(:id)")
    suspend fun getPhotograph(id: UUID): Photograph?

    @Delete
    suspend fun deletePhotograph(photograph: Photograph)

    @Query("DELETE FROM photograph")
    suspend fun deletePhotographs()

}