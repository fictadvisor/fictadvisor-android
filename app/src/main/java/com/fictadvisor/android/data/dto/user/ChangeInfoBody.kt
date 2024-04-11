package com.fictadvisor.android.data.dto.user

data class ChangeInfoBody(
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val groupId: String?,
    val state: UserGroupState?
)
