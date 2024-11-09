package com.example.project24.verificationToken

import com.example.project24.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*
import java.util.stream.Stream

@Repository
interface VerificationTokenRepository :
    JpaRepository<VerificationToken, Int?> {
    fun findByToken(token: String?): VerificationToken?

    fun findByUser(user: User?): VerificationToken?

    fun findAllByExpiryDateLessThan(now: Date?): Stream<VerificationToken?>?

    fun deleteByExpiryDateLessThan(now: Date?)

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    fun deleteAllExpiredSince(now: Date?)
}
