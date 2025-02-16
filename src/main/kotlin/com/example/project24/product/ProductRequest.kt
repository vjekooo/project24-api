package com.example.project24.product

import com.example.project24.category.CategoryService
import com.example.project24.media.Media
import com.example.project24.store.Store
import java.math.BigDecimal

data class ProductRequest(
    val id: Long?,
    val storeId: Long,
    val name: String,
    val description: String,
    val media: List<String>?,
    val price: BigDecimal?,
    val isFeatured: Boolean?,
    val category: List<String>
)

fun mapRequestToProduct(
    productRequest: ProductRequest,
    store: Store,
    categoryService: CategoryService
): Product {

    val categories = productRequest.category.map { categoryId ->
        categoryService.getCategoryById(categoryId.toLong())
            ?: throw IllegalArgumentException("Category not found for ID: $categoryId")
    }.toMutableSet()

    val product =  Product(
        productRequest.id ?: 0,
        name = productRequest.name,
        description = productRequest.description,
        media = mutableListOf(),
        price = productRequest.price ?: BigDecimal.ZERO,
        isFeatured = productRequest.isFeatured ?: false,
        category = categories,
        store = store
    )

    val mediaList: MutableList<Media> = productRequest.media?.map { mediaUrl ->
        Media(
            id = 0,
            imageUrl = mediaUrl,
        )
    }?.toMutableList() ?: mutableListOf()

    product.media = mediaList

    return product

}


