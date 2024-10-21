package com.example.project24.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    @NotEmpty
    var userName: String,
    @NotEmpty
    var firstName: String,
    @NotEmpty
    var lastName: String,
    @NotEmpty
    @Email
    var email: String,
    @NotEmpty
    var password: String,
//    var address: Address
) {
    constructor() : this(0, "", "", "", "", "")
}
