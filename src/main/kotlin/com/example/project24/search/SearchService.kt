package com.example.project24.search

import com.example.project24.product.ProductDTO
import com.example.project24.product.ProductService
import com.example.project24.product.mapToProductDTO
import com.example.project24.store.StoreDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

data class SearchResult(
    val products: List<ProductDTO>,
    val stores: List<StoreDTO>
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