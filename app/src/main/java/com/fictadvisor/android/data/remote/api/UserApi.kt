package com.fictadvisor.android.data.remote.api

import com.fictadvisor.android.data.dto.TelegramDTO
import com.fictadvisor.android.data.dto.user.AddContactBody
import com.fictadvisor.android.data.dto.user.ChangeAvatarResponse
import com.fictadvisor.android.data.dto.user.ChangeInfoBody
import com.fictadvisor.android.data.dto.user.UserDTOResponse
import com.fictadvisor.android.data.dto.user.ChangeRoleBody
import com.fictadvisor.android.data.dto.user.ChangeUserBody
import com.fictadvisor.android.data.dto.user.Contact
import com.fictadvisor.android.data.dto.user.GetContactsResponse
import com.fictadvisor.android.data.dto.user.GetSelectiveDisciplinesBySemesterResponse
import com.fictadvisor.android.data.dto.user.GetSelectiveDisciplinesResponse
import com.fictadvisor.android.data.dto.user.GetSelectiveResponse
import com.fictadvisor.android.data.dto.user.PostSelectiveDisciplinesBody
import com.fictadvisor.android.data.dto.user.RequestNewGroupBody
import com.fictadvisor.android.data.dto.user.SimplifiedUser
import com.fictadvisor.android.data.dto.user.VerifyStudentBody
import com.fictadvisor.android.data.dto.user.VerifyStudentResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("/v2/users/{userId}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<SimplifiedUser>

    @PATCH("/v2/users/{userId}")
    suspend fun editUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body body: ChangeUserBody
    ): Response<SimplifiedUser>

    @PATCH("/v2/users/{userId}/student")
    suspend fun changeInfo(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body body: ChangeInfoBody
    ): Response<UserDTOResponse>

    @POST("/v2/users/{userId}/telegram")
    suspend fun linkTelegram(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body body: TelegramDTO
    ): Response<UserDTOResponse>

    @POST("/v2/users/{userId}/contacts")
    suspend fun addContact(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body body: AddContactBody
    ): Response<Contact>

    @GET("/v2/users/{userId}/contacts")
    suspend fun getContacts(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<GetContactsResponse>

    @DELETE("/v2/users/{userId}/contacts/{id}")
    suspend fun deleteContact(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("id") id: String
    ): Response<ResponseBody>

    @PATCH("/v2/users/{userId}/requestNewGroup")
    suspend fun requestNewGroup(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body body: RequestNewGroupBody
    ): Response<ResponseBody>

    @GET("/v2/users/{userId}/selectiveBySemesters")
    suspend fun getSelectiveDisciplinesBySemester(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<GetSelectiveDisciplinesBySemesterResponse>

    @POST("/v2/users/{userId}/selectiveDisciplines")
    suspend fun postSelectiveDisciplines(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body body: PostSelectiveDisciplinesBody
    ): Response<ResponseBody>

    @GET("/v2/users/{userId}/selectiveDisciplines")
    suspend fun getSelectiveDisciplines(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Query("year") year: Int,
        @Query("semester") semester: Int
    ): Response<GetSelectiveDisciplinesResponse>

    @Multipart
    @PATCH("/v2/users/{userId}/")
    suspend fun changeAvatar(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Part avatar: MultipartBody.Part
    ): Response<ChangeAvatarResponse>

    @POST("/v2/users/{userId}/roles")
    suspend fun setRole(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body body: ChangeRoleBody
    ): Response<ResponseBody>

    @GET("/v2/users/{userId}/selective")
    suspend fun getSelective(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<GetSelectiveResponse>

    @PATCH("/v2/users/{userId}/verifyStudent")
    suspend fun verifyStudent(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body body: VerifyStudentBody
    ): Response<VerifyStudentResponse>
}
