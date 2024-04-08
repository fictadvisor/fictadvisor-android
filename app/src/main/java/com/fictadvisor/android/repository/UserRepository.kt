package com.fictadvisor.android.repository

import com.fictadvisor.android.data.dto.TelegramDTO
import com.fictadvisor.android.data.dto.user.AddContactBody
import com.fictadvisor.android.data.dto.user.ChangeAvatarResponse
import com.fictadvisor.android.data.dto.user.ChangeInfoBody
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
import com.fictadvisor.android.data.dto.user.UserDTOResponse
import com.fictadvisor.android.data.dto.user.VerifyStudentBody
import com.fictadvisor.android.data.dto.user.VerifyStudentResponse
import com.fictadvisor.android.data.remote.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

class UserRepository {

    private val userService = RetrofitClient.userApi

    suspend fun getUser(token: String, userId: String): Response<SimplifiedUser> {
        return userService.getUser(token, userId)
    }

    suspend fun editUser(token: String, userId: String, body: ChangeUserBody): Response<SimplifiedUser> {
        return userService.editUser(token, userId, body)
    }

    suspend fun changeInfo(token: String, userId: String, body: ChangeInfoBody): Response<UserDTOResponse> {
        return userService.changeInfo(token, userId, body)
    }

    suspend fun linkTelegram(token: String, userId: String, body: TelegramDTO): Response<UserDTOResponse> {
        return userService.linkTelegram(token, userId, body)
    }

    suspend fun addContact(token: String, userId: String, body: AddContactBody): Response<Contact> {
        return userService.addContact(token, userId, body)
    }

    suspend fun getContacts(token: String, userId: String): Response<GetContactsResponse> {
        return userService.getContacts(token, userId)
    }

    suspend fun deleteContact(token: String, userId: String, id: String): Response<ResponseBody> {
        return userService.deleteContact(token, userId, id)
    }

    suspend fun requestNewGroup(token: String, userId: String, body: RequestNewGroupBody): Response<ResponseBody> {
        return userService.requestNewGroup(token, userId, body)
    }

    suspend fun getSelectiveDisciplinesBySemester(token: String, userId: String): Response<GetSelectiveDisciplinesBySemesterResponse> {
        return userService.getSelectiveDisciplinesBySemester(token, userId)
    }

    suspend fun postSelectiveDisciplines(token: String, userId: String, body: PostSelectiveDisciplinesBody): Response<ResponseBody> {
        return userService.postSelectiveDisciplines(token, userId, body)
    }

    suspend fun getSelectiveDisciplines(token: String, userId: String, year: Int, semester: Int): Response<GetSelectiveDisciplinesResponse> {
        return userService.getSelectiveDisciplines(token, userId, year, semester)
    }

    suspend fun changeAvatar(token: String, userId: String, avatar: MultipartBody.Part): Response<ChangeAvatarResponse> {
        return userService.changeAvatar(token, userId, avatar)
    }

    suspend fun setRole(token: String, userId: String, body: ChangeRoleBody): Response<ResponseBody> {
        return userService.setRole(token, userId, body)
    }

    suspend fun getSelective(token: String, userId: String): Response<GetSelectiveResponse> {
        return userService.getSelective(token, userId)
    }

    suspend fun verifyStudent(token: String, userId: String, body: VerifyStudentBody): Response<VerifyStudentResponse> {
        return userService.verifyStudent(token, userId, body)
    }
}