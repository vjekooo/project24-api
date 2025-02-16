package com.example.project24.product

import com.example.project24.category.CategoryDTO
import com.example.project24.category.mapToCategoryDTO
import com.example.project24.media.Media
import com.example.project24.media.MediaDTO
import com.example.project24.media.mapToMediaDTO
import java.math.BigDecimal

data class ProductDTO(
    val id: Int,
    val name: String,
    val description: String,
    val media: List<MediaDTO>?,
    val storeId: Long,
    val price: BigDecimal,
    val discount: Double?,
    val finalPrice: BigDecimal?,
    val isFeatured: Boolean?,
    val category: List<CategoryDTO>?
)

fun mapToProductDTO(product: Product): ProductDTO {
    return ProductDTO(
        id = product.id.toInt(),
        name = product.name,
        description = product.description,
        media = product.media?.map { mapToMediaDTO(it) } ?: emptyList(),
        storeId = product.store.id,
        price = product.price,
        discount = product.discount,
        finalPrice = product.calculateFinalPrice(),
        isFeatured = product.isFeatured,
        category = product.category.map { mapToCategoryDTO(it) }
    )
}
