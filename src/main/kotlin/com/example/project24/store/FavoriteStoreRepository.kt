package com.example.project24.store

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FavoriteStoreRepository : JpaRepository<FavoriteStore, Int> {
    @Query(
        value = "SELECT fs FROM FavoriteStore fs WHERE fs.user.id = :userId"
    )
    fun findAllByUserId(userId: Long): List<FavoriteStore>

    fun findByStoreId(productId: Long): FavoriteStore?

    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM FavoriteStore fs WHERE fs.storeId = :storeId"
    )
    fun deleteByStoreId(storeId: Long)
}