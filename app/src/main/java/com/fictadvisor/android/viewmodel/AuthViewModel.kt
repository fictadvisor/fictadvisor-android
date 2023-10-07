package com.fictadvisor.android.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fictadvisor.android.data.dto.AuthLoginResponse
import com.fictadvisor.android.data.dto.AuthRefreshResponse
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.TelegramDTO
import com.fictadvisor.android.repository.AuthRepository
import kotlinx.coroutines.*

class AuthViewModel constructor(private val mainRepository: AuthRepository) : ViewModel() {
    var job: Job? = null
    var authLoginResponse = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    var authLoginTelegramResponse = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    var authUpdatePasswordResponse = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    var authRefreshResponse = MutableLiveData<BaseResponse<AuthRefreshResponse>>()


    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("AuthViewModel", "Exception handled: ${throwable.localizedMessage}")
    }

    fun login(username:String, password:String) {
        authLoginResponse.postValue(BaseResponse.Loading())

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.login(username, password)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authLoginResponse.postValue(BaseResponse.Success(response.body()))
                } else {
                    authLoginResponse.postValue(BaseResponse.Error(response.message()))
                }
            }
        }

    }

    fun loginTelegram(telegramInfo: TelegramDTO) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.loginWithTelegram(telegramInfo)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authLoginTelegramResponse.postValue(BaseResponse.Success(response.body()))
                } else {
                    authLoginTelegramResponse.postValue(BaseResponse.Error(response.message()))
                }
            }
        }
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.updatePassword(oldPassword, newPassword)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authUpdatePasswordResponse.postValue(BaseResponse.Success(response.body()))
                } else {
                    authUpdatePasswordResponse.postValue(BaseResponse.Error(response.message()))
                }
            }
        }
    }

    fun refresh() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.refresh()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authRefreshResponse.postValue(BaseResponse.Success(response.body()))
                } else {
                    authRefreshResponse.postValue(BaseResponse.Error(response.message()))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}