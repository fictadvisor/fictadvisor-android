package com.fictadvisor.android.data.dto.schedule

data class PatchEventDTO(
    val week: Int,
    val name: String,
    val changeStartDate: Boolean,
    val changeEndDate: Boolean,
    val eventType: TDiscipline? = null,
    val url: String,
    val eventInfo: String,
    val disciplineInfo: String,
    val period: TEventPeriod,
    val teachers: List<String>,
    val disciplineId: String,
    val startTime: String,
    val endTime: String
)
