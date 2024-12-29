package com.example.project24.store

import com.example.project24.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
data class FavoriteStore(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Column(nullable = false)
    val storeId: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User,
) {
    constructor() : this(0, 0, User())
}
