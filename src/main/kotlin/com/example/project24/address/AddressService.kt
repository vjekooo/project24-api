package com.example.project24.address

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AddressService {

    @Autowired
    lateinit var addressRepository: AddressRepository

    fun createAddress(address: Address): Address {
        return this.addressRepository.save(address)
    }

    fun getAddressByUserId(id: Int): Optional<Address> {
        return this.addressRepository.getByUserId(id)
    }
}