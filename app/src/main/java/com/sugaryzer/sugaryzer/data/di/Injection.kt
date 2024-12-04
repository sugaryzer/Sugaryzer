package com.sugaryzer.sugaryzer.data.di

import android.content.Context
import com.sugaryzer.sugaryzer.data.pref.UserPreference
import com.sugaryzer.sugaryzer.data.pref.dataStore
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): SugaryzerRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return SugaryzerRepository.getInstance(apiService, pref)
    }
}
