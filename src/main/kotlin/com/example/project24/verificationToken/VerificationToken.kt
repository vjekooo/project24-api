package com.example.project24.verificationToken

import com.example.project24.user.User
import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
data class VerificationToken(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    val token: String,

    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    val user: User,

    val expiryDate: Date = Date(System.currentTimeMillis() + 3600000),
) {
    constructor() : this(0, "", User()) {

    }

    fun isTokenExpired(): Boolean {
        return expiryDate.before(Timestamp(System.currentTimeMillis()))
    }
}