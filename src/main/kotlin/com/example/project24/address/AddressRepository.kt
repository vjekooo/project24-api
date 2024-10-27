package com.example.project24.address

import org.springframework.data.repository.CrudRepository

interface AddressRepository : CrudRepository<Address, Int?> {}
