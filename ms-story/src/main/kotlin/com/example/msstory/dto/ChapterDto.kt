package com.example.msstory.dto

import java.util.Date

// DTO para la entidad Chapter
data class ChapterDto (
    val chapterId: Long,
    val storyId: Long,
    val title: String,
    val description: String,
    val publicationDate: Date,
    val status: Boolean
)