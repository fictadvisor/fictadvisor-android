package com.fictadvisor.android.data.dto.user

data class UserGroup(
    val id: String,
    val code: String,
    val role: UserGroupRole?,
    val state: UserGroupState
)
