package com.sugaryzer.sugaryzer.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository

class AuthViewModel(private val repository: SugaryzerRepository): ViewModel() {
    fun getSession() = repository.getSession().asLiveData()
}