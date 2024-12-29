package com.example.project24.address

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface AddressRepository : JpaRepository<Address, Int?> {
    @Query(
        value = "SELECT a FROM Address a WHERE a.user.id = :userId",
    )
    fun getByUserId(userId: Int): Optional<Address>
}
