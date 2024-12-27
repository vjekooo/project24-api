package com.example.project24.user

import com.example.project24.verificationToken.VerificationToken
import com.example.project24.verificationToken.VerificationTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var verificationTokenService: VerificationTokenService

    fun saveUser(user: User) {
        userRepository.save(user)
    }

    fun getUserDetails(id: Int): Optional<User> {
        return userRepository.findById(id)
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun createVerificationTokenForUser(token: VerificationToken) {
        verificationTokenService.save(token)
    }

    fun getVerificationToken(verificationToken: String?): VerificationToken? {
        if (verificationToken == null) {
            return null
        }
        return verificationTokenService.findByToken(verificationToken)
    }
}
