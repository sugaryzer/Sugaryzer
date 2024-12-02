package com.sugaryzer.sugaryzer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sugaryzer.sugaryzer.data.di.Injection
import com.sugaryzer.sugaryzer.data.repository.SugaryzerRepository
import com.sugaryzer.sugaryzer.ui.auth.AuthViewModel
import com.sugaryzer.sugaryzer.ui.auth.signin.SignInFragment
import com.sugaryzer.sugaryzer.ui.auth.signin.SignInViewModel
import com.sugaryzer.sugaryzer.ui.auth.signup.SignUpViewModel
import com.sugaryzer.sugaryzer.ui.profile.ProfileViewModel

class ViewModelFactory(
    private val repository: SugaryzerRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
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