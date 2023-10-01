package com.fictadvisor.android.repository

import com.fictadvisor.android.data.dto.*
import com.fictadvisor.android.data.remote.RetrofitClient

class AuthRepository() {
    private val authService = RetrofitClient.authApi

    fun login(username: String, password: String) = authService.login()
    fun register(studentInfo: Student, userInfo: User, telegramInfo: Telegram) = authService.register(
        RegistrationRequest(
            studentInfo,
            userInfo,
            telegramInfo,
        ),
    )
    fun loginWithTelegram(telegramInfo: Telegram) = authService.loginTelegram(telegramInfo)

    fun registerWithTelegram(token: String, telegramId: Long) = authService.registerTelegram(
        RegistrationTelegramRequest(
            token,
            telegramId,
        ),
    )

    fun refresh() = authService.refresh()

    fun forgotPassword(email: String) = authService.forgotPassword(
        ForgotPasswordRequest(
            email,
        ),
    )

    fun verifyEmail(email: String) = authService.verifyEmail(
        VerifyEmailRequest(
            email,
        ),
    )
}
