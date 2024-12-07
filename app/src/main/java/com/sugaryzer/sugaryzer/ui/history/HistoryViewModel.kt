package com.sugaryzer.sugaryzer.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.data.response.DataItemHistory

class HistoryViewModel(private val sugaryzerRepository: SugaryzerRepository) : ViewModel() {
    val getScanHistory: LiveData<ResultState<List<DataItemHistory>>> = sugaryzerRepository.getScanHistory()
}