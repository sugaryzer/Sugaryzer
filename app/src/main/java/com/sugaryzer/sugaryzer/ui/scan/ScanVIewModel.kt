package com.sugaryzer.sugaryzer.ui.scan

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.data.response.DataItemRecommendation
import com.sugaryzer.sugaryzer.data.response.LoginResponse
import com.sugaryzer.sugaryzer.data.response.ResultAnalytics
import com.sugaryzer.sugaryzer.data.response.ResultConsume
import com.sugaryzer.sugaryzer.data.response.ResultProfile
import com.sugaryzer.sugaryzer.data.response.ScanResponse
import com.sugaryzer.sugaryzer.data.response.ScannedResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.HttpException
import java.time.LocalDate

class ScanViewModel(private val sugaryzerRepository: SugaryzerRepository): ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    private val _uploadResult = MutableLiveData<ResultState<ScanResponse>>()
    val uploadResult: LiveData<ResultState<ScanResponse>> = _uploadResult
    private val currentDate: String
        get() = LocalDate.now().toString()


    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun getImageUri(): Uri? {
        return _imageUri.value
    }

    fun uploadStory(
        photo: MultipartBody.Part,
    ) {
        viewModelScope.launch {
            _uploadResult.value = ResultState.Loading
            try {
                val response = sugaryzerRepository.scanImage(photo)

                _uploadResult.value = response
            } catch (e: Exception) {
                _uploadResult.value = ResultState.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    private val _scannedResult = MutableLiveData<ResultState<ScannedResponse>>()
    val scannedResult = _scannedResult

    val getConsume: LiveData<ResultState<ResultConsume>> = sugaryzerRepository.getConsume(currentDate)
    fun getRecommendation(productBarcode: String): LiveData<ResultState<List<DataItemRecommendation>>> {
        return sugaryzerRepository.getRecommendation(productBarcode)
    }
    fun uploadScan(code:String, sugarConsume:Double){
        viewModelScope.launch {
            try {
                _scannedResult.value = ResultState.Loading
                val response = sugaryzerRepository.uploadScan(code,sugarConsume)
                _scannedResult.value = ResultState.Success(response)
            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody).getString("message")
                } catch (jsonException: Exception) {
                    errorBody ?: e.message ?: "Unknown error occurred"
                }
                _scannedResult.value = ResultState.Error(errorMessage)
            }
        }
    }
}