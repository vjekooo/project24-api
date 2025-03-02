package com.example.project24.user

import com.example.project24.address.Address
import com.example.project24.product.FavoriteProduct
import com.example.project24.store.FavoriteStore
import com.example.project24.store.Store
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
class User(
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
    val address: Address? = null,
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val store: List<Store>? = null,
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val favoriteProduct: List<FavoriteProduct>? = null,
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val favoriteStore: List<FavoriteStore>? = null,
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
