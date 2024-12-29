package com.example.project24.product

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FavoriteProductRepository : JpaRepository<FavoriteProduct, Int> {
    @Query(
        value = "SELECT fp FROM FavoriteProduct fp WHERE fp.user.id = :userId"
    )
    fun findAllByUserId(userId: Long): List<FavoriteProduct>

    fun findByProductId(productId: Long): FavoriteProduct?

    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM FavoriteProduct fp WHERE fp.productId = :productId"
    )
    fun deleteByProductId(productId: Long)
}