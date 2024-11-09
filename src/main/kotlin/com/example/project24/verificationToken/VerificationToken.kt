package com.example.project24.verificationToken

import com.example.project24.user.User
import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
data class VerificationToken(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    val token: String? = null,

    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    val user: User? = null,

    val expiryDate: Date? = null,
) {
    constructor() : this(null, null, null, null)

    fun isTokenExpired(): Boolean {
        return expiryDate!!.before(Timestamp(System.currentTimeMillis()))
    }
}