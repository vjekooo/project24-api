package com.example.project24.product

import com.example.project24.media.MediaDTO
import com.example.project24.media.mapMediaDTOtoMedia
import com.example.project24.media.mapToMediaDTO
import com.example.project24.store.Store
import java.math.BigDecimal

data class ProductDTO(
    val id: Int,
    val name: String,
    val description: String,
    val media: List<MediaDTO>?,
    val storeId: Long,
    val price: BigDecimal,
    val discount: Double?,
    val finalPrice: BigDecimal?,
    val isFeatured: Boolean?
)

fun mapToProductDTO(product: Product): ProductDTO {
    return ProductDTO(
        id = product.id.toInt(),
        name = product.name,
        description = product.description,
        media = product.media?.map { mapToMediaDTO(it) } ?: emptyList(),
        storeId = product.store.id,
        price = product.price,
        discount = product.discount,
        finalPrice = product.calculateFinalPrice(),
        isFeatured = product.isFeatured
    )
}

fun mapToProduct(productDTO: ProductDTO, store: Store): Product {
    return Product(
        id = productDTO.id.toLong(),
        name = productDTO.name,
        description = productDTO.description,
        media = productDTO.media?.map { mapMediaDTOtoMedia(it) }
            ?.toMutableList(),
        store = store,
        price = productDTO.price,
        discount = productDTO.discount
    )
}
