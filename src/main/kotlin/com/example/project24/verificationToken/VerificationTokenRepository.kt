package com.example.project24.verificationToken

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VerificationTokenRepository :
    JpaRepository<VerificationToken, Int?> {
    fun findByToken(token: String?): VerificationToken?

    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM verification_token t WHERE t.user_id = :userId",
        nativeQuery = true
    )
    fun deleteByUserId(userId: Long?)
}
