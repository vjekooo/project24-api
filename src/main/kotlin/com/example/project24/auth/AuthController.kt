package com.example.project24.auth

import com.example.project24.config.ApiMessageResponse
import com.example.project24.user.User
import com.example.project24.user.UserRepository
import com.example.project24.user.UserService
import com.example.project24.verificationToken.OnRegistrationCompleteEvent
import com.example.project24.verificationToken.VerificationToken
import com.example.project24.verificationToken.VerificationTokenService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpHeaders

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
    ): ResponseEntity<ApiMessageResponse> {
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
            ApiMessageResponse("User registered successfully. Check your email for verification link."),
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
                )

        val user: User = verificationToken.user

        if ((verificationToken.isTokenExpired())) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Token expired."
            )
        }
        user.enabled = true
        userRepository.save(user)

        this.verificationTokenService.deleteVerificationToken(user.id)

//        val jwtToken =
//            authService.authentication(AuthRequest(user.email, user.password))
//        val cookie = this.tokenService.createCookie(jwtToken.accessToken)
//        response.addCookie(cookie)

        return ResponseEntity(
            user,
            HttpStatus.OK
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody credentials: AuthRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiMessageResponse> {
        val token = authService.authentication(credentials)
        val cookie = tokenService.createCookie(token.accessToken)

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie
            .toString()).body(ApiMessageResponse("Login successful"))
    }

    @GetMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Void> {
        val cookie = tokenService.createCookie("", invalidate = true)
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build()
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
}

private fun getBaseUrl(
    response: HttpServletResponse
): String {
    val origin = response.getHeader("Access-Control-Allow-Origin")
    return origin
}
