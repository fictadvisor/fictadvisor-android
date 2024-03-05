package com.fictadvisor.android.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.ErrorResponse
import com.fictadvisor.android.data.dto.schedule.DetailedEventResponse
import com.fictadvisor.android.data.dto.schedule.GetEventResponse
import com.fictadvisor.android.data.dto.schedule.PatchEventDTO
import com.fictadvisor.android.data.dto.schedule.PostEventDTO
import com.fictadvisor.android.repository.ScheduleRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleViewModel(private val mainRepository: ScheduleRepository) : ViewModel() {
    var job: Job? = null
    val mainDispatcher = Dispatchers.Main

    private val getEventsResponseMutable = MutableLiveData<BaseResponse<GetEventResponse>>()
    val getEventsResponse: LiveData<BaseResponse<GetEventResponse>> = getEventsResponseMutable

    private val getEventsAuthorizedResponseMutable = MutableLiveData<BaseResponse<GetEventResponse>>()
    val getEventsAuthorizedResponse: LiveData<BaseResponse<GetEventResponse>> = getEventsAuthorizedResponseMutable

    private val getEventInfoResponseMutable = MutableLiveData<BaseResponse<DetailedEventResponse>>()
    val getEventInfoResponse: LiveData<BaseResponse<DetailedEventResponse>> = getEventInfoResponseMutable

    private val deleteEventByIdResponseMutable = MutableLiveData<BaseResponse<DetailedEventResponse>>()
    val deleteEventByIdResponse: LiveData<BaseResponse<DetailedEventResponse>> = deleteEventByIdResponseMutable

    private val addEventResponseMutable = MutableLiveData<BaseResponse<DetailedEventResponse>>()
    val addEventResponse: LiveData<BaseResponse<DetailedEventResponse>> = addEventResponseMutable

    private val editEventResponseMutable = MutableLiveData<BaseResponse<DetailedEventResponse>>()
    val editEventResponse: LiveData<BaseResponse<DetailedEventResponse>> = editEventResponseMutable

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("ScheduleViewModel", "Exception handled: ${throwable.localizedMessage}")
    }

    fun getEvents(groupId: String, week: Int){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getEvents(groupId, week)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    getEventsResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    getEventsResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun getEventsAuthorized(groupId: String, week: Int, showOwnSelective: Boolean){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getEventsAuthorized(groupId, week, showOwnSelective)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    getEventsAuthorizedResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    getEventsAuthorizedResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun getEventInfo(eventId: String, week: Any){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getEventInfo(eventId, week)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    getEventInfoResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    getEventInfoResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun deleteEventById(groupId: String, eventId: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.deleteEventById(groupId, eventId)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    deleteEventByIdResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    deleteEventByIdResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun addEvent(body: PostEventDTO, groupId: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.addEvent(body, groupId)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    addEventResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    addEventResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun editEvent(body: PatchEventDTO, groupId: String, eventId: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.editEvent(body, groupId, eventId)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    editEventResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    editEventResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }
}