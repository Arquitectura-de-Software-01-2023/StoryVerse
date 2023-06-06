package com.example.msstory.dao.storyRepository

import com.example.msstory.dao.Library
import org.springframework.data.repository.CrudRepository

interface LibraryRepository: CrudRepository<Library, Long> {
    abstract fun findByUserIdAndStoryId(userId: String, storyId: Long): Library?
    abstract fun findByStoryIdAndUserId(storyId: Long, userId: String): Library
}