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
import okhttp3.ResponseBody

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

    private val authVerifyEmailResponseMutable = MutableLiveData<BaseResponse<ResponseBody>>()
    val authVerifyEmailResponse: LiveData<BaseResponse<ResponseBody>> = authVerifyEmailResponseMutable

    private val authRegisterResponseMutable = MutableLiveData<BaseResponse<ResponseBody>>()
    val authRegisterResponse: LiveData<BaseResponse<ResponseBody>> = authRegisterResponseMutable

    private val authRegisterTelegramResponseMutable = MutableLiveData<BaseResponse<ResponseBody>>()
    val authRegisterTelegramResponse: LiveData<BaseResponse<ResponseBody>> = authRegisterTelegramResponseMutable

    private val authForgotPasswordResponseMutable = MutableLiveData<BaseResponse<ResponseBody>>()
    val authForgotPasswordResponse: LiveData<BaseResponse<ResponseBody>> = authForgotPasswordResponseMutable

    private val authVerifyEmailTokenResponseMutable = MutableLiveData<BaseResponse<AuthLoginResponse>>()
    val authVerifyEmailTokenResponse: LiveData<BaseResponse<AuthLoginResponse>> = authVerifyEmailTokenResponseMutable

    private val authCheckResetTokenResponseMutable = MutableLiveData<BaseResponse<CheckResetTokenResponse>>()
    val authCheckResetTokenResponse: LiveData<BaseResponse<CheckResetTokenResponse>> = authCheckResetTokenResponseMutable

    private val authCheckRegisterTelegramResponseMutable = MutableLiveData<BaseResponse<CheckRegisterTelegramResponse>>()
    val authCheckRegisterTelegramResponse: LiveData<BaseResponse<CheckRegisterTelegramResponse>> = authCheckRegisterTelegramResponseMutable

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

    fun register(registrationDTO: RegistrationDTO) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.register(registrationDTO)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    authRegisterResponseMutable.postValue(BaseResponse.Success(response.body() as ResponseBody))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    authRegisterResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun registerWithTelegram(token: String, telegramId: Long) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.registerWithTelegram(token, telegramId)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    authRegisterTelegramResponseMutable.postValue(BaseResponse.Success(response.body() as ResponseBody))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    authRegisterTelegramResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.forgotPassword(email)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    authForgotPasswordResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    authForgotPasswordResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun verifyEmail(email: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.verifyEmail(email)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    authVerifyEmailResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    authVerifyEmailResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun verifyEmailToken(token: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.verifyEmailToken(token)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    authVerifyEmailTokenResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    authVerifyEmailTokenResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
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

    fun checkResetToken(token: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.checkResetToken(token)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authCheckResetTokenResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    authCheckResetTokenResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun checkRegisterTelegram(token: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.checkRegisterTelegram(token)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authCheckRegisterTelegramResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    authCheckRegisterTelegramResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
