package com.fictadvisor.android.data.dto

data class RegistrationDTO(
    val student: StudentDTO,
    val user: UserDTO,
    val telegram: TelegramDTO? = null // not required
)
