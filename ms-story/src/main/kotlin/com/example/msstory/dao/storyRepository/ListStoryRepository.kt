package com.example.msstory.dao.storyRepository

import com.example.msstory.dao.Story
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.PagingAndSortingRepository

interface ListStoryRepository: PagingAndSortingRepository<Story, Long> {
    fun findByWriterIdNotAndStatusIsTrue(writerId: Long, pageable: PageRequest): List<Story>
    fun findByWriterIdAndStatusIsTrue(writerId: Long, pageable: PageRequest): List<Story>
    fun findByTitleContainingAndStatusIsTrue(title: String, pageable: PageRequest): List<Story>
    fun findByCategoryIdAndStatusIsTrue(categoryId: Long, pageable: PageRequest): List<Story>
}