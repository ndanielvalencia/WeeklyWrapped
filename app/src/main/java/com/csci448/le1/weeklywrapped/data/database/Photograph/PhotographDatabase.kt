package com.csci448.le1.weeklywrapped.data.database.Photograph

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.csci448.le1.weeklywrapped.data.Collage
import com.csci448.le1.weeklywrapped.data.Photograph

@Database(entities=[Photograph::class, Collage::class], version=1)
@TypeConverters(PhotographTypeConverters::class)
abstract class PhotographDatabase : RoomDatabase() {
    companion object {
        private const val LOG_TAG = "448.PhotographDatabase"
        private const val DATABASE_NAME = "photograph-database"

        @Volatile private var INSTANCE: PhotographDatabase? = null

        fun getInstance(context: Context): PhotographDatabase {
            synchronized(this) {
                Log.d(LOG_TAG, "getInstance(Context) called")
                var instance = INSTANCE
                if (instance == null) {
                    Log.d(LOG_TAG, "creating PhotographDatabase instance")
                    instance = Room
                        .databaseBuilder(context,
                                         PhotographDatabase::class.java,
                                         DATABASE_NAME
                        )
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    abstract val photographDao: PhotographDao
    abstract val collageDao: CollageDao
}