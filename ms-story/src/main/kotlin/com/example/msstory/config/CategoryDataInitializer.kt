package com.example.msstory.config

import com.example.msstory.dao.Category
import com.example.msstory.dao.storyRepository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class CategoryDataInitializer @Autowired constructor (val categoryRepository: CategoryRepository): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val categories = categoryRepository.findAll()
        if (categories.isEmpty()) {
            categoryRepository.save(Category("Aventura",true, 1))
            categoryRepository.save(Category("Historia corta",true, 2))
            categoryRepository.save(Category("Paranormal",true, 3))
            categoryRepository.save(Category("Acción",true, 4))
            categoryRepository.save(Category("Humor",true, 5))
            categoryRepository.save(Category("Romance",true, 6))
            categoryRepository.save(Category("Ciencia Ficción",true, 7))
            categoryRepository.save(Category("Misterio",true, 8))
            categoryRepository.save(Category("Suspenso",true, 9))
            categoryRepository.save(Category("Espiritual",true, 10))
            categoryRepository.save(Category("Novela histórica",true, 11))
            categoryRepository.save(Category("Terror",true, 12))
            categoryRepository.save(Category("Fantasía",true, 13))
            categoryRepository.save(Category("Novela juvenil",true, 14))
            categoryRepository.save(Category("Vampiros",true, 15))
            categoryRepository.save(Category("Otro",true, 16))
        }
    }
}