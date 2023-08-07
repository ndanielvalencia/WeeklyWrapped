package com.csci448.le1.weeklywrapped.data

import android.content.Context
import android.util.Log
import com.csci448.le1.weeklywrapped.data.database.Photograph.CollageDao
import com.csci448.le1.weeklywrapped.data.database.Photograph.PhotographDatabase
import com.csci448.le1.weeklywrapped.data.database.Photograph.PhotographDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

class PhotographRepository
@OptIn(DelicateCoroutinesApi::class)
private constructor(private val photographDao: PhotographDao,
                    private val collageDao: CollageDao,
                    private val coroutineScope: CoroutineScope = GlobalScope) {
    companion object {
        private const val LOG_TAG = "448.PhotographRepository"

        @Volatile private var INSTANCE: PhotographRepository? = null

        fun getInstance(context: Context): PhotographRepository {
            synchronized(this) {
                Log.d(LOG_TAG, "getInstance(Context) called")
                var instance = INSTANCE
                if (instance == null) {
                    Log.d(LOG_TAG, "creating PhotographRepository instance")
                    val database = PhotographDatabase.getInstance(context)
                    instance = PhotographRepository( database.photographDao, database.collageDao)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
    // Photograph functions
    fun getPhotographs() = photographDao.getPhotographs()

    suspend fun getPhotograph(id: UUID) = photographDao.getPhotograph(id)

    fun addPhotograph(photograph: Photograph) {
        coroutineScope.launch {
            photographDao.addPhotograph(photograph)
        }
    }

    fun deletePhotograph(photograph: Photograph) {
        coroutineScope.launch {
            photographDao.deletePhotograph(photograph)
        }
    }

    suspend fun deletePhotographs(){
        photographDao.deletePhotographs()
    }

    // Collage functions

    fun getCollages() = collageDao.getCollages()

    suspend fun getCollages(id: UUID) = collageDao.getCollage(id = id)

    fun addCollage(collage: Collage) {
        coroutineScope.launch {
            collageDao.addCollage(collage)
        }
    }

    fun deleteCollage(collage: Collage) {
        coroutineScope.launch {
            collageDao.deleteCollage(collage)
        }
    }

    suspend fun deleteCollages(){
        collageDao.deleteCollages()
    }
}