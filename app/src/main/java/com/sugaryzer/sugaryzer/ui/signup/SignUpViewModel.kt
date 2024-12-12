package com.sugaryzer.sugaryzer.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.data.response.LoginResponse
import com.sugaryzer.sugaryzer.data.response.RegisterResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class SignUpViewModel(private val repository: SugaryzerRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<RegisterResponse>>()
    val responseResult = _responseResult

    fun signUp(email:String, name: String ,password: String, weight: Int, height: Int, age: Int ){
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.signUp(email, password, name, weight,height,age)
                _responseResult.value = ResultState.Success(response)
            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody).getString("message")
                } catch (jsonException: Exception) {
                    errorBody ?: e.message ?: "Unknown error occurred"
                }
                _responseResult.value = ResultState.Error(errorMessage)
            }
        }
    }
}