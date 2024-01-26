package com.fictadvisor.android.repository

import com.fictadvisor.android.data.remote.RetrofitClient

class GroupRepository() {
    private val groupService = RetrofitClient.groupApi

    suspend fun getAllGroups() = groupService.getAllGroups()
}
