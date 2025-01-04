package com.example.project24.search

import com.example.project24.store.Store
import com.example.project24.product.Product
import com.example.project24.product.ProductRepository
import com.example.project24.store.StoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

data class SearchResult(
    val products: List<Product>,
    val stores: List<Store>
)

@Service
class SearchService() {

    @Autowired
   lateinit var productRepository: ProductRepository

   @Autowired
   lateinit var storeRepository: StoreRepository

   fun searchAll(searchTerm: String): SearchResult {
       val products = productRepository.searchProducts(searchTerm)
       val stores = storeRepository.searchStores(searchTerm)
       return SearchResult(products, stores)
   }

   fun searchProducts(searchTerm: String): List<Product> {
       return productRepository.searchProducts(searchTerm)
   }

   fun searchStores(searchTerm: String): List<Store> {
       return storeRepository.searchStores(searchTerm)
   }
}