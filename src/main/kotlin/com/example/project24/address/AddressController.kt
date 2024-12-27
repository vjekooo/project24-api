package com.example.project24.address

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/address")
class AddressController(private val addressRepository: AddressRepository) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    fun create(@Valid @RequestBody address: Address) {
        addressRepository.save(address);
    }
}
