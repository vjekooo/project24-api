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

    @Query(
        value = "SELECT p.*" +
                "FROM product p" +
                "         JOIN product_category pc ON p.id = pc.product_id" +
                "         JOIN category c ON pc.category_id = c.id WHERE c" +
                ".name IN (:categories)",
        nativeQuery = true
    )
    fun findByFilter(categories: List<String>): List<Product>
}