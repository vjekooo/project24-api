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
    lateinit var addressRepository: AddressRepository

    @Autowired
    lateinit var userService: UserService

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    fun create(@Valid @RequestBody address: Address): ResponseEntity<ApiMessageResponse> {
        println(address)
        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId.toInt()

        val user: Optional<User> = userService.getUserDetails(userId)
        if (user.isEmpty) {
            throw IllegalStateException("User not found")
        }
        address.user = user.get()
        addressRepository.save(address)

        return ResponseEntity(
            ApiMessageResponse("Address created successfully"),
            HttpStatus.CREATED
        )


    }
}
