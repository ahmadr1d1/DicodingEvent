package com.ahmadrd.dicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmadrd.dicodingevent.data.Repository
import com.ahmadrd.dicodingevent.data.local.entity.FavoriteEvents
import com.ahmadrd.dicodingevent.data.remote.response.DetailEventResponse
import com.ahmadrd.dicodingevent.data.remote.retrofit.ApiConfig
import com.ahmadrd.dicodingevent.ui.utils.Result
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favEventRepo: Repository) : ViewModel() {

    private val _result = MutableLiveData<Result<DetailEventResponse>>()
    val result: LiveData<Result<DetailEventResponse>> = _result
    fun getDetailFavoriteUser(username: String) {
        viewModelScope.launch {
            _result.value = Result.Loading
            try {
                val response = ApiConfig.getApiService().getFavEventByName(username)
                _result.value = Result.Success(response)
            } catch (e: Exception) {
                _result.value = Result.Error(e.message.toString())
            }
        }
    }

    fun getFavEvents(): LiveData<List<FavoriteEvents>> = favEventRepo.getFavEvents()

    fun insertUser(favorite: FavoriteEvents) {
        viewModelScope.launch {
            favEventRepo.insertFavoriteEvent(favorite)
        }
    }

    fun isEventFavorited(eventId: Int): LiveData<List<FavoriteEvents>> =
        favEventRepo.isEventFavorited(eventId)

    fun deleteByEventId(eventId: Int) {
        viewModelScope.launch {
            favEventRepo.deleteByEventId(eventId)
        }
    }
}