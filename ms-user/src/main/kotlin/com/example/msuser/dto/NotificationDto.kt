package com.example.msuser.dto

import java.util.Date

data class NotificationDto(
    val message: String = "",
    val type: String = "",
    val date: Date = Date(),
)
