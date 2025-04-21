package com.ahmadrd.dicodingevent.data.remote.retrofit

import com.ahmadrd.dicodingevent.data.remote.response.DetailEventResponse
import com.ahmadrd.dicodingevent.data.remote.response.DicodingEventResponse
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
    fun searchEvents(@Query("q") q: String): Call<DicodingEventResponse>

    @GET("events/{name}")
    suspend fun getFavEventByName(@Path("name") name: String): DetailEventResponse
}