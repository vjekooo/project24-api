package com.example.project24.store

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

    @Query("""
    SELECT s FROM Store s 
    JOIN s.categories c
    WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) 
       OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
""")
    fun searchStores(@Param("searchTerm") searchTerm: String): List<Store>
}