package com.example.project24.media

import com.example.project24.product.Product
import com.example.project24.store.Store
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty

@Entity
data class Media(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @NotEmpty
    val imageUrl: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    @JsonIgnore
    var product: Product? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = true)
    @JsonIgnore
    var store: Store? = null
) {
    constructor() : this(0, "")
}
