package com.fictadvisor.android.validator

import java.util.regex.Pattern

class InputValidator {
    companion object {
        data class ValidationResult(val isValid: Boolean, val errorMessage: String = "")

        private fun isLengthValid(value: String, minLength: Int, maxLength: Int): Boolean {
            return value.length in minLength..maxLength
        }

        fun isUsernameValid(username: String): ValidationResult {
            if(username.isEmpty()){
                return ValidationResult(false, "Обов'язкове поле")
            }
            if(!isLengthValid(username, 2, 40)){
                return ValidationResult(false, "Довжина має бути від 2 до 40 символів")
            }
            if(!Pattern.matches("\\w+", username)){
                return ValidationResult(false, "Може містити тільки латинські літери, цифри та знак _")
            }
            return ValidationResult(true)
        }

        fun isNameValid(name: String): ValidationResult {
            if (name.isEmpty()) {
                return ValidationResult(false, "Обов'язкове поле")
            }
            if (!isLengthValid(name, 2, 40)) {
                return ValidationResult(false, "Довжина має бути від 2 до 40 символів")
            }
            if (!Pattern.matches("^[ҐЄІЇЬА-ЩЮЯґєіїьа-щюя\\-`ʼ' ]+\$", name)) {
                return ValidationResult(false, "Може містити тільки літери українського алфавіту, дефіс, пробіл та апостроф")
            }
            return ValidationResult(true)
        }

        fun isLastnameValid(lastname: String): ValidationResult {
            if(lastname.isEmpty()){
                return ValidationResult(false, "Обов'язкове поле")
            }
            if(!isLengthValid(lastname, 2, 40)){
                return ValidationResult(false, "Довжина має бути від 2 до 40 символів")
            }
            if(!Pattern.matches("^[ҐЄІЇЬА-ЩЮЯґєіїьа-щюя\\-`ʼ' ]+\$", lastname)){
                return ValidationResult(false, "Може містити тільки літери українського алфавіту, дефіс, пробіл та апостроф")
            }
            return ValidationResult(true)
        }

        fun isMiddleNameValid(middleName: String): ValidationResult {
            if(middleName.isEmpty()){
                return ValidationResult(false, "Обов'язкове поле")
            }
            if(!isLengthValid(middleName, 2, 40)){
                return ValidationResult(false, "Довжина має бути від 2 до 40 символів")
            }
            if(!Pattern.matches("^[ҐЄІЇЬА-ЩЮЯґєіїьа-щюя\\-`ʼ' ]+\$", middleName)){
                return ValidationResult(false, "Може містити тільки літери українського алфавіту, дефіс, пробіл та апостроф")
            }
            return ValidationResult(true)
        }

        fun isGroupValid(group: String): ValidationResult {
            if (group.isEmpty()) {
                return ValidationResult(false, "Обов'язкове поле")
            }
            return ValidationResult(true)
        }

        fun isPasswordValid(password: String): ValidationResult {
            if(password.isEmpty()){
                return ValidationResult(false, "Обов'язкове поле")
            }
            if(!isLengthValid(password, 8, 32)){
                return ValidationResult(false, "Довжина має бути від 8 до 32 символів")
            }
            if(!Pattern.matches("(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[\\w\\W]+", password)){
                return ValidationResult(false, "Має містити принаймні одну велику літеру, одну цифру та один спеціальний символ")
            }
            return ValidationResult(true)
        }

        fun isEmailValid(email: String): ValidationResult {
            if(email.isEmpty()){
                return ValidationResult(false, "Обов'язкове поле")
            }
            if(!isLengthValid(email, 5, 40)){
                return ValidationResult(false, "Довжина має бути від 5 до 40 символів")
            }
            if(!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)\$", email)){
                return ValidationResult(false, "Невірний формат електронної пошти")
            }
            return ValidationResult(true)
        }
    }
}
