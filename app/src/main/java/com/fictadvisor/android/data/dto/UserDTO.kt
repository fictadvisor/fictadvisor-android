package com.fictadvisor.android.data.dto

data class UserDTO(
    val username: String,
    val email: String,
    val password: String
) {
    fun isEmpty(): Boolean {
        return username.isEmpty() || email.isEmpty() || password.isEmpty()
    }
}