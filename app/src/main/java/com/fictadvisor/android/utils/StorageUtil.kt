package com.fictadvisor.android.utils
import android.content.Context
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

    fun setEmail(email: String) {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit().apply {
            putString(StorageKeys.EMAIL.name, email)
            apply()
        }
    }

    fun getEmail(): String? {
        val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return prefs.getString(StorageKeys.EMAIL.name, null)
    }
}
