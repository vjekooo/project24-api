package com.example.project24.store

data class StoreRequest(
    val name: String,
    val description: String,
    val image: List<String>,
    val categoryId: Long
)
