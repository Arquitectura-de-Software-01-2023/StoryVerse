package com.example.msstory.dao.storyRepository

import com.example.msstory.dao.Writer
import org.springframework.data.repository.CrudRepository

interface WriterRepository: CrudRepository<Writer, Long> {
    fun findByUserId(userId: String): Writer?
    fun findByWriterId(writerId: Long): Writer?
}