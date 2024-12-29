package com.example.project24.address

import com.example.project24.config.ApiMessageResponse
import com.example.project24.config.CustomAuthenticationToken
import com.example.project24.user.User
import com.example.project24.user.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/address")
class AddressController {

    @Autowired
    lateinit var addressService: AddressService

    @Autowired
    lateinit var userService: UserService

    @PostMapping("")
    fun create(@Valid @RequestBody address: Address): ResponseEntity<ApiMessageResponse> {
        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId.toInt()

        val user: Optional<User> = this.userService.getUserDetails(userId)
        if (user.isEmpty) {
            throw IllegalStateException("User not found")
        }
        address.user = user.get()
        this.addressService.createAddress(address)

        return ResponseEntity(
            ApiMessageResponse("Address created successfully"),
            HttpStatus.CREATED
        )
    }

    @GetMapping("")
    fun getUsersAddress(): ResponseEntity<AddressDTO> {
        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId.toInt()

        val address = this.addressService.getAddressByUserId(userId)
        if (address.isEmpty) {
            return ResponseEntity.notFound().build()
        } else {
            val mappedAddress = mapToAddressDTO(address.get())

            return ResponseEntity.ok(mappedAddress)
        }
    }
}
