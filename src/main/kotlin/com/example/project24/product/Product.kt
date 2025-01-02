package com.example.project24.product

import com.example.project24.store.Store
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.math.BigDecimal
import java.util.*

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @NotEmpty
    val name: String,
    @NotEmpty
    @Column(columnDefinition = "TEXT")
    val description: String,
    @ElementCollection
    @CollectionTable(
        name = "product_image",
        joinColumns = [JoinColumn(name = "product_id")],
    )
    val image: List<String>? = listOf(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @JsonIgnore
    var store: Store,
    @Column(nullable = false, precision = 10, scale = 2)
    val price: BigDecimal,
    @Column(nullable = true)
    val discount: Double? = null,
    @Column(nullable = false)
    val createdAt: Date = Date(),
    @Column(nullable = true)
    var updatedAt: Date? = Date()
) {
    constructor() : this(0, "", "", emptyList(), Store(), BigDecimal.ZERO) {

    }

    @Transient
    fun calculateFinalPrice(): BigDecimal {
        return discount?.let { price - (price * BigDecimal.valueOf(it)) } ?: price
    }
}
