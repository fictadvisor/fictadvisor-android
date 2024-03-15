package com.fictadvisor.android.validator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel(){
    private val inputValidator = InputValidator()

    val loginErrorLiveData = MutableLiveData<String?>()

    fun validateLoginData(login: String): Boolean {
        val usernameValidationResult = inputValidator.isUsernameValid(login)
        val emailValidationResult = inputValidator.isEmailValid(login)

        if (!usernameValidationResult.isValid && !emailValidationResult.isValid) {
            loginErrorLiveData.value = "Некоректний логін"
            return false
        } else {
            loginErrorLiveData.value = null
        }
        return true
    }

}