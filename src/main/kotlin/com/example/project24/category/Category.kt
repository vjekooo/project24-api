package com.example.project24.category

import com.example.project24.product.Product
import com.example.project24.store.Store
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.*

@Entity
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    var parent: Category? = null,

    @ManyToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    var store: MutableSet<Store> = mutableSetOf(),

    @ManyToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    var product: MutableSet<Product> = mutableSetOf()

) {
    constructor() : this(0, "",) {

    }
}