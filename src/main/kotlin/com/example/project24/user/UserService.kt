package com.example.project24.user

import com.example.project24.verificationToken.VerificationToken
import com.example.project24.verificationToken.VerificationTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var verificationTokenService: VerificationTokenService

    fun createVerificationTokenForUser(token: VerificationToken) {
        verificationTokenService.save(token)
    }

    fun getVerificationToken(verificationToken: String?): VerificationToken? {
        if (verificationToken == null) {
            return null
        }
        return verificationTokenService.findByToken(verificationToken)
    }

    fun saveRegisteredUser(user: User) {
        userRepository.save(user)
    }

}
