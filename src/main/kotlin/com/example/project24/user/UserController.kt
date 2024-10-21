package com.example.project24.user

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepository: UserRepository) {

    @GetMapping
    public fun getUsers(): Iterable<User> {
        return userRepository.findAll();
    }

    @GetMapping("/{userName}")
    public fun findByUsername(@PathVariable userName: String): User? {
        val user = userRepository.findByUserName(userName);
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
    public fun createUser(@Valid @RequestBody user: User) {
        userRepository.save(user);
    }
}
