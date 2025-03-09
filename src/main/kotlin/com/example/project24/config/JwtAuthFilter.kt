package com.example.project24.config

import com.example.project24.user.CustomUserDetailsService
import com.example.project24.auth.TokenService
import com.example.project24.user.CustomUserDetails
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

class CustomAuthenticationToken(
    val userId: Long,
    principal: Any,
    credentials: Any?,
    authorities: Collection<GrantedAuthority>
) : UsernamePasswordAuthenticationToken(principal, credentials, authorities)

@Component
class JwtAuthFilter(
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwtTokenFromCookie =
            request.cookies?.find { it.name == "jwt" }?.value


        if (jwtTokenFromCookie == null) {
            filterChain.doFilter(request, response)
            return
        }

        val tokenIsExpired = tokenService.isExpired(jwtTokenFromCookie)

        if (tokenIsExpired) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write("""{"message": "Token expired"}""")
            response.addHeader(
                "Set-Cookie",
                tokenService.createCookie("", invalidate = true).toString()
            )
            response.writer.flush()
            return
        }

        val email = jwtTokenFromCookie.let { tokenService.extractEmail(it) }

        if (email != null && SecurityContextHolder.getContext().authentication == null) {
            val foundUser = userDetailsService.loadUserByUsername(email)

            if (tokenService.isValid(jwtTokenFromCookie, foundUser)) {
                updateContext(foundUser, request)
            }

            filterChain.doFilter(request, response)
        }
    }

    private fun String?.doesNotContainBearerToken() =
        this == null || !this.startsWith("Bearer ")

    private fun String.extractTokenValue(): String? {
        val token = this.substringAfter("Bearer ")
        if (token == "undefined") return null
        return token
    }

    private fun updateContext(
        foundUser: CustomUserDetails,
        request: HttpServletRequest
    ) {
        val authToken = CustomAuthenticationToken(
            userId = foundUser.getUserId(),
            foundUser,
            null,
            foundUser.authorities
        )
        authToken.details =
            WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }

}
