package com.example.msstory.dao.storyRepository

import com.example.msstory.dao.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository: JpaRepository <Tag, Long> {
    abstract fun findByName(name: String): Tag?
    abstract fun findByTagId(tagId: Long): Tag
}