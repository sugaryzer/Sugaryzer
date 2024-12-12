package com.sugaryzer.sugaryzer.ui.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.data.response.LoginResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class SignInViewModel(private val repository: SugaryzerRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<LoginResponse>>()
    val responseResult = _responseResult

    fun signin(email:String, password:String){
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.login(email,password)
                if(response.result.accessToken.isNotEmpty()){
                    repository.saveSession(response.result.accessToken)
                    _responseResult.value = ResultState.Success(response)
                }
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