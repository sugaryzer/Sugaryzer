package com.sugaryzer.sugaryzer.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.data.response.DataItemNews
import com.sugaryzer.sugaryzer.data.response.LoginResponse
import com.sugaryzer.sugaryzer.data.response.ResultProfile
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class ProfileViewModel(private val repository: SugaryzerRepository) : ViewModel() {
    val getProfile: LiveData<ResultState<List<ResultProfile>>> = repository.getProfile()
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
    private val _editProfileResult = MutableLiveData<ResultState<ResultProfile>>()
    val editProfileResult: LiveData<ResultState<ResultProfile>> = _editProfileResult

    fun editProfile(name: String, weight: Int, age: Int, height: Int) {
        viewModelScope.launch {
            try {
                _editProfileResult.value = ResultState.Loading

                val response = repository.updateProfile(name, weight, age, height)

                _editProfileResult.value = ResultState.Success(response)

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let { JSONObject(it).getString("message") }
                } catch (jsonException: Exception) {
                    errorBody ?: e.message ?: "Unknown error occurred"
                }
                _editProfileResult.value = errorMessage?.let { ResultState.Error(it) }
            } catch (e: Exception) {
                _editProfileResult.value = ResultState.Error(e.message ?: "An error occurred")
            }
        }
    }
}