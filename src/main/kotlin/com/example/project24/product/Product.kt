package com.example.project24.product

import com.example.project24.store.Store
import com.example.project24.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "product_images",
        joinColumns = [JoinColumn(name = "product_id")]
    )
    @Column(name = "image_url")
    val images: List<String> = emptyList(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @JsonIgnore
    var store: Store? = null,
    @Column(nullable = false)
    val createdAt: Date = Date(),
    @Column(nullable = true)
    var updatedAt: Date? = Date()
) {
    constructor() : this(0, "", "", emptyList()) {

    }
}
