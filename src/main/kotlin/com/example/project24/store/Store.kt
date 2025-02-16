package com.example.project24.store

import com.example.project24.address.Address
import com.example.project24.category.Category
import com.example.project24.media.Media
import com.example.project24.product.Product
import com.example.project24.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.util.*

@Entity
class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @NotEmpty
    val name: String,

    @NotEmpty
    @Column(columnDefinition = "TEXT")
    val description: String,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    val media: MutableList<Media>? = mutableListOf(),

    @OneToOne(
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @JoinColumn(name = "address_id")
    var address: Address? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User,

    @OneToMany(
        mappedBy = "store",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val product: List<Product>? = null,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "store_category",
        joinColumns = [JoinColumn(name = "store_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var category: MutableSet<Category> = mutableSetOf(),

    @Column(nullable = false)
    val createdAt: Date = Date(),

    @Column(nullable = true)
    var updatedAt: Date? = Date()
) {

    @JsonProperty("userId")
    fun getUserId(): Long {
        return user.id
    }

    constructor() : this(0, "", "", mutableListOf(), null, User(), null) {

    }
}
