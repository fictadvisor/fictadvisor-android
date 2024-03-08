package com.fictadvisor.android.validator

import android.content.Context
import android.widget.Toast

class RegistrationInputValidator(val context: Context): InputValidator() {
    fun isStudentDataValid(
        name: String,
        lastname: String,
        middleName: String,
        group: String
    ): Boolean {
        val nameValidationResult = isNameValid(name)
        val lastnameValidationResult = isLastnameValid(lastname)
        val middleNameValidationResult = isMiddleNameValid(middleName)
        val groupValidationResult = isGroupValid(group)


        if (!nameValidationResult.isValid) {
            Toast.makeText(context, nameValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!lastnameValidationResult.isValid) {
            Toast.makeText(context, lastnameValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!middleNameValidationResult.isValid) {
            Toast.makeText(context, middleNameValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!groupValidationResult.isValid) {
            Toast.makeText(context, groupValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun isUserDataValid(email: String, password: String, passwordConfirm: String, username: String): Boolean {
        val emailValidationResult = isEmailValid(email)
        val passwordValidationResult = isPasswordValid(password)
        val usernameValidationResult = isUsernameValid(username)

        if (!emailValidationResult.isValid) {
            Toast.makeText(context, emailValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!passwordValidationResult.isValid) {
            Toast.makeText(context, passwordValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if(password != passwordConfirm) {
            Toast.makeText(context, "Паролі не співпадають", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!usernameValidationResult.isValid) {
            Toast.makeText(context, usernameValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}