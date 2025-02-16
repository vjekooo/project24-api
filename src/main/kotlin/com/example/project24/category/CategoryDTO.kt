package com.example.project24.category

data class CategoryDTO(
    val id: Long,
    val name: String
)

fun mapToCategoryDTO(category: Category): CategoryDTO {
    return CategoryDTO(
        id = category.id,
        name = category.name
    )
}
