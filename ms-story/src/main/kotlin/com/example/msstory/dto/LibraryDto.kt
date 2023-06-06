package com.example.msstory.dto

import java.util.*

// DTO para la entidad Library
data class LibraryDto (
    val libraryId: Long,
    val userId: String,
    val storyId: Long,
    val date: Date,
    val status: Boolean
)