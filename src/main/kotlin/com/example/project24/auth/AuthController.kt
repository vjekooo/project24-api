package com.example.project24.auth

import com.example.project24.user.User
import com.example.project24.user.UserRepository
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val authService: AuthService,
    private val tokenService: TokenService,
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
    fun login(
        @RequestBody credentials: AuthRequest,
        session: HttpSession,
        response: HttpServletResponse,
    ): AuthResponse {
        val token = authService.authentication(credentials)
//        val cookie = tokenService.createCookie(token.accessToken)
//        return response.addHeader("Set-Cookie", cookie)

        return token
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest
    ): TokenResponse =
        authService.refreshAccessToken(request.token)
            ?.mapToTokenResponse()
            ?: throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Invalid refresh token."
            )

    private fun String.mapToTokenResponse(): TokenResponse =
        TokenResponse(
            token = this
        )

    @GetMapping("/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "Logged out"
    }
}
