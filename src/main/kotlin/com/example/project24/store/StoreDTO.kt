package com.example.project24.store

import com.example.project24.address.Address
import com.example.project24.user.User
import java.util.*

data class StoreDTO(
    val id: Long,
    val name: String,
    val description: String,
    val media: List<Media>?,
    val address: Address?,
    val user: User,
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
        user = store.user,
        createdAt = store.createdAt,
        updatedAt = store.updatedAt
    )
}
