package com.example.msstory.dto

import java.util.*

data class UserDto(
        val sub: String,
        val email: String,
        val preferred_username: String,
        val birthdate: Date,
        val library_private: Boolean?,
        val url_pfp: String?,
        val url_header: String?,
        val description: String?)
