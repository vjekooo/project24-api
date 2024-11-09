package com.example.project24.verificationToken

import com.example.project24.user.User
import org.springframework.context.ApplicationEvent
import java.util.*

class OnRegistrationCompleteEvent(
    private val user: User,
    private val locale: Locale,
    private val appUrl: String
) : ApplicationEvent(user) {
    fun getUser(): User {
        return user
    }

    fun getAppUrl(): String {
        return appUrl
    }

    fun getLocale(): Locale {
        return locale
    }
}
