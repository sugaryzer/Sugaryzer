package com.sugaryzer.sugaryzer.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.data.response.DataItemNews

class NewsViewModel(private val sugaryzerRepository: SugaryzerRepository) : ViewModel() {
    val getNews: LiveData<ResultState<List<DataItemNews>>> = sugaryzerRepository.getNews()
}