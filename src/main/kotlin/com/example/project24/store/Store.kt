package com.example.project24.store

import com.example.project24.address.Address
import com.example.project24.product.Product
import com.example.project24.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.util.*

enum class StoreMediaType {
    IMAGE,
    VIDEO
}

@Embeddable
data class Media(
    @NotEmpty
    val url: String,
    @NotEmpty
    val type: StoreMediaType
) {
    constructor() : this("", StoreMediaType.IMAGE)
}

@Entity
data class Store(
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
        name = "store_media",
        joinColumns = [JoinColumn(name = "store_id")],
    )
    @AttributeOverrides(
        AttributeOverride(name = "url", column = Column(name = "url")),
        AttributeOverride(name = "type", column = Column(name = "type"))
    )
    val media: List<Media>? = listOf(),
    @OneToOne(
        mappedBy = "store",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var address: Address? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null,
    @OneToMany(
        mappedBy = "store",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val product: List<Product>? = null,
    @Column(nullable = false)
    val createdAt: Date = Date(),
    @Column(nullable = true)
    var updatedAt: Date? = Date()
) {
    constructor() : this(
        0, "", ""
    ) {

    }
}
