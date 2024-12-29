package com.example.project24.product

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FavoriteProductService {

    @Autowired
    lateinit var favoriteProductRepository: FavoriteProductRepository

    fun saveFavorite(product: FavoriteProduct): FavoriteProduct {
        return this.favoriteProductRepository.save(product)
    }

    fun getAllUserFavorites(userId: Long): List<FavoriteProduct> {
        return this.favoriteProductRepository.findAllByUserId(userId)
    }

}