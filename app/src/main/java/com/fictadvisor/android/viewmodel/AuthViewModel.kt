package com.fictadvisor.android.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fictadvisor.android.data.dto.*
import com.fictadvisor.android.repository.AuthRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*

class AuthViewModel(private val mainRepository: AuthRepository) : ViewModel() {
    var job: Job? = null
    val mainDispatcher = Dispatchers.Main

    private val authLoginResponseMutable = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    val authLoginResponse: LiveData<BaseResponse<AuthLoginResponse>> = authLoginResponseMutable

    private val authLoginTelegramResponseMutable = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    val authLoginTelegramResponse: LiveData<BaseResponse<AuthLoginResponse>> = authLoginTelegramResponseMutable

    private val authUpdatePasswordResponseMutable = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    val authUpdatePasswordResponse: LiveData<BaseResponse<AuthLoginResponse>> = authUpdatePasswordResponseMutable

    private val authRefreshResponseMutable = MutableLiveData<BaseResponse<AuthRefreshResponse>>()
    val authRefreshResponse: LiveData<BaseResponse<AuthRefreshResponse>> = authRefreshResponseMutable

    private val authOrdinaryStudentResponseMutable = MutableLiveData<BaseResponse<OrdinaryStudentResponse>>()
    val authOrdinaryStudentResponse: LiveData<BaseResponse<OrdinaryStudentResponse>> = authOrdinaryStudentResponseMutable

    private val authIsRegisterResponseMutable = MutableLiveData<BaseResponse<Boolean>>()
    val authIsRegisterResponse: LiveData<BaseResponse<Boolean>> = authIsRegisterResponseMutable

    private val authCheckCaptainResponseMutable = MutableLiveData<BaseResponse<Boolean>>()
    val authCheckCaptainResponse: LiveData<BaseResponse<Boolean>> = authCheckCaptainResponseMutable

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("AuthViewModel", "Exception handled: ${throwable.localizedMessage}")
    }

    fun login(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        authLoginResponseMutable.postValue(BaseResponse.Loading())

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.login(loginRequest)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    authLoginResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    authLoginResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun loginTelegram(telegramInfo: TelegramDTO) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.loginWithTelegram(telegramInfo)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    authLoginTelegramResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    authLoginTelegramResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.updatePassword(oldPassword, newPassword)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    authUpdatePasswordResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    authUpdatePasswordResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun refresh() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.refresh()
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    authRefreshResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    authRefreshResponseMutable.postValue(BaseResponse.Error(errorResponse))
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
                    authOrdinaryStudentResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    authOrdinaryStudentResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun verifyIsRegistered(username: String?, email: String?) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.verifyIsRegistered(username, email)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authIsRegisterResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    authIsRegisterResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun checkCaptain(groupId: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.checkCaptain(groupId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authCheckCaptainResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    authCheckCaptainResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
