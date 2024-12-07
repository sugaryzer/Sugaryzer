package com.sugaryzer.sugaryzer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.dataclass.SignInRequest
import com.sugaryzer.sugaryzer.data.pref.UserPreference
import com.sugaryzer.sugaryzer.data.response.DataItemHistory
import com.sugaryzer.sugaryzer.data.response.DataItemNews
import com.sugaryzer.sugaryzer.data.response.LoginResponse
import com.sugaryzer.sugaryzer.data.retrofit.ApiService

class SugaryzerRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreference
){
    fun getSession() = pref.getSession()
    fun getThemeSetting() = pref.getThemeSetting()

    suspend fun saveSession(token: String) = pref.saveSession(token)

    suspend fun removeSession() = pref.removeSession()

    suspend fun login(email: String, password: String): LoginResponse {
        val signInRequest = SignInRequest(email, password)
        return apiService.login(signInRequest)
    }

    suspend fun register(name: String, email: String, image: String, height: Int, weight: Int, age: Int, password: String) = apiService.register(name, email, image, height, weight, age, password)

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = pref.saveThemeSetting(isDarkModeActive)

    fun getNews(): LiveData<ResultState<List<DataItemNews>>> = liveData {
        emit(ResultState.Loading)

        try {
            val response = apiService.getNews()

            if (response.error == false) {
                emit(ResultState.Success(response.result?.data ?: emptyList()))
            } else {
                emit(ResultState.Error(response.message ?: "Unknown error from server"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getScanHistory(): LiveData<ResultState<List<DataItemHistory>>> = liveData {
        emit(ResultState.Loading)

        try {
            val response = apiService.getHistory()

            if (response.error == false) {
                emit(ResultState.Success(response.result?.data ?: emptyList()))
            } else {
                emit(ResultState.Error(response.message ?: "Unknown error from server"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: SugaryzerRepository? = null

        fun getInstance(apiService: ApiService, pref: UserPreference): SugaryzerRepository =
            instance ?: synchronized(this) {
                instance ?: SugaryzerRepository(apiService, pref).also { instance = it }
            }
    }

}