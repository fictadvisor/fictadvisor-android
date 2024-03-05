package com.fictadvisor.android.repository

import com.fictadvisor.android.data.dto.schedule.DetailedEventResponse
import com.fictadvisor.android.data.dto.schedule.GetEventResponse
import com.fictadvisor.android.data.dto.schedule.PatchEventDTO
import com.fictadvisor.android.data.dto.schedule.PostEventDTO
import com.fictadvisor.android.data.remote.RetrofitClient
import retrofit2.Response

class ScheduleRepository {
    private val scheduleService = RetrofitClient.scheduleApi

    suspend fun getEvents(groupId: String, week: Int): Response<GetEventResponse> {
        return scheduleService.getEvents(groupId, week)
    }

    suspend fun getEventsAuthorized(groupId: String, week: Int, showOwnSelective: Boolean): Response<GetEventResponse> {
        return scheduleService.getEventsAuthorized(groupId, week, showOwnSelective)
    }

    suspend fun getEventInfo(eventId: String, week: Any): Response<DetailedEventResponse> {
        return scheduleService.getEventInfo(eventId, week)
    }

    suspend fun deleteEventById(groupId: String, eventId: String): Response<DetailedEventResponse> {
        return scheduleService.deleteEventById(groupId, eventId)
    }

    suspend fun addEvent(body: PostEventDTO, groupId: String): Response<DetailedEventResponse> {
        return scheduleService.addEvent(body, groupId)
    }

    suspend fun editEvent(body: PatchEventDTO, groupId: String, eventId: String): Response<DetailedEventResponse> {
        return scheduleService.editEvent(body, groupId, eventId)
    }
}

