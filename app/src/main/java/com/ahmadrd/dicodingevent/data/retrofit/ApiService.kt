package com.ahmadrd.dicodingevent.data.retrofit

import com.ahmadrd.dicodingevent.data.response.DetailEventResponse
import com.ahmadrd.dicodingevent.data.response.DicodingEventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: Int): Call<DetailEventResponse>

    @GET("events?active=1")
    fun getEventUpcomingLimit(@Query("limit") limit: Int? = 5): Call<DicodingEventResponse>

    @GET("events?active=0")
    fun getEventFinishedLimit(@Query("limit") limit: Int? = 5): Call<DicodingEventResponse>

    @GET("events")
    fun getEventUpcoming(@Query("active") active: Int? = 1): Call<DicodingEventResponse>

    @GET("events")
    fun getEventFinished(@Query("active") active: Int? = 0): Call<DicodingEventResponse>

    @GET("events")
    fun searchEvents(
//        @Query("active") active: Int? = -1,
        @Query("q") q: String,
//        @Query("limit") limit: Int? = 40
    ): Call<DicodingEventResponse>
}