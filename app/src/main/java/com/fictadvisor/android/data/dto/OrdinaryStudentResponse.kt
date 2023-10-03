package com.fictadvisor.android.data.dto

data class OrdinaryStudentResponse(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val id: String,
    val username: String,
    val email: String,
    val avatar: String,
    val telegramId: Long,
    val group: ExtendedGroupResponse,
)
