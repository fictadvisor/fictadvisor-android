package com.fictadvisor.android.data.remote.api

import com.fictadvisor.android.data.dto.GetAllGroupsResponse
import retrofit2.Response
import retrofit2.http.GET

interface GroupApi {

    @GET("/v2/groups")
    suspend fun getAllGroups(): Response<GetAllGroupsResponse>
}