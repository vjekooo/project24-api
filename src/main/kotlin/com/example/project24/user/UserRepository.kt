package com.example.project24.user

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int?> {
    override fun findAll(): Iterable<User>
    fun findByUserName(userName: String): User?
    fun findByEmail(email: String): User?
}
