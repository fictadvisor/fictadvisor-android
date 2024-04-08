package com.fictadvisor.android.data.dto.user

data class ChangeUserBody(
    val email: String?,
    val username: String?,
    val state: UserGroupState?
)
