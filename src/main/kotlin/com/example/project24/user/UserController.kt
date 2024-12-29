package com.example.project24.user

import com.example.project24.config.ApiMessageResponse
import com.example.project24.config.CustomAuthenticationToken
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("")
    fun getUserDetails(): ResponseEntity<UserDTO> {
        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId.toInt()
        val user = userService.getUserDetails(userId)

        val mappedUser = mapToUserDTO(user.get())
        return mappedUser.let { ResponseEntity.ok(it) }
    }

    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: User): ResponseEntity<ApiMessageResponse> {
        userService.saveUser(user)
        return ResponseEntity(
            ApiMessageResponse("User created successfully"),
            HttpStatus.CREATED
        )
    }

    @PutMapping("/update")
    fun updateUser(@Valid @RequestBody user: User): ResponseEntity<ApiMessageResponse> {
        val existingUser = userService.getUserByEmail(user.email);
        if (existingUser == null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User not found"
            )
        } else {
            user.id = existingUser.id;
            userService.saveUser(user);
            return ResponseEntity(
                ApiMessageResponse("User updated successfully"),
                HttpStatus.OK
            )
        }
    }
}
