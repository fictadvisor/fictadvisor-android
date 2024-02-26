package com.fictadvisor.android.data.dto.schedule

data class DetailedEventResponse(
    val url: String,
    val eventInfo: String,
    val disciplineType: TDiscipline,
    val disciplineInfo: String,
    val period: TEventPeriod,
    val teachers: List<Teacher>
)
