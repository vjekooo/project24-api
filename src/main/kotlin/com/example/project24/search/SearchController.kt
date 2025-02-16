package com.example.project24.search

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
   fun searchByFilter(
       @RequestParam(required = false) category: String?,
       @RequestParam(required = false) name: String?
   ): ResponseEntity<SearchResult> {
       return ResponseEntity.ok(searchService.searchByFilter(category, name))
   }
}