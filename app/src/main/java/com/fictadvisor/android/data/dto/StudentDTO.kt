package com.fictadvisor.android.data.dto

data class StudentDTO(
    val groupId: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val isCaptain: Boolean,
){
    fun isEmpty(): Boolean {
        return groupId.isEmpty() || firstName.isEmpty() || middleName.isEmpty() || lastName.isEmpty()
    }
}