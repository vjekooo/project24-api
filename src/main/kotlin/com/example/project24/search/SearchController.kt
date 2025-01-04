package com.example.project24.search

import com.example.project24.product.Product
import com.example.project24.store.Store
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController() {

   @Autowired
   lateinit var searchService: SearchService

   @GetMapping("")
   fun searchAll(@RequestParam(name = "searchTerm") searchTerm: String): ResponseEntity<SearchResult> {
       val result = searchService.searchAll(searchTerm)
       return ResponseEntity.ok(result)
   }

   @GetMapping("/search/products")
   fun searchProducts(@RequestParam searchTerm: String): List<Product> {
       return searchService.searchProducts(searchTerm)
   }

   @GetMapping("/search/stores")
   fun searchStores(@RequestParam searchTerm: String): List<Store> {
       return searchService.searchStores(searchTerm)
   }
}