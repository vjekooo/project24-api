package com.example.project24.category

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService {

    @Autowired
    lateinit var repository: CategoryRepository

    fun getCategories(): List<Category> {
        return repository.findAll()
    }

    fun getCategoryById(id: Long): Category? {
        return repository.findById(id).orElse(null)
    }
}