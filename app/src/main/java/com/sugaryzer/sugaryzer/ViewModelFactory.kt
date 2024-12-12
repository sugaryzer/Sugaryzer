package com.sugaryzer.sugaryzer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sugaryzer.sugaryzer.data.di.Injection
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.ui.history.HistoryViewModel
import com.sugaryzer.sugaryzer.ui.home.HomeViewModel
import com.sugaryzer.sugaryzer.ui.main.MainViewModel
import com.sugaryzer.sugaryzer.ui.news.NewsViewModel
import com.sugaryzer.sugaryzer.ui.signin.SignInViewModel
import com.sugaryzer.sugaryzer.ui.signup.SignUpViewModel
import com.sugaryzer.sugaryzer.ui.profile.ProfileViewModel
import com.sugaryzer.sugaryzer.ui.scan.ScanViewModel

class ViewModelFactory(
    private val repository: SugaryzerRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}