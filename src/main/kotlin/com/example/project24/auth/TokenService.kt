package com.example.project24.auth

import com.example.project24.config.CookieProperties
import com.example.project24.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import jakarta.servlet.http.Cookie
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

    private val maxTokenAge = accessTokenExpiration * 10

    fun generate(
        userDetails: UserDetails,
    ): String =
        Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + maxTokenAge))
            // TODO: Make it safer
            .signWith(secretKey)
            .compact()

    fun setCookie(response: HttpServletResponse, token: String) {
        val cookie = Cookie(cookieName, token)
        cookie.isHttpOnly = httpOnly
        cookie.secure = secure
        cookie.path = path
        cookie.maxAge = maxTokenAge
        response.addCookie(cookie)
    }

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)

        return userDetails.username == email && !isExpired(token)
    }

    fun extractEmail(token: String): String? =
        getAllClaims(token)
            .subject

    fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    private fun getAllClaims(token: String): Claims = Jwts.parserBuilder()
        .setSigningKey(secretKey).build()
        .parseClaimsJws(token).body
}
