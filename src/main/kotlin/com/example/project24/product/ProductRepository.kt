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
        value = "SELECT p.*\n" +
                "FROM product p\n" +
                "         JOIN product_category pc ON p.id = pc.product_id\n" +
                "         JOIN category c ON pc.category_id = c.id\n" +
                "WHERE c.name LIKE :category",
        nativeQuery = true
    )
    fun findByFilter(category: String?): List<Product>
}