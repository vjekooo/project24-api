package com.example.project24.store

import com.example.project24.address.Address
import com.example.project24.category.Category
import com.example.project24.media.MediaDTO
import com.example.project24.media.mapToMediaDTO
import com.example.project24.product.ProductDTO
import com.example.project24.product.mapToProductDTO

data class StoreDTO(
    val id: Long,
    val name: String,
    val description: String,
    val media: List<MediaDTO>?,
    val address: Address?,
    val userId: Long,
    val products: List<ProductDTO>?,
    val categories: List<Category>?,
)

fun mapToStoreDTO(store: Store): StoreDTO {
    return StoreDTO(
        id = store.id,
        name = store.name,
        description = store.description,
        media = store.media?.map { mapToMediaDTO(it) } ?:
        emptyList(),
        address = store.address,
        userId = store.user.id,
        products = store.product?.map { mapToProductDTO(it) },
        categories = store.category.map { Category(it.id, it.name) }
    )
}
