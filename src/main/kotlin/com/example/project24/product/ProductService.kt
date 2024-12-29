package com.example.project24.product

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService {

    @Autowired
    lateinit var productRepository: ProductRepository

    fun createProduct(product: Product): Product {
        return this.productRepository.save(product)
    }

    fun getProductsByStoreId(id: Long): List<Product> {
        return this.productRepository.findAllByStoreId(id)
    }

    fun deleteProductById(id: Int) {
        return this.productRepository.deleteById(id)
    }
}