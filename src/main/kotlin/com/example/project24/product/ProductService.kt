package com.example.project24.product

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService {

    @Autowired
    lateinit var repository: ProductRepository

    fun createProduct(product: Product): Product {
        return this.repository.save(product)
    }

    fun updateProduct(product: Product) {
        val existing = repository.findById(product.id)

        if (existing.isPresent) {
            repository.save(product)
        } else {
            throw IllegalArgumentException("Product with id ${product.id} does not exist")
        }

    }

    fun getProductsByStoreId(id: Long): List<Product> {
        return this.repository.findAllByStoreId(id)
    }

    fun getProductById(id: Long): Product? {
        return this.repository.findById(id).orElse(null)
    }

    fun getRelatedProducts(productId: Long, categoryIds: List<Long>):
            List<Product> {
        return this.repository.findRelatedProductsByCategories(productId, categoryIds)
    }

    fun deleteProductById(id: Long) {
        return this.repository.deleteById(id)
    }

    fun searchByFilter(category: String): List<ProductDTO> {
        val products =  this.repository.findByFilter(category)
        return products.map { product ->
            mapToProductDTO(product)
        }
    }

    fun getProductsByStoreCategory(category: String): List<ProductDTO> {
        val products = this.repository.findAllByStoreCategory(category)
        return products.map { product ->
            mapToProductDTO(product)
        }
    }

    fun getLatestProducts(): List<ProductDTO> {
        val products = this.repository.findAll().sortedByDescending { it.createdAt }
        return products.map { product ->
            mapToProductDTO(product)
        }
    }

}