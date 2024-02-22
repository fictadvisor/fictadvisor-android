package com.fictadvisor.android.validator

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class InputValidatorTest: InputValidator() {

    @Test
    fun isUsernameValidCorrect() {
        val username = "valid_username"
        val result = isUsernameValid(username)
        assertTrue(result.isValid)
    }

    @Test
    fun isUsernameValidIncorrectEmpty() {
        val username = ""
        val result = isUsernameValid(username)
        assertFalse(result.isValid)
        assertEquals("Обов'язкове поле", result.errorMessage)
    }

    @Test
    fun isUsernameValidIncorrectLength() {
        val username = "a"
        val username2 = "a".repeat(41)

        val result = isUsernameValid(username)
        val result2 = isUsernameValid(username2)

        assertFalse(result.isValid)
        assertEquals("Довжина має бути від 2 до 40 символів", result.errorMessage)
        assertFalse(result2.isValid)
        assertEquals("Довжина має бути від 2 до 40 символів", result2.errorMessage)
    }

    @Test
    fun isUsernameValidIncorrectSymbols() {
        val username = "invalid@username"
        val result = isUsernameValid(username)
        assertFalse(result.isValid)
        assertEquals("Може містити тільки латинські літери, цифри та знак _", result.errorMessage)
    }

    @Test
    fun isNameValidCorrect() {
        val name = "Петро"
        val result = isNameValid(name)
        assertTrue(result.isValid)
    }

    @Test
    fun isNameValidIncorrectEmpty() {
        val name = ""
        val result = isNameValid(name)
        assertFalse(result.isValid)
        assertEquals("Обов'язкове поле", result.errorMessage)
    }

    @Test
    fun isNameValidIncorrectLength() {
        val name = "a"
        val name2 = "a".repeat(41)

        val result = isNameValid(name)
        val result2 = isNameValid(name2)

        assertFalse(result.isValid)
        assertEquals("Довжина має бути від 2 до 40 символів", result.errorMessage)
        assertFalse(result2.isValid)
        assertEquals("Довжина має бути від 2 до 40 символів", result2.errorMessage)
    }

    @Test
    fun isNameValidIncorrectSymbols() {
        val name = "invalid@name"
        val result = isNameValid(name)
        assertFalse(result.isValid)
        assertEquals("Може містити тільки літери українського алфавіту, дефіс, пробіл та апостроф", result.errorMessage)
    }

    @Test
    fun isLastnameValidCorrect() {
        val lastname = "Петренко"
        val result = isLastnameValid(lastname)
        assertTrue(result.isValid)
    }

    @Test
    fun isLastnameValidIncorrectEmpty() {
        val lastname = ""
        val result = isLastnameValid(lastname)
        assertFalse(result.isValid)
        assertEquals("Обов'язкове поле", result.errorMessage)
    }

    @Test
    fun isLastnameValidIncorrectLength() {
        val lastname = "a"
        val lastname2 = "a".repeat(41)

        val result = isLastnameValid(lastname)
        val result2 = isLastnameValid(lastname2)

        assertFalse(result.isValid)
        assertEquals("Довжина має бути від 2 до 40 символів", result.errorMessage)
        assertFalse(result2.isValid)
        assertEquals("Довжина має бути від 2 до 40 символів", result2.errorMessage)
    }

    @Test
    fun isLastnameValidIncorrectSymbols() {
        val lastname = "invalid@lastname"
        val result = isLastnameValid(lastname)
        assertFalse(result.isValid)
        assertEquals("Може містити тільки літери українського алфавіту, дефіс, пробіл та апостроф", result.errorMessage)
    }

    @Test
    fun isMiddleNameValidCorrect() {
        val middleName = "Петрович"
        val result = isMiddleNameValid(middleName)
        assertTrue(result.isValid)
    }

    @Test
    fun isMiddleNameValidIncorrectEmpty() {
        val middleName = ""
        val result = isMiddleNameValid(middleName)
        assertFalse(result.isValid)
        assertEquals("Обов'язкове поле", result.errorMessage)
    }

    @Test
    fun isMiddleNameValidIncorrectLength() {
        val middleName = "a"
        val middleName2 = "a".repeat(41)

        val result = isMiddleNameValid(middleName)
        val result2 = isMiddleNameValid(middleName2)

        assertFalse(result.isValid)
        assertEquals("Довжина має бути від 2 до 40 символів", result.errorMessage)
        assertFalse(result2.isValid)
        assertEquals("Довжина має бути від 2 до 40 символів", result2.errorMessage)
    }

    @Test
    fun isMiddleNameValidIncorrectSymbols() {
        val middleName = "invalid@middleName"
        val result = isMiddleNameValid(middleName)
        assertFalse(result.isValid)
        assertEquals("Може містити тільки літери українського алфавіту, дефіс, пробіл та апостроф", result.errorMessage)
    }

    @Test
    fun isPasswordValidCorrect() {
        val password = "validPassword1!"
        val result = isPasswordValid(password)
        assertTrue(result.isValid)
    }

    @Test
    fun isPasswordValidIncorrectEmpty() {
        val password = ""
        val result = isPasswordValid(password)
        assertFalse(result.isValid)
        assertEquals("Обов'язкове поле", result.errorMessage)
    }

    @Test
    fun isPasswordValidIncorrectLength() {
        val password = "a"
        val password2 = "a".repeat(41)

        val result = isPasswordValid(password)
        val result2 = isPasswordValid(password2)

        assertFalse(result.isValid)
        assertEquals("Довжина має бути від 8 до 32 символів", result.errorMessage)
        assertFalse(result2.isValid)
        assertEquals("Довжина має бути від 8 до 32 символів", result2.errorMessage)
    }

    @Test
    fun isPasswordValidIncorrectSymbols() {
        val password = "invalid@password"
        val result = isPasswordValid(password)
        assertFalse(result.isValid)
        assertEquals("Має містити принаймні одну велику літеру, одну цифру та один спеціальний символ", result.errorMessage)
    }

    @Test
    fun isEmailValidCorrect() {
        val email = "email@gmail.com"
        val result = isEmailValid(email)
        assertTrue(result.isValid)
    }

    @Test
    fun isEmailValidIncorrectEmpty() {
        val email = ""
        val result = isEmailValid(email)
        assertFalse(result.isValid)
        assertEquals("Обов'язкове поле", result.errorMessage)
    }

    @Test
    fun isEmailValidIncorrectSymbols() {
        val email = "invalidemail"
        val result = isEmailValid(email)
        assertFalse(result.isValid)
        assertEquals("Невірний формат електронної пошти", result.errorMessage)
    }

}