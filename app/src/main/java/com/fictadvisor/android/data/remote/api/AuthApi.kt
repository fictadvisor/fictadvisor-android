package com.fictadvisor.android.data.remote.api

import com.fictadvisor.android.data.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/loginTelegram")
    fun loginTelegram(@Body telegramDto: Telegram): Response<AuthLoginResponse>

    @POST("/auth/login")
    fun login(): Response<AuthLoginResponse>

    @POST("/auth/registerTelegram")
    fun registerTelegram(@Body registrationTelegramRequest: RegistrationTelegramRequest)

    @POST("/auth/register")
    fun register(@Body registrationRequest: RegistrationRequest)

    @POST("/auth/refresh")
    fun refresh(): Response<AuthRefreshResponse>

    @POST("/auth/forgotPassword")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest)

    @POST("/auth/verifyEmail")
    fun verifyEmail(@Body verifyEmailRequest: VerifyEmailRequest)
}
