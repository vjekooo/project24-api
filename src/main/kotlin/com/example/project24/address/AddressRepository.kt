package com.example.project24.address

import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<Address, Int?> {}
