package com.fictadvisor.android.data.remote.api

import com.fictadvisor.android.data.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/loginTelegram")
    suspend fun loginTelegram(@Body telegramDto: Telegram): Response<AuthLoginResponse>

    @POST("/auth/login")
    suspend fun login(): Response<AuthLoginResponse>

    @POST("/auth/registerTelegram")
    suspend fun registerTelegram(@Body registrationTelegramRequest: RegistrationTelegramRequest)

    @POST("/auth/register")
    suspend fun register(@Body registrationRequest: RegistrationRequest)

    @POST("/auth/refresh")
    suspend fun refresh(): Response<AuthRefreshResponse>

    @POST("/auth/forgotPassword")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest)

    @POST("/auth/verifyEmail")
    suspend fun verifyEmail(@Body verifyEmailRequest: VerifyEmailRequest)
}
