package com.example.project24.category

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class CategoryWithSub (
    val id: Long,
    val name: String,
    val subCategories: List<Category>
) {

}

@RestController
@RequestMapping("/api/category")
class CategoryController {

    @Autowired
    lateinit var categoryService: CategoryService

    @GetMapping("")
    fun getAllCategories(): ResponseEntity<List<CategoryWithSub>> {
        val categories = this.categoryService.getCategories()
        val subCategories = categories.filter { it.parent != null }
            .groupBy { it.parent?.id }

        val categoryWithSubs = categories.filter { it.parent == null }.map { root ->
            CategoryWithSub(
                id = root.id ?: 0,
                name = root.name,
                subCategories = subCategories[root.id].orEmpty()
            )
        }
        
        return ResponseEntity.ok(categoryWithSubs)
    }
}
