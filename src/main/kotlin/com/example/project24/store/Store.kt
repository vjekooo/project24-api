package com.example.project24.store

import com.example.project24.address.Address
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty

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
    val media: List<String> = listOf(),
    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "address_id")
    val address: Address
) {
    constructor() : this(0, "", "", listOf(), Address())
}
