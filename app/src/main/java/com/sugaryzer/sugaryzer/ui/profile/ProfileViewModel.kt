package com.sugaryzer.sugaryzer.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: SugaryzerRepository) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean>{
        return repository.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean){
        viewModelScope.launch {
            repository.saveThemeSetting(isDarkModeActive)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.removeSession()
        }
    }
}