package com.example.msuser.dto

import java.util.*

data class AnnouncementDto (
    val announcementId: Long,
    val userId: String,
    val description: String,
    val date: Date,
    val status: Boolean
)