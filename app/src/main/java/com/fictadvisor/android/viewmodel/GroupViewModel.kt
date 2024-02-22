package com.fictadvisor.android.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.ErrorResponse
import com.fictadvisor.android.data.dto.GetAllGroupsResponse
import com.fictadvisor.android.repository.GroupRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupViewModel(private val mainRepository: GroupRepository) : ViewModel() {
    var job: Job? = null
    val mainDispatcher = Dispatchers.Main

    private val getAllGroupsResponseMutable = MutableLiveData<BaseResponse<GetAllGroupsResponse>>()
    val getAllGroupsResponse: LiveData<BaseResponse<GetAllGroupsResponse>> = getAllGroupsResponseMutable
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("GroupViewModel", "Exception handled: ${throwable.localizedMessage}")
    }
    fun getAllGroups() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getAllGroups()
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    getAllGroupsResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    getAllGroupsResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }
}
