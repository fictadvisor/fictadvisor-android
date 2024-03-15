package com.fictadvisor.android.utils
import android.content.Context
import android.util.Log
import com.fictadvisor.android.data.dto.ExtendedGroupResponse
import com.fictadvisor.android.data.dto.OrdinaryStudentResponse
import com.fictadvisor.android.data.dto.TelegramDTO
import com.fictadvisor.android.data.dto.TokensDTO
import com.google.gson.Gson
import org.json.JSONObject

class StorageUtil(private val context: Context) {

    private val prefName = "userDataPref"
    private val gson = Gson()

    fun setTokens(accessToken: String, refreshToken: String) {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit().apply {
            putString(StorageKeys.ACCESS_TOKEN.name, accessToken)
            putString(StorageKeys.REFRESH_TOKEN.name, refreshToken)
            apply()
        }
    }

    fun setOrdinaryStudentInfo(student: OrdinaryStudentResponse) {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit().apply {
            putString(StorageKeys.STUDENT_FIRST_NAME.name, student.firstName)
            putString(StorageKeys.STUDENT_MIDDLE_NAME.name, student.middleName)
            putString(StorageKeys.STUDENT_LAST_NAME.name, student.lastName)
            putString(StorageKeys.STUDENT_ID.name, student.id)
            putString(StorageKeys.STUDENT_USERNAME.name, student.username)
            putString(StorageKeys.STUDENT_EMAIL.name, student.email)
            putString(StorageKeys.STUDENT_AVATAR.name, student.avatar)
            putLong(StorageKeys.STUDENT_TELEGRAM_ID.name, student.telegramId)
            putString(StorageKeys.STUDENT_GROUP_ID.name, student.group.id)
            putString(StorageKeys.STUDENT_GROUP_CODE.name, student.group.code)
            putString(StorageKeys.STUDENT_GROUP_STATE.name, student.group.state)
            putString(StorageKeys.STUDENT_GROUP_ROLE.name, student.group.role)
            apply()
        }
    }
//AuthLoginResponse and TokensDTO are same
    fun getTokens(): TokensDTO? {
        val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val accessToken = prefs.getString(StorageKeys.ACCESS_TOKEN.name, null)
        val refreshToken = prefs.getString(StorageKeys.REFRESH_TOKEN.name, null)

        return if (accessToken != null && refreshToken != null) {
            TokensDTO(accessToken, refreshToken)
        } else {
            null
        }
    }

    fun getOrdinaryStudentInfo(): OrdinaryStudentResponse? {
        val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val studentFirstName = prefs.getString(StorageKeys.STUDENT_FIRST_NAME.name, null)
        val studentMiddleName = prefs.getString(StorageKeys.STUDENT_MIDDLE_NAME.name, null)
        val studentLastName = prefs.getString(StorageKeys.STUDENT_LAST_NAME.name, null)
        val studentId = prefs.getString(StorageKeys.STUDENT_ID.name, null)
        val studentUsername = prefs.getString(StorageKeys.STUDENT_USERNAME.name, null)
        val studentEmail = prefs.getString(StorageKeys.STUDENT_EMAIL.name, null)
        val studentAvatar = prefs.getString(StorageKeys.STUDENT_AVATAR.name, null)
        val studentTelegramId = prefs.getLong(StorageKeys.STUDENT_TELEGRAM_ID.name, 0)
        val studentGroupId = prefs.getString(StorageKeys.STUDENT_GROUP_ID.name, null)
        val studentGroupCode = prefs.getString(StorageKeys.STUDENT_GROUP_CODE.name, null)
        val studentGroupState = prefs.getString(StorageKeys.STUDENT_GROUP_STATE.name, null)
        val studentGroupRole = prefs.getString(StorageKeys.STUDENT_GROUP_ROLE.name, null)
        val list = listOf(
            studentFirstName,
            studentMiddleName,
            studentLastName,
            studentId,
            studentUsername,
            studentEmail,
            studentAvatar,
            studentGroupId,
            studentGroupCode,
            studentGroupState,
            studentGroupRole
        )

        return if (list.all { it != null }) {
            OrdinaryStudentResponse(
                studentFirstName!!,
                studentMiddleName!!,
                studentLastName!!,
                studentId!!,
                studentUsername!!,
                studentEmail!!,
                studentAvatar!!,
                studentTelegramId,
                ExtendedGroupResponse(studentGroupId!!, studentGroupCode!!, studentGroupState!!, studentGroupRole!!)
            )
        } else {
            Log.e("StorageUtil", "getOrdinaryStudentInfo: some fields are null")
            null
        }
    }

    fun deleteTokens() {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit().apply {
            remove(StorageKeys.ACCESS_TOKEN.name)
            remove(StorageKeys.REFRESH_TOKEN.name)
            apply()
        }
    }

    fun setTelegramInfo(data: TelegramDTO) {
        val jsonObject = JSONObject().apply {
            put(StorageKeys.TELEGRAM_INFO.name, JSONObject().put("telegram", gson.toJson(data)))
        }
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit().apply {
            putString(StorageKeys.TELEGRAM_INFO.name, jsonObject.toString())
            apply()
        }
    }

    fun deleteTelegramInfo() {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit().apply {
            remove(StorageKeys.TELEGRAM_INFO.name)
            apply()
        }
    }

    fun getTelegramInfo(): TelegramDTO? {
        val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val telegramInfoString = prefs.getString(StorageKeys.TELEGRAM_INFO.name, null)

        return if (telegramInfoString != null) {
            val jsonObject = JSONObject(telegramInfoString)
            val telegramJson = jsonObject.optString(StorageKeys.TELEGRAM_INFO.name)
            gson.fromJson(telegramJson, TelegramDTO::class.java)
        } else {
            null
        }
    }
}
