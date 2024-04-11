package com.fictadvisor.android.data.dto.user

data class ChangeAvatarResponse(
    val id: String,
    val email: String,
    val username: String,
    val telegramId: Long,
    val avatar: String,
    val state: UserGroupState
)
