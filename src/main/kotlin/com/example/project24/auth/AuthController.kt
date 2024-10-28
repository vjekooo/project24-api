package com.example.project24.auth

import com.example.project24.user.User
import com.example.project24.user.UserRepository
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val authService: AuthService
) {

    @PostMapping
    fun authenticate(
        @RequestBody authRequest: AuthRequest
    ): AuthResponse =
        authService.authentication(authRequest)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    fun register(@RequestBody user: User) {
        user.password = BCryptPasswordEncoder().encode(user.password)
        userRepository.save(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody user: Login, session: HttpSession): String {
        val foundUser = userRepository.findByEmail(user.email);
        if (foundUser == null) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error logging in"
            );
        } else if (!BCryptPasswordEncoder().matches(
                user.password,
                foundUser.password
            )
        ) {
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
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "Logged out"
    }
}
