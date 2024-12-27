package com.example.project24.verificationToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VerificationTokenService {
    @Autowired
    lateinit var tokenRepository: VerificationTokenRepository

    fun deleteVerificationToken(userId: Long) {
        this.tokenRepository.deleteByUserId(userId)
    }
}