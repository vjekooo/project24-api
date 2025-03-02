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
        value = """
            SELECT p.*
            FROM product p
            JOIN product_category pc ON p.id = pc.product_id
            JOIN category c ON pc.category_id = c.id
            WHERE c.name IN (:categoryIds)
        """,
        nativeQuery = true
    )
    fun findByFilter(categoryIds: List<String>): List<Product>

    @Query(
        value = """
            SELECT p.* 
            FROM product p
            JOIN product_category pc ON p.id = pc.product_id
            JOIN category c ON pc.category_id = c.id
            WHERE c.id IN (:categoryIds)
            AND p.id != :productId
        """,
        nativeQuery = true
    )
    fun findRelatedProductsByCategories(productId: Long, categoryIds: List<Long>)
    : List<Product>
}