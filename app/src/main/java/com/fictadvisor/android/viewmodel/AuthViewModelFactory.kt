package com.fictadvisor.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fictadvisor.android.repository.AuthRepository

class AuthViewModelFactory() : ViewModelProvider.Factory {

    private val authRepository by lazy(LazyThreadSafetyMode.NONE) {
        AuthRepository()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
