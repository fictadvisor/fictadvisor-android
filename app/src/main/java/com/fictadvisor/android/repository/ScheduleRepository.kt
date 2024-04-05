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

    suspend fun getEventsAuthorized(token: String, groupId: String, week: Int, showOwnSelective: Boolean): Response<GetEventResponse> {
        return scheduleService.getEventsAuthorized(token, groupId, week, showOwnSelective)
    }

    suspend fun getEventInfo(token: String, eventId: String, week: Any): Response<DetailedEventResponse> {
        return scheduleService.getEventInfo(token, eventId, week)
    }

    suspend fun deleteEventById(token: String, groupId: String, eventId: String): Response<DetailedEventResponse> {
        return scheduleService.deleteEventById(token, groupId, eventId)
    }

    suspend fun addEvent(token: String, body: PostEventDTO, groupId: String): Response<DetailedEventResponse> {
        return scheduleService.addEvent(token, body, groupId)
    }

    suspend fun editEvent(token: String, body: PatchEventDTO, groupId: String, eventId: String): Response<DetailedEventResponse> {
        return scheduleService.editEvent(token, body, groupId, eventId)
    }
}

