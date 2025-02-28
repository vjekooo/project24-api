package com.example.project24.store

import com.example.project24.user.User
import org.springframework.web.multipart.MultipartFile

data class StoreRequest(
    val id: String?,
    val name: String,
    val description: String,
    val existingImages: List<String>?,
    val newImages: List<MultipartFile>?,
    val category: List<String>
)

fun mapRequestToStore(
    storeRequest: StoreRequest,
): Store {
    val store = Store(
        storeRequest.id?.toLong() ?: 0,
        name = storeRequest.name,
        description = storeRequest.description,
        media = mutableListOf(),
        category = mutableListOf(),
        user = User(),
        product = mutableListOf()
    )
    return store
}
