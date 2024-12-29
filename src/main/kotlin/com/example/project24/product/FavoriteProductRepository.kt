package com.example.project24.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FavoriteProductRepository : JpaRepository<FavoriteProduct, Int> {
    @Query(
        value = "SELECT fp FROM FavoriteProduct fp WHERE fp.user.id = :userId"
    )
    fun findAllByUserId(userId: Long): List<FavoriteProduct>
}