package com.fictadvisor.android.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fictadvisor.android.data.dto.AuthLoginResponse
import com.fictadvisor.android.data.dto.AuthRefreshResponse
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.LoginRequest
import com.fictadvisor.android.data.dto.OrdinaryStudentResponse
import com.fictadvisor.android.data.dto.StudentDTO
import com.fictadvisor.android.data.dto.TelegramDTO
import com.fictadvisor.android.data.dto.UserDTO
import com.fictadvisor.android.repository.AuthRepository
import kotlinx.coroutines.*

class AuthViewModel constructor(private val mainRepository: AuthRepository) : ViewModel() {
    var job: Job? = null
    var authLoginResponse = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    var authLoginTelegramResponse = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    var authUpdatePasswordResponse = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    var authRefreshResponse = MutableLiveData<BaseResponse<AuthRefreshResponse>>()
    var authOrdinaryStudentResponse = MutableLiveData<BaseResponse<OrdinaryStudentResponse>>()
    var authIsRegisterResponse = MutableLiveData<BaseResponse<Boolean>>()
    var authCheckCaptainResponse = MutableLiveData<BaseResponse<Boolean>>()

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("AuthViewModel", "Exception handled: ${throwable.localizedMessage}")
    }

    fun login(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        authLoginResponse.postValue(BaseResponse.Loading())

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.login(loginRequest)
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

    fun register(studentInfo: StudentDTO, userInfo: UserDTO, telegramInfo: TelegramDTO) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            mainRepository.register(studentInfo, userInfo, telegramInfo)
        }
    }

    fun registerWithTelegram(token: String, telegramId: Long) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            mainRepository.registerWithTelegram(token, telegramId)
        }
    }

    fun forgotPassword(email: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            mainRepository.forgotPassword(email)
        }
    }

    fun verifyEmail(email: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            mainRepository.verifyEmail(email)
        }
    }

    fun getStudent() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getStudent()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authOrdinaryStudentResponse.postValue(BaseResponse.Success(response.body()))
                } else {
                    authOrdinaryStudentResponse.postValue(BaseResponse.Error(response.message()))
                }
            }
        }
    }

    fun verifyIsRegistered(username: String?, email: String?) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.verifyIsRegistered(username, email)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authIsRegisterResponse.postValue(BaseResponse.Success(response.body()))
                } else {
                    authIsRegisterResponse.postValue(BaseResponse.Error(response.message()))
                }
            }
        }
    }

    fun checkCaptain(groupId: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.checkCaptain(groupId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authCheckCaptainResponse.postValue(BaseResponse.Success(response.body()))
                } else {
                    authCheckCaptainResponse.postValue(BaseResponse.Error(response.message()))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}