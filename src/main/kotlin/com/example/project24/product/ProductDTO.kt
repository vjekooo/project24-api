package com.example.project24.product

import com.example.project24.store.Store
import java.math.BigDecimal

data class ProductDTO(
    val id: Int,
    val name: String,
    val description: String,
    val image: List<String>,
    val storeId: Long,
    val price: BigDecimal,
    val discount: Double?,
    val finalPrice: BigDecimal
)

fun mapToProductDTO(product: Product): ProductDTO {
    return ProductDTO(
        id = product.id.toInt(),
        name = product.name,
        description = product.description,
        image = product.image ?: emptyList(),
        storeId = product.store.id,
        price = product.price,
        discount = product.discount,
        finalPrice = product.calculateFinalPrice()
    )
}

fun mapToProduct(productDTO: ProductDTO): Product {
    return Product(
        id = productDTO.id.toLong(),
        name = productDTO.name,
        description = productDTO.description,
        image = productDTO.image,
        store = Store(),
        price = productDTO.price,
        discount = productDTO.discount,
    )
}
