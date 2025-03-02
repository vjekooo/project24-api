package com.example.project24.user

import com.example.project24.address.Address
import com.example.project24.product.FavoriteProduct
import com.example.project24.product.FavoriteProductDTO
import com.example.project24.product.mapToFavoriteProductDTO
import com.example.project24.product.mapToProductDTO
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
    val favoriteProducts: List<FavoriteProductDTO>?,
    val favoriteStores: List<FavoriteStoreDTO>?,
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
        favoriteProducts = user.favoriteProduct?.map { mapToFavoriteProductDTO(it) },
        favoriteStores = user.favoriteStore?.map{ mapToFavoriteStoreDTO(it) },
        createdAt = user.createdAt.toString(),
        updatedAt = user.updatedAt?.toString() ?: ""
    )
}
