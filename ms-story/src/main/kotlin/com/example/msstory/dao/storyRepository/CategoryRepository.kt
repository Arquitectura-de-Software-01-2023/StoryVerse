package com.example.msstory.dao.storyRepository

import com.example.msstory.dao.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository <Category, Long> {
}