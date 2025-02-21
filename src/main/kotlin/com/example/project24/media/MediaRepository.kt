package com.example.project24.media

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MediaRepository : JpaRepository<Media, Long> {
    @Query(
        value = "SELECT m FROM Media m WHERE m.product.id = :productId"
    )
    fun findAllByProductId(productId: Long): List<Media>
}