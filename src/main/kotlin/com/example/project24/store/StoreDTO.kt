package com.example.project24.store

import com.example.project24.address.Address
import com.example.project24.product.Product
import java.util.*

data class StoreDTO(
    val id: Long,
    val name: String,
    val description: String,
    val media: List<Media>?,
    val address: Address?,
    val userId: Long,
    val products: List<Product>?,
    val createdAt: Date,
    val updatedAt: Date?
)

fun mapToStoreDTO(store: Store): StoreDTO {
    return StoreDTO(
        id = store.id,
        name = store.name,
        description = store.description,
        media = store.media,
        address = store.address,
        userId = store.user.id,
        products = store.product,
        createdAt = store.createdAt,
        updatedAt = store.updatedAt
    )
}
