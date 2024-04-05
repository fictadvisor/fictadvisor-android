package com.fictadvisor.android.data.dto.schedule

data class EventDTO(
    val id: String,
    val name: String,
    val startTime: String,
    val endTime: String,
    val disciplineType: DisciplineTypeDTO?
)


