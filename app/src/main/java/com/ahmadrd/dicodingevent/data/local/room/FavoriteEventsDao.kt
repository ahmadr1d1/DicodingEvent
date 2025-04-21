package com.ahmadrd.dicodingevent.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmadrd.dicodingevent.data.local.entity.FavoriteEvents
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteEventsDao {

    @Query("SELECT * FROM favorite_events WHERE eventId = :eventId")
    fun getFavoriteByEventId(eventId: Int): Flow<List<FavoriteEvents>>

    @Query("SELECT * FROM favorite_events ORDER BY title ASC")
    fun getFavEvents(): Flow<List<FavoriteEvents>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteEvent(event: FavoriteEvents)

    @Query("DELETE FROM favorite_events WHERE eventId = :eventId")
    suspend fun deleteByEventId(eventId: Int)
}