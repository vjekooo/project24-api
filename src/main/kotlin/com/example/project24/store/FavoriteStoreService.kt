package com.example.project24.store

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FavoriteStoreService {

    @Autowired
    lateinit var favoriteStoreRepository: FavoriteStoreRepository

    fun saveFavorite(store: FavoriteStore): FavoriteStore {
        return this.favoriteStoreRepository.save(store)
    }

    fun deleteFavoriteByStoreId(storeId: Long) {
        this.favoriteStoreRepository.deleteByStoreId(storeId)
    }

    fun getFavoriteByProductId(storeId: Long): FavoriteStore? {
        return this.favoriteStoreRepository.findByStoreId(
            storeId
        )
    }

    fun getAllUserFavorites(userId: Long): List<FavoriteStore> {
        return this.favoriteStoreRepository.findAllByUserId(userId)
    }
}