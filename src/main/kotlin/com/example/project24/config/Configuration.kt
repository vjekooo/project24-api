package com.example.project24.config

import com.example.project24.user.CustomUserDetailsService
import com.example.project24.user.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableConfigurationProperties(JwtProperties::class, CookieProperties::class)
class Configuration {

    @Value("\${cors.originPatterns:default}")
    private val corsOriginPatterns: String = ""

    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService =
        CustomUserDetailsService(userRepository)

    @Bean
    fun authenticationProvider(userRepository: UserRepository): AuthenticationProvider =
        DaoAuthenticationProvider()
            .also {
                it.setUserDetailsService(userDetailsService(userRepository))
                it.setPasswordEncoder(encoder())
            }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(corsOriginPatterns)
        configuration.allowedMethods =
            listOf("GET", "POST", "PUT", "DELETE", "PATCH")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        val source: UrlBasedCorsConfigurationSource =
            UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
