package com.example.project24.user

import com.example.project24.address.Address
import com.example.project24.store.Store
import jakarta.persistence.*
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
    var firstName: String,
    var lastName: String,
    @NotEmpty
    var password: String,
    var role: Role? = Role.USER,
    @Column(name = "enabled")
    var enabled: Boolean = false,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "address_id")
    val address: Address? = null,
    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "store_id")
    val store: Store? = null,
) {
    constructor() : this(0, "", "", "", "", Role.USER, false, Address())
}
