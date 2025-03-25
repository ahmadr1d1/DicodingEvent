package com.ahmadrd.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmadrd.dicodingevent.data.response.DicodingEventResponse
import com.ahmadrd.dicodingevent.data.response.ListEventsItem
import com.ahmadrd.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _listEventUpcoming = MutableLiveData<List<ListEventsItem>>()
    val listEventUpcoming: LiveData<List<ListEventsItem>> = _listEventUpcoming

    private val _listEventFinished = MutableLiveData<List<ListEventsItem>>()
    val listEventFinished: LiveData<List<ListEventsItem>> = _listEventFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        getEventUpcoming()
        getEventFinished()
    }

    private fun getEventUpcoming() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventUpcomingLimit()
        client.enqueue(object : Callback<DicodingEventResponse> {
            override fun onResponse(
                call: Call<DicodingEventResponse>,
                response: Response<DicodingEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val dicodingEventResponse = response.body()
                    if (dicodingEventResponse != null) {
                        _listEventUpcoming.value = dicodingEventResponse.listEvents
                    }
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure 1 getUser: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DicodingEventResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure 2 getEvents: ${t.message}")
            }
        })
    }

    private fun getEventFinished() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventFinishedLimit()
        client.enqueue(object : Callback<DicodingEventResponse> {
            override fun onResponse(
                call: Call<DicodingEventResponse>,
                response: Response<DicodingEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val dicodingEventResponse = response.body()
                    if (dicodingEventResponse != null) {
                        _listEventFinished.value = dicodingEventResponse.listEvents
                    }
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure 1 getUser: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DicodingEventResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure 2 getEvents: ${t.message}")
            }
        })
    }
}