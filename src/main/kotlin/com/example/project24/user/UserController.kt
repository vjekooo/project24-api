package com.example.project24.user

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepository: UserRepository) {

    @GetMapping("")
    fun findByEmail(authentication: Authentication): User? {
        val email = authentication.name
        val user = userRepository.findByEmail(email);
        if (user == null) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            );
        } else {
            return user;
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: User) {
        userRepository.save(user);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/update")
    fun updateUser(@Valid @RequestBody user: User) {
        val existingUser = userRepository.findByEmail(user.email);
        if (existingUser == null) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            );
        } else {
            user.id = existingUser.id;
            userRepository.save(user);
        }
    }
}
