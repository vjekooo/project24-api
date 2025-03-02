package com.example.project24.user

import com.example.project24.config.ApiMessageResponse
import com.example.project24.config.CustomAuthenticationToken
import com.example.project24.product.FavoriteProductService
import com.example.project24.product.ProductService
import com.example.project24.product.mapToProductDTO
import com.example.project24.store.FavoriteStoreService
import com.example.project24.store.StoreService
import com.example.project24.store.mapToStoreDTO
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var storeService: StoreService

    @Autowired
    lateinit var productService: ProductService

    @Autowired
    lateinit var favoriteStoreService: FavoriteStoreService

    @Autowired
    lateinit var favoriteProductService: FavoriteProductService

    @GetMapping("")
    fun getUserDetails(): ResponseEntity<UserDTO> {
        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId.toInt()
        val user = userService.getUserDetails(userId)

        val mappedUser = mapToUserDTO(user.get())

        val favoriteStoreIds = favoriteStoreService.getAllUserFavorites(userId
            .toLong())

        val favoriteStores = favoriteStoreIds.mapNotNull { favorite ->
            storeService.getStoreById(favorite.storeId)?.let { store ->
                mapToStoreDTO(store)
            }
        }

        val favoriteProductIds = favoriteProductService.getAllUserFavorites(userId.toLong())
        val favoriteProducts = favoriteProductIds.mapNotNull { favorite ->
            productService.getProductById(favorite.productId)?.let { product ->
                mapToProductDTO(product)
            }
        }

        mappedUser.favoriteStores = favoriteStores
        mappedUser.favoriteProducts = favoriteProducts

        return mappedUser.let { ResponseEntity.ok(it) }
    }

    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: User): ResponseEntity<ApiMessageResponse> {
        userService.saveUser(user)
        return ResponseEntity(
            ApiMessageResponse("User created successfully"),
            HttpStatus.CREATED
        )
    }

    @PutMapping("/update")
    fun updateUser(@Valid @RequestBody user: User): ResponseEntity<ApiMessageResponse> {
        val existingUser = userService.getUserByEmail(user.email);
        if (existingUser == null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User not found"
            )
        } else {
            user.id = existingUser.id;
            userService.saveUser(user);
            return ResponseEntity(
                ApiMessageResponse("User updated successfully"),
                HttpStatus.OK
            )
        }
    }
}
