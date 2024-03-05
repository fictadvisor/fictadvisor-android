package com.fictadvisor.android.data.remote.api

import com.fictadvisor.android.data.dto.schedule.DetailedEventResponse
import com.fictadvisor.android.data.dto.schedule.GetEventResponse
import com.fictadvisor.android.data.dto.schedule.PatchEventDTO
import com.fictadvisor.android.data.dto.schedule.PostEventDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {
    @GET("/v2/schedule/groups/{groupId}/general")
    suspend fun getEvents(
        @Path("groupId") groupId: String,
        @Query("week") week: Int,
        @Query("addLecture") addLecture: Boolean = true,
        @Query("addLaboratory") addLaboratory: Boolean = true,
        @Query("addPractice") addPractice: Boolean = true
    ): Response<GetEventResponse>

    @GET("/v2/schedule/groups/{groupId}/events")
    suspend fun getEventsAuthorized(
        @Path("groupId") groupId: String,
        @Query("week") week: Int,
        @Query("showOwnSelective") showOwnSelective: Boolean,
        @Query("addLecture") addLecture: Boolean = true,
        @Query("addLaboratory") addLaboratory: Boolean = true,
        @Query("addPractice") addPractice: Boolean = true,
        @Query("otherEvents") otherEvents: Boolean = true
    ): Response<GetEventResponse>

    @GET("/v2/schedule/events/{eventId}")
    suspend fun getEventInfo(
        @Path("eventId") eventId: String,
        @Query("week") week: Any // You can define the type accordingly
    ): Response<DetailedEventResponse>

    @DELETE("/v2/schedule/groups/{groupId}/events/{eventId}")
    suspend fun deleteEventById(
        @Path("groupId") groupId: String,
        @Path("eventId") eventId: String
    ): Response<DetailedEventResponse>

    @POST("/v2/schedule/events")
    suspend fun addEvent(
        @Body body: PostEventDTO,
        @Query("groupId") groupId: String
    ): Response<DetailedEventResponse>

    @PATCH("/v2/schedule/groups/{groupId}/events/{eventId}")
    suspend fun editEvent(
        @Body body: PatchEventDTO,
        @Path("groupId") groupId: String,
        @Path("eventId") eventId: String
    ): Response<DetailedEventResponse>
}