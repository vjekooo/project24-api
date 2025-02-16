package com.example.project24.search

import com.example.project24.store.Store
import com.example.project24.product.Product
import com.example.project24.product.ProductService
import com.example.project24.store.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

data class SearchResult(
    val products: List<Product>,
    val stores: List<Store>
)

@Service
class SearchService {
    @Autowired
    lateinit var productService: ProductService

    @Autowired
    lateinit var storeService: StoreService

    fun searchByFilter(category: String?, name: String?): SearchResult {
        val products = productService.searchByFilter(category, name)
        val stores = storeService.searchByFilter(category, name)
        return SearchResult(products = products, stores = stores)
    }
}