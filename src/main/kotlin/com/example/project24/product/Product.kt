package com.example.project24.product

import com.example.project24.media.Media
import com.example.project24.store.Store
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.math.BigDecimal
import java.util.*

@Entity
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @NotEmpty
    val name: String,
    @NotEmpty
    @Column(columnDefinition = "TEXT")
    val description: String,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val media: MutableList<Media>? = mutableListOf(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @JsonIgnore
    var store: Store,
    @Column(nullable = false, precision = 10, scale = 2)
    val price: BigDecimal,
    @Column(nullable = true)
    val discount: Double? = null,
    @Column(nullable = false)
    var isFeatured: Boolean = false,
    @Column(nullable = false)
    val createdAt: Date = Date(),
    @Column(nullable = true)
    var updatedAt: Date? = Date()
) {
    constructor() : this(0, "", "", mutableListOf(), Store(), BigDecimal.ZERO) {

    }

    @Transient
    fun calculateFinalPrice(): BigDecimal {
        return discount?.let { price - (price * BigDecimal.valueOf(it)) } ?: price
    }
}
