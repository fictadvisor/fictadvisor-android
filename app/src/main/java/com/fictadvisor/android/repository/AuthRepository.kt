package com.fictadvisor.android.repository

import com.fictadvisor.android.data.dto.*
import com.fictadvisor.android.data.remote.RetrofitClient

class AuthRepository() {
    private val authService = RetrofitClient.authApi

    suspend fun login(username: String, password: String) = authService.login()
    suspend fun register(studentInfo: Student, userInfo: User, telegramInfo: Telegram) = authService.register(
        RegistrationRequest(
            studentInfo,
            userInfo,
            telegramInfo,
        ),
    )
    suspend fun loginWithTelegram(telegramInfo: Telegram) = authService.loginTelegram(telegramInfo)

    suspend fun registerWithTelegram(token: String, telegramId: Long) = authService.registerTelegram(
        RegistrationTelegramRequest(
            token,
            telegramId,
        ),
    )

    suspend fun refresh() = authService.refresh()

    suspend fun forgotPassword(email: String) = authService.forgotPassword(
        ForgotPasswordRequest(
            email,
        ),
    )

    suspend fun verifyEmail(email: String) = authService.verifyEmail(
        VerifyEmailRequest(
            email,
        ),
    )

    suspend fun updatePassword(oldPassword: String, newPassword: String) = authService.updatePassword(
        UpdatePasswordDTO(
            oldPassword,
            newPassword,
        ),
    )

    suspend fun getStudent() = authService.getStudent()

    suspend fun verifyIsRegistered(username: String?, email: String?) = authService.verifyIsRegistered(username, email)

    suspend fun checkCaptain(groupId: String) = authService.checkCaptain(groupId)

    suspend fun checkResetToken(token: String) = authService.checkResetToken(token)


}
