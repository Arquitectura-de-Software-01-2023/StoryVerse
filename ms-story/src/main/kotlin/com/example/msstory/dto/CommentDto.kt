package com.example.msstory.dto

import java.util.*

// DTO para la entidad Comment
data class CommentDto(
        val commentId: Long,
        var userId: String,
        val chapterId: Long,
        val description: String,
        val date: Date,
        val status: Boolean
)