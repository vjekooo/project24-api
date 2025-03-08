package com.example.project24.category

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepository : JpaRepository<Category, Long> {
    @Query(
        value = """
            SELECT id FROM category WHERE parent_id = (SELECT parent_id FROM 
            category WHERE id = :subCategory)
        """,
        nativeQuery = true
    )
    fun findAllSubCategoriesByParentCategory(subCategory: Long): List<Long>
}
