package com.example.project24.address

import com.example.project24.store.Store
import com.example.project24.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.util.*

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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @JsonIgnore
    var store: Store? = null,
    @Column(nullable = false)
    val createdAt: Date = Date(),
    @Column(nullable = true)
    var updatedAt: Date? = Date()
) {
    constructor() : this(0, "", "", "", "")
}
