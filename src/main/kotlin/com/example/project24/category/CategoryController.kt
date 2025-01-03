package com.example.project24.category

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/category")
class CategoryController {

    @Autowired
    lateinit var categoryService: CategoryService

    @GetMapping("")
    fun getAllCategories(): ResponseEntity<List<Category>> {
        val category = this.categoryService.getCategories()
        return ResponseEntity.ok(category)
    }
}
