package com.fictadvisor.android.repository

import com.fictadvisor.android.data.dto.*
import com.fictadvisor.android.data.remote.RetrofitClient

class AuthRepository() {
    private val authService = RetrofitClient.authApi

    suspend fun login(loginRequest: LoginRequest) = authService.login(loginRequest)
    suspend fun register(studentInfo: StudentDTO, userInfo: UserDTO, telegramInfo: TelegramDTO) = authService.register(
        RegistrationDTO(
            studentInfo,
            userInfo,
            telegramInfo,
        ),
    )
    suspend fun loginWithTelegram(telegramInfo: TelegramDTO) = authService.loginTelegram(telegramInfo)

    suspend fun registerWithTelegram(token: String, telegramId: Long) = authService.registerTelegram(
        RegisterTelegramDTO(
            token,
            telegramId,
        ),
    )

    suspend fun refresh() = authService.refresh()

    suspend fun forgotPassword(email: String) = authService.forgotPassword(
        ForgotPasswordDTO(
            email,
        ),
    )

    suspend fun verifyEmail(email: String) = authService.verifyEmail(
        VerificationEmailDTO(
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
