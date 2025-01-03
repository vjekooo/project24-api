package com.example.project24.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    @Query(
        value = "SELECT p FROM Product p WHERE p.store.id = :storeId"
    )
    fun findAllByStoreId(storeId: Long): List<Product>
}