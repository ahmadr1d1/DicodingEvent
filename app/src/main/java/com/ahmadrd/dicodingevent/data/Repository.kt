package com.ahmadrd.dicodingevent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.ahmadrd.dicodingevent.data.local.entity.FavoriteEvents
import com.ahmadrd.dicodingevent.data.local.room.FavoriteEventsDao

class Repository private constructor(private val favoriteEventsDao: FavoriteEventsDao){

    fun getFavEvents(): LiveData<List<FavoriteEvents>> =
        favoriteEventsDao.getFavEvents().asLiveData()

    fun isEventFavorited(eventId: Int): LiveData<List<FavoriteEvents>> =
        favoriteEventsDao.getFavoriteByEventId(eventId).asLiveData()

    suspend fun insertFavoriteEvent(favorite: FavoriteEvents) {
        favoriteEventsDao.insertFavoriteEvent(favorite)
    }

    suspend fun deleteByEventId(eventId: Int) {
        favoriteEventsDao.deleteByEventId(eventId)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            favoriteDao: FavoriteEventsDao
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(favoriteDao)
        }.also { instance = it }
    }
}