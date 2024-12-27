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
}