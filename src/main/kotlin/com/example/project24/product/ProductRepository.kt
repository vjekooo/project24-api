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
            WHERE c.name = :category
        """,
        nativeQuery = true
    )
    fun findByFilter(category: String): List<Product>

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

    @Query(
        value = """
            WITH RECURSIVE subcategories AS (
                SELECT c1.id
                FROM category c1
                WHERE c1.name = :storeCategory
                UNION ALL
                SELECT c2.id
                FROM category c2
                INNER JOIN subcategories sc ON c2.parent_id = sc.id
            )
            SELECT p.*
            FROM product p
            JOIN store_category sc ON p.store_id = sc.store_id
            JOIN category c ON sc.category_id = c.id
            WHERE c.id IN (SELECT id FROM subcategories)
        """,
        nativeQuery = true
    )

    fun findAllByStoreCategory(storeCategory: String): List<Product>
}