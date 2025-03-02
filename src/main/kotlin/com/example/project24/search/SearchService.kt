package com.example.project24.search

import com.example.project24.store.Store
import com.example.project24.product.ProductDTO
import com.example.project24.product.ProductService
import com.example.project24.product.mapToProductDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

data class SearchResult(
    val products: List<ProductDTO>,
    val stores: List<Store>
)

@Service
class SearchService {
    @Autowired
    lateinit var productService: ProductService

    fun searchByFilter(categories: List<String>): SearchResult {

        val products = productService.searchByFilter(categories)

        val mappedProducts = products.map { product ->
            mapToProductDTO(product)
        }

        return SearchResult(products = mappedProducts, stores = mutableListOf())
    }
}