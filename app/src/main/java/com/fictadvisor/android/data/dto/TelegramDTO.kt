package com.fictadvisor.android.data.dto

import com.google.gson.annotations.SerializedName

data class TelegramDTO(
    @SerializedName("auth_date")
    val authDate: Long,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("photo_url")
    val photoUrl: String,
    val hash: String,
    val id: Long,
    val username: String

)