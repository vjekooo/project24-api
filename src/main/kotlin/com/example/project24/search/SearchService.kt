package com.example.project24.search

import com.example.project24.store.Store
import com.example.project24.product.Product
import com.example.project24.product.ProductDTO
import com.example.project24.product.ProductService
import com.example.project24.product.mapToProductDTO
import com.example.project24.store.StoreService
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

    @Autowired
    lateinit var storeService: StoreService

    fun searchByFilter(category: String?, subCategory: String?): SearchResult {
        if (category != null) {
            val products = productService.searchByFilter(category)

            val mappedProducts = products.map { product ->
                mapToProductDTO(product)
            }

            return SearchResult(products = mappedProducts, stores = mutableListOf())
        }
        if (subCategory != null) {
            val products = productService.searchByFilter(subCategory)

            val mappedProducts = products.map { product ->
                mapToProductDTO(product)
            }
            return SearchResult(products = mappedProducts, stores = mutableListOf())
        }
        return SearchResult(products = mutableListOf(), stores = mutableListOf())
    }
}