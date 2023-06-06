package com.example.msstory.dao.storyRepository

import com.example.msstory.dao.Story
import org.springframework.data.repository.CrudRepository

interface StoryRepository: CrudRepository<Story, Long> {
    fun findByStoryId(storyId: Long): Story
    fun findFirstByOrderByStoryIdDesc(): Story
    fun findByCategoryIdAndStatus(categoryId: Long, status: Boolean): List<Story>
}