package com.example.project24.store

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : JpaRepository<Store, Long> {

    @Query(
        value = """
            SELECT * FROM store 
            WHERE user_id = :userId 
            ORDER BY created_at DESC
        """,
        nativeQuery = true
    )
    fun findAllByUserId(userId: Long): List<Store>?

    @Query(
        value = "select s from Store s ORDER BY s.createdAt DESC LIMIT 6"
    )
    fun findLatest(): List<Store>

    @Query(
        value = """
            SELECT s.* 
            FROM store s
            JOIN product p ON p.store_id = s.id
            WHERE p.id = :productId
        """,
        nativeQuery = true
    )
    fun findByProductId(productId: Long): Store?

    @Query(
        value = """
            SELECT s.*
            FROM store s
            JOIN store_category sc ON s.id = sc.store_id
            JOIN category c ON sc.category_id = c.id
            WHERE c.name = :category
        """,
        nativeQuery = true
    )
    fun findByFilter(category: String): List<Store>

    @Query(
        value = """
        WITH RECURSIVE root_category AS (
            SELECT c1.*
            FROM category c1
            WHERE c1.name = :subCategory
            UNION ALL
            SELECT c2.*
            FROM category c2
            INNER JOIN root_category rc ON rc.parent_id = c2.id
        )
        SELECT DISTINCT s.*
        FROM store s
        JOIN store_category sc ON sc.store_id = s.id
        WHERE sc.category_id IN (
            SELECT id 
            FROM root_category
            WHERE parent_id IS NULL  -- Root category has no parent
        )
    """,
        nativeQuery = true
    )
    fun findAllStoresBySubcategory(subCategory: String): List<Store>

    @Modifying
    @Query("UPDATE Store s SET s.viewCount = s.viewCount + 1 WHERE s.id = :storeId")
    fun incrementViewCount(storeId: Long)
}