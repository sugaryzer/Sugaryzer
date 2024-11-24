package com.sugaryzer.sugaryzer.data.repository

import com.sugaryzer.sugaryzer.data.pref.UserPreference

class SugaryzerRepository private constructor(
    private val pref: UserPreference
){
    fun getThemeSetting() = pref.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = pref.saveThemeSetting(isDarkModeActive)

    companion object {
        @Volatile
        private var instance: SugaryzerRepository? = null

        fun getInstance(pref: UserPreference): SugaryzerRepository =
            instance ?: synchronized(this) {
                instance ?: SugaryzerRepository(pref).also { instance = it }
            }
    }

}