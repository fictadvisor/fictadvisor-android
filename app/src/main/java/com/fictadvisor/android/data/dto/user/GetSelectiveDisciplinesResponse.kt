package com.fictadvisor.android.data.dto.user

data class GetSelectiveDisciplinesResponse(
    val availableSelectiveAmount: Int,
    val year: Int,
    val semester: Int,
    val remainingSelective: List<UserRemainingSelective>
)
