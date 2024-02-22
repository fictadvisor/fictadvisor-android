package com.fictadvisor.android.data.dto

import java.sql.Timestamp

data class ErrorResponse(
    val status: Long,
    val timestamp: String,
    val error: String,
    val message: String,
)
