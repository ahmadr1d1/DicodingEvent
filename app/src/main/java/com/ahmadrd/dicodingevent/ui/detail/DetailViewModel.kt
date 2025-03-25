package com.ahmadrd.dicodingevent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmadrd.dicodingevent.data.response.DetailEventResponse
import com.ahmadrd.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {

    private val _detailEvent = MutableLiveData<DetailEventResponse?>()
    val detailEvent: LiveData<DetailEventResponse?> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun getDetailEvent(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val detailEventResponse = response.body()
                    if (detailEventResponse != null) {
                        _detailEvent.value = detailEventResponse
                    } else {
                        Log.e(TAG, "Response body is null")
                        _error.value = true
                    }
                } else {
                    _error.value = true
                    Log.e(TAG, "onFailure 1 getUser: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                Log.e(TAG, "onFailure 2 getEvents: ${t.message}")
            }
        })
    }
}