package com.example.msstory.dto

// DTO (paquete de ayuda)
data class ResponseDto<T> (
    val data: T,
    val message: String,
    val success: Boolean
)