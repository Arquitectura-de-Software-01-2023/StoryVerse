package com.example.msuser.dto

data class ChangePasswordDto(
    val type: String,
    val value: String,
    val temporary: Boolean
)