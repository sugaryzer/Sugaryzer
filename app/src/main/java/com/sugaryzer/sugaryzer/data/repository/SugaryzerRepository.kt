package com.sugaryzer.sugaryzer.data.repository

import com.sugaryzer.sugaryzer.data.SignInRequest
import com.sugaryzer.sugaryzer.data.pref.UserPreference
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

    companion object {
        @Volatile
        private var instance: SugaryzerRepository? = null

        fun getInstance(apiService: ApiService, pref: UserPreference): SugaryzerRepository =
            instance ?: synchronized(this) {
                instance ?: SugaryzerRepository(apiService, pref).also { instance = it }
            }
    }

}