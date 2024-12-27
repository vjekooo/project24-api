package com.example.project24.auth

import com.example.project24.config.JwtProperties
import com.example.project24.user.CustomUserDetails
import com.example.project24.user.CustomUserDetailsService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    fun authentication(authenticationRequest: AuthRequest): AuthResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.email,
                authenticationRequest.password
            )
        )

        val user =
            userDetailsService.loadUserByUsername(authenticationRequest.email)

        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(user)

        refreshTokenRepository.save(refreshToken, user)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun refreshAccessToken(refreshToken: String): String? {
        val extractedEmail = tokenService.extractEmail(refreshToken)

        return extractedEmail?.let { email ->
            val currentUserDetails =
                userDetailsService.loadUserByUsername(email)
            val refreshTokenUserDetails =
                refreshTokenRepository.findUserDetailsByToken(refreshToken)

            if (!tokenService.isExpired(refreshToken) && refreshTokenUserDetails?.username == currentUserDetails.username)
                createAccessToken(currentUserDetails)
            else
                null
        }
    }

    private fun createAccessToken(user: CustomUserDetails) =
        tokenService.generate(
            userDetails = user,
        )

    private fun createRefreshToken(user: CustomUserDetails) =
        tokenService.generate(
            userDetails = user,
        )
}
