package com.example.msstory.dto

import java.util.*

// DTO para la entidad Writer
data class WriterDto (
    val writerId: Long,
    val userId: String,
    val date: Date,
    val status: Boolean
)