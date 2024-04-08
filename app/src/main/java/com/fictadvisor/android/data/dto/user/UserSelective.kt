package com.fictadvisor.android.data.dto.user

data class UserSelective(
    val semester: Int,
    val year: Int,
    val disciplines: List<String>,
    val amount: Int
)
