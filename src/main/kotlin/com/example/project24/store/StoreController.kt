package com.example.project24.store

import com.example.project24.auth.TokenService
import com.example.project24.category.CategoryService
import com.example.project24.config.ApiMessageResponse
import com.example.project24.config.CustomAuthenticationToken
import com.example.project24.media.Media
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

    @Autowired
    lateinit var favoriteStoreService: FavoriteStoreService

    @Autowired
    lateinit var categoryService: CategoryService

    @PostMapping("")
    fun createStore(@Valid @RequestBody store: StoreRequest):
            ResponseEntity<ApiMessageResponse> {

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

        val address = user.get().address ?: return ResponseEntity(
            ApiMessageResponse("User address is not set"),
            HttpStatus.BAD_REQUEST
        )

        val newStore = Store(
            0,
            store.name,
            store.description,
            store.image.map { Media(0, it) }.toMutableList(),
            address,
            user.get()
        )
       
        val category = categoryService.getCategoryById(store.categoryId)
            ?: return ResponseEntity(
                ApiMessageResponse("Category not found"),
                HttpStatus.BAD_REQUEST
            )

        newStore.category = mutableSetOf(category)

        this.storeService.createStore(newStore)

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

    @GetMapping("/all")
    fun getAllStores(): ResponseEntity<List<Store>> {
        val stores = this.storeService.getAllStores()
        return ResponseEntity.ok(stores)
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

    @PostMapping("/toggle-favorite")
    fun toggleStoreFavorite(
        @Valid @RequestBody favoriteStore:
        FavoriteStoreDTO
    ):
            ResponseEntity<ApiMessageResponse> {

        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId.toInt()

        val user = this.userService.getUserDetails(userId)

        val store = FavoriteStore(0, favoriteStore.storeId, user.get())

        val favorite =
            this.favoriteStoreService.getFavoriteByProductId(store.storeId)

        if (favorite != null) {
            this.favoriteStoreService.deleteFavoriteByStoreId(store.storeId)
            return ResponseEntity.ok(ApiMessageResponse("Product removed from favorites"))
        } else {
            this.favoriteStoreService.saveFavorite(
                store
            )
            return ResponseEntity.ok(ApiMessageResponse("Product added to favorites"))
        }
    }

    @GetMapping("/favorite")
    fun getFavoriteProducts(): ResponseEntity<List<FavoriteStoreDTO>> {
        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId
        val favorites = this.favoriteStoreService.getAllUserFavorites(userId)
        return ResponseEntity.ok(favorites.map { favorite ->
            mapToFavoriteStoreDTO(
                favorite
            )
        })
    }

    @GetMapping("/{id}/related")
    fun getRelatedStores(@PathVariable id: Long): ResponseEntity<List<StoreDTO>> {
        val stores = this.storeService.getAllStores().filter { it.id != id }
        return ResponseEntity.ok(stores.map { store -> mapToStoreDTO(store)
        })
    }
}