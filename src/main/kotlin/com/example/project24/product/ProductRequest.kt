package com.example.project24.product

import com.example.project24.category.CategoryService
import com.example.project24.store.Store
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal

data class ProductRequest(
    val id: String?,
    val storeId: String,
    val name: String,
    val description: String,
    val existingImages: List<String>?,
    val newImages: List<MultipartFile>?,
    val price: BigDecimal?,
    val isFeatured: Boolean?,
    val category: List<String>
)

fun mapRequestToProduct(
    productRequest: ProductRequest,
    store: Store,
    categoryService: CategoryService,
): Product {

    val categories = productRequest.category.map { categoryId ->
        categoryService.getCategoryById(categoryId.toLong())
            ?: throw IllegalArgumentException("Category not found for ID: $categoryId")
    }.toMutableList()

    val product =  Product(
        productRequest.id?.toLong() ?: 0,
        name = productRequest.name,
        description = productRequest.description,
        media = mutableListOf(),
        price = productRequest.price ?: BigDecimal.ZERO,
        isFeatured = productRequest.isFeatured ?: false,
        category = categories,
        store = store
    )
    return product
}


