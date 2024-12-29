package com.example.project24.store

data class FavoriteStoreDTO(
    val id: Long,
    val storeId: Long
)

fun mapToFavoriteStoreDTO(store: FavoriteStore): FavoriteStoreDTO {
    return FavoriteStoreDTO(
        id = store.id,
        storeId = store.storeId
    )
}
