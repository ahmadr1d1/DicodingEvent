package com.ahmadrd.dicodingevent.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmadrd.dicodingevent.data.local.entity.FavoriteEvents

@Database(entities = [FavoriteEvents::class], version = 3, exportSchema = false)
abstract class FavoriteEventsDatabase: RoomDatabase() {

    abstract fun favoriteEventsDao(): FavoriteEventsDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteEventsDatabase? = null
        fun getInstance(context: Context): FavoriteEventsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteEventsDatabase::class.java,
                    "FavoriteEvents.db"
                ).fallbackToDestructiveMigration(true)
                    .build()
            }
    }
}