package com.example.project24.user

import com.example.project24.address.Address
import com.example.project24.product.*
import com.example.project24.store.*

data class UserDTO(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: Role?,
    val enabled: Boolean?,
    val address: Address?,
    val stores: List<StoreDTO>?,
    var favoriteProducts: List<ProductDTO>?,
    var favoriteStores: List<StoreDTO>?,
    val createdAt: String,
    val updatedAt: String
)

fun mapToUserDTO(user: User): UserDTO {
    return UserDTO(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        role = user.role,
        enabled = user.enabled,
        address = user.address,
        stores = user.store?.map { mapToStoreDTO(it) },
        favoriteProducts = mutableListOf<ProductDTO>(),
        favoriteStores = mutableListOf<StoreDTO>(),
        createdAt = user.createdAt.toString(),
        updatedAt = user.updatedAt?.toString() ?: ""
    )
}
