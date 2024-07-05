package com.fictadvisor.android.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.ErrorResponse
import com.fictadvisor.android.data.dto.TelegramDTO
import com.fictadvisor.android.data.dto.user.AddContactBody
import com.fictadvisor.android.data.dto.user.ChangeAvatarResponse
import com.fictadvisor.android.data.dto.user.ChangeInfoBody
import com.fictadvisor.android.data.dto.user.ChangeRoleBody
import com.fictadvisor.android.data.dto.user.ChangeUserBody
import com.fictadvisor.android.data.dto.user.Contact
import com.fictadvisor.android.data.dto.user.GetContactsResponse
import com.fictadvisor.android.data.dto.user.GetSelectiveDisciplinesBySemesterResponse
import com.fictadvisor.android.data.dto.user.GetSelectiveDisciplinesResponse
import com.fictadvisor.android.data.dto.user.GetSelectiveResponse
import com.fictadvisor.android.data.dto.user.PostSelectiveDisciplinesBody
import com.fictadvisor.android.data.dto.user.RequestNewGroupBody
import com.fictadvisor.android.data.dto.user.SimplifiedUser
import com.fictadvisor.android.data.dto.user.UserDTOResponse
import com.fictadvisor.android.data.dto.user.VerifyStudentBody
import com.fictadvisor.android.data.dto.user.VerifyStudentResponse
import com.fictadvisor.android.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class UserViewModel(private val mainRepository: UserRepository) : ViewModel() {
    var job: Job? = null
    val mainDispatcher = Dispatchers.Main

    private val getUserResponseMutable = MutableLiveData<BaseResponse<SimplifiedUser>>()
    val getUserResponse: LiveData<BaseResponse<SimplifiedUser>> = getUserResponseMutable

    private val editUserResponseMutable = MutableLiveData<BaseResponse<SimplifiedUser>>()
    val editUserResponse: LiveData<BaseResponse<SimplifiedUser>> = editUserResponseMutable

    private val changeInfoResponseMutable = MutableLiveData<BaseResponse<UserDTOResponse>>()
    val changeInfoResponse: LiveData<BaseResponse<UserDTOResponse>> = changeInfoResponseMutable

    private val linkTelegramResponseMutable = MutableLiveData<BaseResponse<UserDTOResponse>>()
    val linkTelegramResponse: LiveData<BaseResponse<UserDTOResponse>> = linkTelegramResponseMutable

    private val addContactResponseMutable = MutableLiveData<BaseResponse<Contact>>()
    val addContactResponse: LiveData<BaseResponse<Contact>> = addContactResponseMutable

    private val getContactsResponseMutable = MutableLiveData<BaseResponse<GetContactsResponse>>()
    val getContactsResponse: LiveData<BaseResponse<GetContactsResponse>> = getContactsResponseMutable

    private val deleteContactResponseMutable = MutableLiveData<BaseResponse<ResponseBody>>()
    val deleteContactResponse: LiveData<BaseResponse<ResponseBody>> = deleteContactResponseMutable

    private val requestNewGroupResponseMutable = MutableLiveData<BaseResponse<ResponseBody>>()
    val requestNewGroupResponse: LiveData<BaseResponse<ResponseBody>> = requestNewGroupResponseMutable

    private val getSelectiveDisciplinesBySemesterResponseMutable = MutableLiveData<BaseResponse<GetSelectiveDisciplinesBySemesterResponse>>()
    val getSelectiveDisciplinesBySemesterResponse: LiveData<BaseResponse<GetSelectiveDisciplinesBySemesterResponse>> = getSelectiveDisciplinesBySemesterResponseMutable

    private val postSelectiveDisciplinesResponseMutable = MutableLiveData<BaseResponse<ResponseBody>>()
    val postSelectiveDisciplinesResponse: LiveData<BaseResponse<ResponseBody>> = postSelectiveDisciplinesResponseMutable

    private val getSelectiveDisciplinesResponseMutable = MutableLiveData<BaseResponse<GetSelectiveDisciplinesResponse>>()
    val getSelectiveDisciplinesResponse: LiveData<BaseResponse<GetSelectiveDisciplinesResponse>> = getSelectiveDisciplinesResponseMutable

    private val changeAvatarResponseMutable = MutableLiveData<BaseResponse<ChangeAvatarResponse>>()
    val changeAvatarResponse: LiveData<BaseResponse<ChangeAvatarResponse>> = changeAvatarResponseMutable

    private val setRoleResponseMutable = MutableLiveData<BaseResponse<ResponseBody>>()
    val setRoleResponse: LiveData<BaseResponse<ResponseBody>> = setRoleResponseMutable

    private val getSelectiveResponseMutable = MutableLiveData<BaseResponse<GetSelectiveResponse>>()
    val getSelectiveResponse: LiveData<BaseResponse<GetSelectiveResponse>> = getSelectiveResponseMutable

    private val verifyStudentResponseMutable = MutableLiveData<BaseResponse<VerifyStudentResponse>>()
    val verifyStudentResponse: LiveData<BaseResponse<VerifyStudentResponse>> = verifyStudentResponseMutable

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("UserViewModel", "Exception handled: ${throwable.localizedMessage}")
    }

    fun getUser(token: String, userId: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getUser(token, userId)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    getUserResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    getUserResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun editUser(token: String, userId: String, body: ChangeUserBody) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.editUser(token, userId, body)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    editUserResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    editUserResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun changeInfo(token: String, userId: String, body: ChangeInfoBody) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.changeInfo(token, userId, body)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    changeInfoResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    changeInfoResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun linkTelegram(token: String, userId: String, body: TelegramDTO) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.linkTelegram(token, userId, body)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    linkTelegramResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    linkTelegramResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun addContact(token: String, userId: String, body: AddContactBody) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.addContact(token, userId, body)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    addContactResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    addContactResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun getContacts(token: String, userId: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getContacts(token, userId)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    getContactsResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    getContactsResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun deleteContact(token: String, userId: String, id: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.deleteContact(token, userId, id)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    deleteContactResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    deleteContactResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun requestNewGroup(token: String, userId: String, body: RequestNewGroupBody) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.requestNewGroup(token, userId, body)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    requestNewGroupResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    requestNewGroupResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun getSelectiveDisciplinesBySemester(token: String, userId: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getSelectiveDisciplinesBySemester(token, userId)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    getSelectiveDisciplinesBySemesterResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    getSelectiveDisciplinesBySemesterResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun postSelectiveDisciplines(token: String, userId: String, body: PostSelectiveDisciplinesBody) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.postSelectiveDisciplines(token, userId, body)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    postSelectiveDisciplinesResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    postSelectiveDisciplinesResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun getSelectiveDisciplines(token: String, userId: String, year: Int, semester: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getSelectiveDisciplines(token, userId, year, semester)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    getSelectiveDisciplinesResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    getSelectiveDisciplinesResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun changeAvatar(token: String, userId: String, avatar: MultipartBody.Part) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.changeAvatar(token, userId, avatar)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    changeAvatarResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    changeAvatarResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun setRole(token: String, userId: String, body: ChangeRoleBody) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.setRole(token, userId, body)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    setRoleResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    setRoleResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun getSelective(token: String, userId: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getSelective(token, userId)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    getSelectiveResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    getSelectiveResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }

    fun verifyStudent(token: String, userId: String, body: VerifyStudentBody) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.verifyStudent(token, userId, body)
            withContext(mainDispatcher) {
                if (response.isSuccessful) {
                    verifyStudentResponseMutable.postValue(BaseResponse.Success(response.body()))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

                    verifyStudentResponseMutable.postValue(BaseResponse.Error(errorResponse))
                }
            }
        }
    }


}