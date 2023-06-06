package com.example.msstory.dto

import java.util.*

// DTO para la entidad Category
data class CategoryDto (
    val categoryId: Long,
    val name: String,
    val status: Boolean
)