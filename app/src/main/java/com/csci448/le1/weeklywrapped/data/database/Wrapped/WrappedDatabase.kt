package com.csci448.le1.weeklywrapped.data.database.Wrapped

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.csci448.le1.weeklywrapped.data.CameraEnabled

@Database(entities=[CameraEnabled::class], version=1)
abstract class WrappedDatabase : RoomDatabase() {
    companion object {
        private const val LOG_TAG = "448.WrappedDatabase"
        private const val DATABASE_NAME = "wrapped-database"

        @Volatile private var INSTANCE: WrappedDatabase? = null

        fun getInstance(context: Context): WrappedDatabase {
            synchronized(this) {
                Log.d(LOG_TAG, "getInstance(Context) called")
                var instance = INSTANCE
                if (instance == null) {
                    Log.d(LOG_TAG, "creating WrappedDatabase instance")
                    instance = Room
                        .databaseBuilder(context,
                            WrappedDatabase::class.java,
                            DATABASE_NAME
                        )
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    abstract val wrappedDao: WrappedDao
}