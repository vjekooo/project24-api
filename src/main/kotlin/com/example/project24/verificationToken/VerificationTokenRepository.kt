package com.example.project24.verificationToken

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VerificationTokenRepository :
    JpaRepository<VerificationToken, Int?> {
    fun findByToken(token: String?): VerificationToken?

    @Modifying
    @Query("delete from VerificationToken t where t.user = :userdId")
    fun deleteByUserId(userId: Long?)
}
