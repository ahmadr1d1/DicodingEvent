package com.ahmadrd.dicodingevent.data

import android.content.Context
import com.ahmadrd.dicodingevent.data.local.room.FavoriteEventsDatabase

object Injection {
    fun provideRepository(context: Context): Repository {
        val database = FavoriteEventsDatabase.getInstance(context)
        val dao = database.favoriteEventsDao()
        return Repository.getInstance(dao)
    }
}