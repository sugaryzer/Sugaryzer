package com.sugaryzer.sugaryzer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.data.response.DataItemHistory
import com.sugaryzer.sugaryzer.data.response.ResultAnalytics
import com.sugaryzer.sugaryzer.data.response.ResultConsume
import com.sugaryzer.sugaryzer.data.response.ResultProfile
import java.time.LocalDate

class HomeViewModel(private val sugaryzerRepository: SugaryzerRepository) : ViewModel() {
    private val currentDate: String
        get() = LocalDate.now().toString()
    val getConsume: LiveData<ResultState<ResultConsume>> = sugaryzerRepository.getConsume(currentDate)
    val getProfile: LiveData<ResultState<List<ResultProfile>>> = sugaryzerRepository.getProfile()

    fun getAnalytics(sugar: Int): LiveData<ResultState<ResultAnalytics>> {
        return sugaryzerRepository.getAnalytics(sugar)
    }
}