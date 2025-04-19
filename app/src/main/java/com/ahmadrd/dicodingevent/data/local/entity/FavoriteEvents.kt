package com.ahmadrd.dicodingevent.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_events")
@Parcelize
data class FavoriteEvents(

    @PrimaryKey
    var eventId: Int? = null,
    var mediaCover: String? = null,
    var title: String? = null,
    var description: String? = null

):Parcelable