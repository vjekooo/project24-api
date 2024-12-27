package com.example.project24.auth

import com.example.project24.config.ApiResponse
import com.example.project24.user.User
import com.example.project24.user.UserRepository
import com.example.project24.user.UserService
import com.example.project24.verificationToken.OnRegistrationCompleteEvent
import com.example.project24.verificationToken.VerificationToken
import com.example.project24.verificationToken.VerificationTokenService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @Autowired
    lateinit var eventPublisher: ApplicationEventPublisher

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var verificationTokenService: VerificationTokenService

    @PostMapping
    fun authenticate(
        @RequestBody authRequest: AuthRequest
    ): AuthResponse =
        authService.authentication(authRequest)

    @PostMapping("/register")
    fun register(
        @RequestBody user: User,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<ApiResponse> {
        val appUrl = getBaseUrl(response)
        user.password = BCryptPasswordEncoder().encode(user.password)
        val registered = userRepository.save(user)
        eventPublisher.publishEvent(
            OnRegistrationCompleteEvent(
                registered,
                request.locale, appUrl
            )
        )
        return ResponseEntity(
            ApiResponse("User registered successfully. Check your email for verification link."),
            HttpStatus.CREATED
        )
    }

    @GetMapping("/confirm-registration")
    fun confirmRegistration(
        @RequestParam token: String,
        response: HttpServletResponse,
    ): ResponseEntity<User> {
        val verificationToken: VerificationToken =
            userService.getVerificationToken(token)
                ?: throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid token."
                );

        val user: User = verificationToken.user
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Token not matched with any user."
            );

        if ((verificationToken.isTokenExpired())) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Token expired."
            )
        }
        user.enabled = true;
        userRepository.save(user)

        this.verificationTokenService.deleteVerificationToken(user.id)

        val jwtToken =
            authService.authentication(AuthRequest(user.email, user.password))
        val cookie = this.tokenService.createCookie(jwtToken.accessToken)
        response.addCookie(cookie)

        return ResponseEntity(
            user,
            HttpStatus.OK
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody credentials: AuthRequest,
        session: HttpSession,
        response: HttpServletResponse,
    ): AuthResponse {
        val token = authService.authentication(credentials)
        val cookie = tokenService.createCookie(token.accessToken)
        response.addCookie(cookie).let { token }

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

private fun getBaseUrl(
    response: HttpServletResponse
): String {
    val origin = response.getHeader("Access-Control-Allow-Origin")
    return origin
}
