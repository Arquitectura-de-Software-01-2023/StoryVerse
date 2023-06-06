package com.example.msstory.dto

import java.util.Date

// DTO para la entidad Story
data class StoryDto(
        val storyId: Long,
        var writerId: Long,
        val categoryId: Long,
        val title: String,
        val urlCover: String,
        val description: String,
        val audience: String,
        val language: String,
        val publicationDate: Date,
        val votes: Long,
        val status: Boolean
)