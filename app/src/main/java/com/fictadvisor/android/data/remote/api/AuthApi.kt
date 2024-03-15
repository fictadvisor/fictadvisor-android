package com.fictadvisor.android.data.remote.api

import com.fictadvisor.android.data.dto.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("/v2/auth/loginTelegram")
    suspend fun loginTelegram(@Body telegram: TelegramDTO): Response<AuthLoginResponse>

    @POST("/v2/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthLoginResponse>

    @POST("/v2/auth/registerTelegram")
    suspend fun registerTelegram(@Body registrationTelegramRequest: RegisterTelegramDTO): Response<ResponseBody>

    @POST("/v2/auth/register")
    suspend fun register(@Body registrationRequest: RegistrationDTO) : Response<ResponseBody>

    @POST("/v2/auth/refresh")
    suspend fun refresh(): Response<AuthRefreshResponse>

    @POST("/v2/auth/forgotPassword")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordDTO): Response<ResponseBody>

    @POST("/v2/auth/register/verifyEmail")
    suspend fun verifyEmail(@Body verifyEmailRequest: VerificationEmailDTO): Response<ResponseBody>

    @POST("/v2/auth/register/verifyEmail/{token}")
    suspend fun verifyEmailToken(@Path("token") token: String): Response<AuthLoginResponse>

    @PUT("/v2/auth/updatePassword")
    suspend fun updatePassword(@Body updatePasswordRequest: UpdatePasswordDTO): Response<AuthLoginResponse>

    @POST("/v2/auth/resetPassword/{token}")
    suspend fun resetPassword(@Path("token") token: String, @Body resetPasswordRequest: ResetPasswordDTO): Response<AuthLoginResponse>

    @GET("/v2/auth/me")
    suspend fun getStudent(@Header("Authorization") token: String): Response<OrdinaryStudentResponse>

    @GET("/v2/auth/verifyIsRegistered")
    suspend fun verifyIsRegistered(@Query("username") username: String?, @Query("email") email: String?): Response<Boolean>

    @GET("/v2/auth/checkCaptain/{groupId}")
    suspend fun checkCaptain(@Path("groupId") groupId: String): Response<Boolean>

    @GET("/v2/auth/checkResetToken/{token}")
    suspend fun checkResetToken(@Path("token") token: String): Response<CheckResetTokenResponse>

    @GET("/v2/auth/checkRegisterTelegram/{token}")
    suspend fun checkRegisterTelegram(@Path("token") token: String): Response<CheckRegisterTelegramResponse>
}
