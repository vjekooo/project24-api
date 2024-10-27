package com.example.project24.address

import com.example.project24.user.User
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty

@Entity
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    @NotEmpty
    val street: String,
    @NotEmpty
    val houseNumber: String,
    @NotEmpty
    val city: String,
    @NotEmpty
    val postalCode: String,
) {
    constructor() : this(0, "", "", "", "")
}
