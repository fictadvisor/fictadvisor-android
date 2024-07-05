package com.fictadvisor.android.data.dto.user

data class UserDTOResponse(
    val id: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val state: UserGroupState,
    val username: String,
    val email: String,
    val avatar: String,
    val telegramId: Long,
    val group: UserGroup
)
