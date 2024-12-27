package com.example.project24.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

typealias ApplicationUser = User

class CustomUserDetails(
    private val userId: Long,
    private val email: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun getUserId(): Long = userId
}

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): CustomUserDetails =
        userRepository.findByEmail(username)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Not found!")

    private fun ApplicationUser.mapToUserDetails(): CustomUserDetails =
        CustomUserDetails(
            userId = this.id,
            email = this.email,
            password = this.password,
            authorities = this.role?.name?.let {
                listOf(
                    org.springframework.security.core.authority.SimpleGrantedAuthority(
                        "ROLE_$it"
                    )
                )
            }
                ?: emptyList()
        )
}
