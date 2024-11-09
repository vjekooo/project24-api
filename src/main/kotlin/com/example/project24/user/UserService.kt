package com.example.project24.user

import com.example.project24.verificationToken.VerificationToken
import com.example.project24.verificationToken.VerificationTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    private val tokenRepository: VerificationTokenRepository? = null

    fun createVerificationTokenForUser(user: User?, token: String?) {
        val myToken = VerificationToken(null, token, user)
        tokenRepository?.save(myToken)
    }

    fun getVerificationToken(VerificationToken: String?): VerificationToken? {
        return tokenRepository!!.findByToken(VerificationToken)
    }

    fun saveRegisteredUser(user: User) {
        userRepository.save(user)
    }

}
