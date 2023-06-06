package com.example.msstory.entity

import java.util.Date

data class StoryEntity (
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
    val status: Boolean,
    val tags: List<String>
)