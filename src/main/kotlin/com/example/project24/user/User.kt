package com.example.project24.user

import com.example.project24.address.Address
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.util.Date

enum class Role {
    USER, ADMIN
}

@Entity
@Table(
    name = "users",
)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,
    @Column(nullable = false, unique = true)
    var email: String,
    var firstName: String,
    var lastName: String,
    @NotEmpty
    var password: String,
    var role: Role? = Role.USER,
    @Column(name = "enabled")
    var enabled: Boolean = false,
    @OneToOne(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @JsonManagedReference
    val address: Address? = null,
    @Column(nullable = false)
    val createdAt: Date = Date(),
    @Column(nullable = true)
    var updatedAt: Date? = Date()
) {
    constructor() : this(
        0,
        "",
        "",
        "",
        "",
        Role.USER,
        false,
    )
}
