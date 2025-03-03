package com.example.project24.search

import com.example.project24.product.ProductDTO
import com.example.project24.product.ProductService
import com.example.project24.store.StoreDTO
import com.example.project24.store.StoreService
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

    @Autowired
    lateinit var storeService: StoreService

    fun searchByFilter(category: String?, subCategory: String?): SearchResult {

        val products = subCategory?.let { productService.searchByFilter(it) }
            ?: mutableListOf()
        val stores = category?.let { storeService.searchByFilter(category) }
            ?: mutableListOf()

        val storeProducts = category?.let { productService.getProductsByStoreCategory(category) }
            ?: mutableListOf()
        val productStores = subCategory?.let { storeService
            .getStoresByProductCategory(subCategory) }
            ?: mutableListOf()

        return SearchResult(products = products + storeProducts, stores =
            stores + productStores)
    }
}