package com.example.project24.product

data class FavoriteProductDTO(
    val id: Long,
    val productId: Long
)

fun mapToFavoriteProductDTO(product: FavoriteProduct): FavoriteProductDTO {
    return FavoriteProductDTO(
        id = product.id,
        productId = product.productId
    )
}
