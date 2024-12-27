package com.example.project24.store

import com.example.project24.auth.TokenService
import com.example.project24.user.CustomUserDetails
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store")
class StoreController {

    @Autowired
    lateinit var storeService: StoreService

    @Autowired
    lateinit var tokenService: TokenService

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    fun createStore(@Valid @RequestBody store: Store) {
        this.storeService.createStore(store)
    }

    @GetMapping("/user")
    fun getUserStores(@RequestHeader("Authorization") authHeader: String):
            ResponseEntity<List<Store>> {

        val token = authHeader.removePrefix("Bearer ").trim()
        val userId = this.tokenService.extractUserId(token)
        val id = userId ?: return ResponseEntity(
            HttpStatus
                .UNAUTHORIZED
        )
        val allStores = this.storeService.getUserStores(id)

        return ResponseEntity(
            allStores ?: emptyList(),
            if (allStores != null) HttpStatus.OK else HttpStatus.NOT_FOUND
        )
    }

    @GetMapping("")
    fun getAllStores() {
        this.storeService.getAllStores()
    }

    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: Long): ResponseEntity<Store> {
        val store = this.storeService.getStoreById(id)
        return if (store != null) {
            ResponseEntity.ok(store)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}