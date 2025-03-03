package com.example.project24.store

import com.example.project24.product.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : JpaRepository<Store, Long> {

    @Query(
        value = "SELECT * FROM store WHERE user = :id",
        nativeQuery = true
    )
    fun findAllByUserId(@Param("id") id: Long): List<Store>?

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

}