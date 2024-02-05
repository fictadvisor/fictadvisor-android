package com.fictadvisor.android.validator

import android.content.Context
import android.widget.Toast

class LoginInputValidator(val context: Context): InputValidator() {
    fun isLoginDataValid(login: String): Boolean {
        val usernameValidationResult = isUsernameValid(login)
        val emailValidationResult = isEmailValid(login)

        if (!usernameValidationResult.isValid || emailValidationResult.isValid) {
            Toast.makeText(context, usernameValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}