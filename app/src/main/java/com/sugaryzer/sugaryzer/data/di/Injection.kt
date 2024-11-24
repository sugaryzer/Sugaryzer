package com.sugaryzer.sugaryzer.data.di

import android.content.Context
import com.sugaryzer.sugaryzer.data.pref.UserPreference
import com.sugaryzer.sugaryzer.data.pref.dataStore
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository

object Injection {
    fun provideRepository(context: Context): SugaryzerRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return SugaryzerRepository.getInstance(pref)
    }
}