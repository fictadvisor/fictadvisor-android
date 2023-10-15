package com.fictadvisor.android.data.remote.api

import com.fictadvisor.android.data.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.Error

interface AuthApi {

    @POST("/auth/loginTelegram")
    suspend fun loginTelegram(@Body telegram: TelegramDTO): Response<AuthLoginResponse>

    @POST("/v2/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthLoginResponse>

    @POST("/auth/registerTelegram")
    suspend fun registerTelegram(@Body registrationTelegramRequest: RegisterTelegramDTO)

    @POST("/auth/register")
    suspend fun register(@Body registrationRequest: RegistrationDTO)

    @POST("/auth/refresh")
    suspend fun refresh(): Response<AuthRefreshResponse>

    @POST("/auth/forgotPassword")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordDTO)

    @POST("/auth/verifyEmail")
    suspend fun verifyEmail(@Body verifyEmailRequest: VerificationEmailDTO)

    @PUT("/auth/updatePassword")
    suspend fun updatePassword(@Body updatePasswordRequest: UpdatePasswordDTO): Response<AuthLoginResponse>

    @GET("/v2/auth/me")
    suspend fun getStudent(): Response<OrdinaryStudentResponse>

    @GET("/v2/auth/verifyIsRegistered")
    suspend fun verifyIsRegistered(@Query("username") username: String?, @Query("email") email: String?): Response<Boolean>

    @GET("/v2/auth/checkCaptain/{groupId}")
    suspend fun checkCaptain(@Path("groupId") groupId: String): Response<Boolean>

    @GET("/v2/auth/checkResetToken/{token}")
    suspend fun checkResetToken(@Path("token") token: String): Response<CheckResetTokenResponse>

    @GET("/v2/auth/checkRegisterTelegram/{token}")
    suspend fun checkRegisterTelegram(@Path("token") token: String): Response<CheckRegisterTelegramResponse>
}
