package com.example.project24.auth

import com.example.project24.user.User
import com.example.project24.user.UserRepository
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(private val userRepository: UserRepository) {

    @PostMapping("/register")
    public fun register(@RequestBody user: User): String {
        userRepository.save(user)
        return "User created"
    }

    @PostMapping("/login")
    public fun login(@RequestBody user: Login, session: HttpSession): String {
        val foundUser = userRepository.findByEmail(user.email);
        if (foundUser == null) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error logging in"
            );
        } else if (foundUser.password != user.password) {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid password"
            );
        } else {
            session.setAttribute("username", user.email)
            val sessionId = session.id
            return "Session created with ID: $sessionId"
        }
    }

    @GetMapping("/logout")
    public fun logout(session: HttpSession): String {
        session.invalidate()
        return "Logged out"
    }
}
