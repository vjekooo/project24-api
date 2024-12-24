package com.example.project24.store

import com.example.project24.address.Address
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty

enum class MediaType {
    IMAGE,
    VIDEO
}

@Embeddable
data class Media(
    @NotEmpty
    val url: String,
    @NotEmpty
    val type: MediaType
) {
    constructor() : this("", MediaType.IMAGE)
}

@Entity
data class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @NotEmpty
    val name: String,
    @NotEmpty
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
    val media: List<Media> = listOf(),
    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "address_id")
    val address: Address
) {
    constructor() : this(0, "", "", listOf(), Address())
}
