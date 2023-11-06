package com.fictadvisor.android.data.dto

import okhttp3.ResponseBody

sealed class BaseResponse<out T> {
    data class Success<out T>(val data: T? = null) : BaseResponse<T>()
    data class Loading(val nothing: Nothing? = null) : BaseResponse<Nothing>()
    data class Error(val error: ErrorResponse? = null) : BaseResponse<Nothing>()
}
