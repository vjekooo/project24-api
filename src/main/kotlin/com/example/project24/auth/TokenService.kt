package com.example.project24.auth

import com.example.project24.config.CookieProperties
import com.example.project24.config.JwtProperties
import com.example.project24.user.CustomUserDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import org.springframework.http.ResponseCookie
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
    jwtProperties: JwtProperties,
    cookieProperties: CookieProperties
) {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray().copyOf(32)
    )
    private val accessTokenExpiration = jwtProperties.accessTokenExpiration

    private val cookieName = cookieProperties.name
    private val path = cookieProperties.path
    private val httpOnly = cookieProperties.httpOnly
    private val secure = cookieProperties.secure

    fun generate(
        userDetails: CustomUserDetails,
    ): String =
        Jwts.builder()
            .setSubject(userDetails.username)
            .claim("userId", userDetails.getUserId())
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + accessTokenExpiration))
            // TODO: Make it safer
            .signWith(secretKey)
            .compact()

    fun createCookie(
        token: String,
        invalidate: Boolean = false,
    ): ResponseCookie {

        val cookie = ResponseCookie.from("jwt")
            .value(token)
            .sameSite("Lax")
            .httpOnly(httpOnly)
            .secure(false)
            .maxAge(if (invalidate) 0 else 200 * 60 * 60 * 24)
            .path(path)
            .build()

        return cookie
    }

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)

        return userDetails.username == email && !isExpired(token)
    }

    fun extractEmail(token: String): String? =
        getAllClaims(token)
            .subject

    fun extractUserId(token: String): Long? {
        return try {
            val claims = getAllClaims(token)
            claims["userId"] as? Long
        } catch (e: Exception) {
            return null
        }
    }

    fun isExpired(token: String): Boolean {
        return try {
            getAllClaims(token).expiration.before(Date(System.currentTimeMillis()))
        } catch (e: ExpiredJwtException) {
            true
        } catch (e: Exception) {
            throw RuntimeException("Failed to parse the token", e)
        }
    }

    private fun getAllClaims(token: String): Claims = Jwts.parserBuilder()
        .setSigningKey(secretKey).build()
        .parseClaimsJws(token).body
}
