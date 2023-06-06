package com.example.msstory.dao.storyRepository

import com.example.msstory.dao.Chapter
import org.springframework.data.repository.CrudRepository

interface ChapterRepository: CrudRepository<Chapter, Long>{
    abstract fun findByChapterId(chapterId: Long): Chapter
}