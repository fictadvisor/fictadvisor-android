package com.fictadvisor.android.data.dto.schedule

data class PatchEventDTO(
    val week: Int,
    val changeStartDate: Boolean,
    val changeEndDate: Boolean,
    val disciplineType: String? = null,
    val url: String,
    val eventInfo: String,
    val disciplineInfo: String,
    val period: TEventPeriod,
    val teachers: List<String>,
    val disciplineId: String
)
