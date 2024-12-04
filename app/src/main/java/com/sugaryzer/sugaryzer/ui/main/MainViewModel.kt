package com.sugaryzer.sugaryzer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository

class MainViewModel(private val repository: SugaryzerRepository): ViewModel() {
    fun getSession() = repository.getSession().asLiveData()
}