package com.example.project24.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("cookie")
data class CookieProperties(
    val name: String,
    val path: String,
    val secure: Boolean,
    val httpOnly: Boolean,
)
