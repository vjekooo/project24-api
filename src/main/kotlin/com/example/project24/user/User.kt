package com.example.project24.user

import com.example.project24.address.Address
import com.example.project24.store.Store
import jakarta.persistence.*
import jakarta.persistence.CascadeType.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

enum class Role {
    USER, ADMIN
}

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"])]
)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,
    @NotEmpty
    @Email
    var email: String,
    @NotEmpty
    var userName: String,
    @NotEmpty
    var firstName: String,
    @NotEmpty
    var lastName: String,
    @NotEmpty
    var password: String,
    val role: Role = Role.USER,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "address_id")
    val address: Address? = null,
    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "store_id")
    val store: Store? = null,
) {
    constructor() : this(0, "", "", "", "", "", Role.USER, Address())
}
