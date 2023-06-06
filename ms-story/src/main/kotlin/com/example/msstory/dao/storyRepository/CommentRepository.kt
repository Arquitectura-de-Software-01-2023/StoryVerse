package com.example.msstory.dao.storyRepository

import com.example.msstory.dao.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository: CrudRepository<Comment, Long> {
    fun findByCommentId(commentId: Long): Comment
}