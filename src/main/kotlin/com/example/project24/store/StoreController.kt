package com.example.project24.store

import com.example.project24.auth.TokenService
import com.example.project24.config.ApiMessageResponse
import com.example.project24.config.CustomAuthenticationToken
import com.example.project24.user.User
import com.example.project24.user.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/store")
class StoreController {

    @Autowired
    lateinit var storeService: StoreService

    @Autowired
    lateinit var tokenService: TokenService

    @Autowired
    lateinit var userService: UserService

    @PostMapping("")
    fun createStore(@Valid @RequestBody store: Store): ResponseEntity<ApiMessageResponse> {

        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId.toInt()

        val user: Optional<User> = userService.getUserDetails(userId)
        if (user.isEmpty) {
            return ResponseEntity(
                ApiMessageResponse("User not found"),
                HttpStatus.BAD_REQUEST
            )
        }

        store.user = user.get()
        store.address = user.get().address

        this.storeService.createStore(store)

        return ResponseEntity(
            ApiMessageResponse("Store created successfully"),
            HttpStatus.CREATED
        )

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
    fun getStoreById(@PathVariable id: Long): ResponseEntity<StoreDTO> {
        val store = this.storeService.getStoreById(id)
        return if (store != null) {
            val storeDTO = mapToStoreDTO(store)
            ResponseEntity.ok(storeDTO)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}