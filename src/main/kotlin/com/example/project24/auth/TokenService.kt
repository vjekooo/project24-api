package com.example.project24.auth

import com.example.project24.config.CookieProperties
import com.example.project24.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
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
        userDetails: UserDetails,
    ): String =
        Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + accessTokenExpiration))
            // TODO: Make it safer
            .signWith(secretKey)
            .compact()

    fun createCookie(
        token: String
    ): String =
        ResponseCookie.from(cookieName, token)
            .path(path)
            .httpOnly(httpOnly)
            .maxAge(accessTokenExpiration)
            .sameSite("None")
            .domain("localhost")
            .build()
            .toString()

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)

        return userDetails.username == email && !isExpired(token)
    }

    fun extractEmail(token: String): String? =
        getAllClaims(token)
            .subject

    fun extractUserId(token: String): Long =
        getAllClaims(token).subject.toLong()

    fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    private fun getAllClaims(token: String): Claims = Jwts.parserBuilder()
        .setSigningKey(secretKey).build()
        .parseClaimsJws(token).body
}
