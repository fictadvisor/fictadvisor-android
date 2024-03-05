package com.fictadvisor.android.data.dto.schedule

data class GetEventResponse (
    val week: String,
    val events: List<EventDTO>,
    val startTime: String,
    )