package com.sugaryzer.sugaryzer.ui.scan

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.data.response.MessageResponse

class ScanViewModel(private val sugaryzerRepository: SugaryzerRepository): ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    private val _uploadResult = MutableLiveData<ResultState<MessageResponse>>()
    val uploadResult: LiveData<ResultState<MessageResponse>> = _uploadResult


    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun getImageUri(): Uri? {
        return _imageUri.value
    }
}