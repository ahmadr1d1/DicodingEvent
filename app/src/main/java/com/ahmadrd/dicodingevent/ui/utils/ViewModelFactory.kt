package com.ahmadrd.dicodingevent.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmadrd.dicodingevent.data.Injection
import com.ahmadrd.dicodingevent.data.Repository
import com.ahmadrd.dicodingevent.ui.favorite.FavoriteViewModel
import com.ahmadrd.dicodingevent.ui.settings.SettingsViewModel

class ViewModelFactory private constructor(
    private val repository: Repository? = null,
    private val pref: SettingPreferences? = null
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository!!) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(pref!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        // Khusus untuk ViewModel yang butuh Repository
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(repository = Injection.provideRepository(context))
            }.also { instance = it }

        // Khusus untuk ViewModel yang butuh SettingPreferences
        fun getInstance(pref: SettingPreferences): ViewModelFactory =
            ViewModelFactory(pref = pref)
    }
}
