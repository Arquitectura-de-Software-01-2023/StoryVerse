package com.example.msstory.dao.storyRepository

import com.example.msstory.dao.StoryTag
import org.springframework.data.jpa.repository.JpaRepository

interface StoryTagRepository: JpaRepository <StoryTag, Long> {
    abstract fun findByStoryId(storyId: Long): List<StoryTag>
    abstract fun findByStoryIdAndTagId(storyId: Long, tagId: Long): StoryTag?
}