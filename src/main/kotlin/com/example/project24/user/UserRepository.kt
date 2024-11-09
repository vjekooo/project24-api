package com.example.project24.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int?> {
    override fun findAll(): MutableList<User>
    fun findByEmail(email: String): User?
}
