package com.example.msstory.dto

// DTO para la entidad StoryTag
data class StoryTagDto (
    val storyTagId: Long,
    val storyId: Long,
    val tagId: Long,
    val status: Boolean
)