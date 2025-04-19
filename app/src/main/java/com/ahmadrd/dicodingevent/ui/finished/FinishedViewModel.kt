package com.ahmadrd.dicodingevent.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmadrd.dicodingevent.data.remote.response.DicodingEventResponse
import com.ahmadrd.dicodingevent.data.remote.response.ListEventsItem
import com.ahmadrd.dicodingevent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _listEventSearch = MutableLiveData<List<ListEventsItem>>()
    val listEventSearch: LiveData<List<ListEventsItem>> = _listEventSearch

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private var lastQuery: String? = null // Menyimpan query terakhir
    var currentQuery: String = ""

    companion object {
        private const val TAG = "FinishedViewModel"
    }

    init {
        getEvent()
    }

     fun getEvent() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventFinished()
        client.enqueue(object : Callback<DicodingEventResponse> {
            override fun onResponse(
                call: Call<DicodingEventResponse>,
                response: Response<DicodingEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val dicodingEventResponse = response.body()
                    if (dicodingEventResponse != null) {
                        _listEvent.value = dicodingEventResponse.listEvents // Akses listEvent dari dicodingEventResponse
                    }
                } else {
                    _error.value = true
                    Log.e(TAG, "onFailure 1 getUser: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DicodingEventResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                Log.e(TAG, "onFailure 2 getEvents: ${t.message}")
            }
        })
        lastQuery = null // Reset query saat full data ditampilkan
    }

    fun getEventSearch(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchEvents(query)
        client.enqueue(object : Callback<DicodingEventResponse>{
            override fun onResponse(call: Call<DicodingEventResponse>, response: Response<DicodingEventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _listEventSearch.value = response.body()?.listEvents
                }else {
                    Log.e(TAG, "onFailure 1 getSearchUser: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DicodingEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure 2 getSearchUser: ${t.message}")
            }
        })
        lastQuery = query // Simpan query terakhir
    }

    // Refresh Page
    fun restoreLastState() {
        lastQuery?.let { getEventSearch(it) } ?: getEvent()
    }
}