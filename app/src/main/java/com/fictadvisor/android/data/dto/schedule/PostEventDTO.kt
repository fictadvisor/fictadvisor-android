package com.fictadvisor.android.data.dto.schedule

data class PostEventDTO (
    val groupId: String,
    val teachers: List<String>,
    val disciplineId: String,
    val url: String,
    val eventInfo: String,
    val disciplineType: TDiscipline,
    val disciplineInfo: String,
    val period: TEventPeriod,
    )