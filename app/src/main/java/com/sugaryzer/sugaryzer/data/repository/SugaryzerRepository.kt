package com.sugaryzer.sugaryzer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.dataclass.ScannedData
import com.sugaryzer.sugaryzer.data.dataclass.SignInRequest
import com.sugaryzer.sugaryzer.data.dataclass.SignUpRequest
import com.sugaryzer.sugaryzer.data.pref.UserPreference
import com.sugaryzer.sugaryzer.data.response.DataItemHistory
import com.sugaryzer.sugaryzer.data.response.DataItemNews
import com.sugaryzer.sugaryzer.data.response.DataItemRecommendation
import com.sugaryzer.sugaryzer.data.response.LoginResponse
import com.sugaryzer.sugaryzer.data.response.RegisterResponse
import com.sugaryzer.sugaryzer.data.response.ResultAnalytics
import com.sugaryzer.sugaryzer.data.response.ResultConsume
import com.sugaryzer.sugaryzer.data.response.ResultProfile
import com.sugaryzer.sugaryzer.data.response.ScanResponse
import com.sugaryzer.sugaryzer.data.response.ScannedResponse
import com.sugaryzer.sugaryzer.data.retrofit.ApiService
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException

class SugaryzerRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreference
){
    fun getSession() = pref.getSession()
    fun getThemeSetting() = pref.getThemeSetting()

    suspend fun saveSession(token: String) = pref.saveSession(token)

    suspend fun removeSession() = pref.removeSession()

    suspend fun signUp(email: String,password: String,  name: String, weight: Int, height: Int, age: Int): RegisterResponse {
        val signUpRequest = SignUpRequest(email, password, name, weight,height,age)
        return apiService.register(signUpRequest)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val signInRequest = SignInRequest(email, password)
        return apiService.login(signInRequest)
    }

    suspend fun updateProfile(name: String, weight: Int, age: Int, height: Int): ResultProfile {
        val resultProfile = ResultProfile(name, weight, age, height)
        val response = apiService.updateProfile(resultProfile)

        if (response.error == false) {
            return response.result ?: throw IllegalStateException("Profile update successful but result is null")
        } else {
            throw IllegalStateException(response.message ?: "Unknown error from server")
        }
    }

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

    fun getRecommendation(productBarcode: String): LiveData<ResultState<List<DataItemRecommendation>>> = liveData {
        emit(ResultState.Loading)

        try {
            val response = apiService.getRecommendations(productBarcode)

            if (response.error == false) {
                emit(ResultState.Success(response.result?.data ?: emptyList()))
            } else {
                emit(ResultState.Error(response.message ?: "Unknown error from server"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getProfile(): LiveData<ResultState<List<ResultProfile>>> = liveData {
        emit(ResultState.Loading)

        try {
            val response = apiService.getProfile()

            if (response.error == false) {
                val resultList = response.result?.let { listOf(it) } ?: emptyList()
                emit(ResultState.Success(resultList))
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
            val response = apiService.getHistory(size = 100)

            if (response.error == false) {
                emit(ResultState.Success(response.result?.data ?: emptyList()))
            } else {
                emit(ResultState.Error(response.message ?: "Unknown error from server"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }


    suspend fun scanImage(photo: MultipartBody.Part): ResultState<ScanResponse> {
        return try {
            val response = apiService.scanImage(photo)

            if (response.error) {
                ResultState.Error(response.message)
            } else {
                ResultState.Success(response)
            }
        } catch (e: IOException) {
            ResultState.Error("Network error: ${e.message}")
        } catch (e: HttpException) {
            ResultState.Error("HTTP error: ${e.message}")
        } catch (e: Exception) {
            ResultState.Error("An unexpected error occurred: ${e.message}")
        }
    }

    fun getConsume(date: String): LiveData<ResultState<ResultConsume>> = liveData {
        emit(ResultState.Loading)

        try {
            val response = apiService.getConsume(date)

            if (!response.error) {
                emit(ResultState.Success(response.result))
            } else {
                emit(ResultState.Error(response.message ?: "Unknown error from server"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }


    fun getAnalytics(sugar: Int): LiveData<ResultState<ResultAnalytics>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getAnalytics(sugar)
            if (response.error == false) {
                emit(ResultState.Success(response.result))
            } else {
                emit(ResultState.Error(response.message ?: "Unknown error from server"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    suspend fun uploadScan(code: String, sugarConsume: Double): ScannedResponse {
        val scannedData = ScannedData(code, sugarConsume)
        return apiService.uploadScan(scannedData)
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