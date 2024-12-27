package com.example.project24.user

import com.example.project24.config.CustomAuthenticationToken
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("")
    fun getUserDetails(): Optional<User> {
        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId.toInt()
        return userService.getUserDetails(userId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: User) {
        userService.saveUser(user)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/update")
    fun updateUser(@Valid @RequestBody user: User) {
        val existingUser = userService.getUserByEmail(user.email);
        if (existingUser == null) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            );
        } else {
            user.id = existingUser.id;
            userService.saveUser(user);
        }
    }
}
