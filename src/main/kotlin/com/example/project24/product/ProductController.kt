package com.example.project24.product

import com.example.project24.category.CategoryService
import com.example.project24.config.ApiMessageResponse
import com.example.project24.config.CustomAuthenticationToken
import com.example.project24.store.StoreService
import com.example.project24.user.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
class ProductController {

    @Autowired
    lateinit var productService: ProductService

    @Autowired
    lateinit var storeService: StoreService

    @Autowired
    lateinit var favoriteProductService: FavoriteProductService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var categoryService: CategoryService

    @PostMapping("")
    fun createProduct(@Valid @RequestBody product: ProductRequest):
            ResponseEntity<ApiMessageResponse> {

        val store = this.storeService.getStoreById(product.storeId)

        if (store == null) {
            return ResponseEntity.badRequest().body(
                ApiMessageResponse("Store not found")
            )
        } else {
            val mappedProduct = mapRequestToProduct(product, store, categoryService)
            mappedProduct.store = store

            this.productService.createProduct(mappedProduct)

            return ResponseEntity.ok(
                ApiMessageResponse(
                    "Product created " +
                            "successfully"
                )
            )
        }
    }

    @PutMapping("")
    fun updateProduct(@Valid @RequestBody product: ProductRequest):
            ResponseEntity<ApiMessageResponse> {
        val store = this.storeService.getStoreById(product.storeId)

        if (store == null) {
            return ResponseEntity.badRequest().body(
                ApiMessageResponse("Store not found")
            )
        } else {
            val mappedProduct = mapRequestToProduct(product, store, categoryService)
            mappedProduct.store = store

            this.productService.updateProduct(mappedProduct)

            return ResponseEntity.ok(
                ApiMessageResponse(
                    "Product created " +
                            "successfully"
                )
            )
        }
    }

    @GetMapping("/store/{storeId}")
    fun getAllStoreProducts(@PathVariable storeId: Long):
            ResponseEntity<List<ProductDTO>> {
        val products = this.productService.getProductsByStoreId(storeId)

        val productsDTO =
            products.map { product -> mapToProductDTO(product) }

        return ResponseEntity.ok(productsDTO)
    }

    @GetMapping("/{productId}")
    fun getProductBy(@PathVariable productId: Long):
            ResponseEntity<ProductDTO> {
        val product = this.productService.getProductById(productId)
        val mappedProduct = product?.let { mapToProductDTO(it) }

        return ResponseEntity.ok(mappedProduct)
    }

    @GetMapping("/{productId}/related")
    fun getRelatedStores(@PathVariable productId: Long):
            ResponseEntity<List<ProductDTO>> {

        val currentProduct = this.productService.getProductById(productId)
        val store = currentProduct?.store ?: return ResponseEntity.notFound().build()
        val relatedProducts = store.product?.filter { it.id != productId } ?: emptyList()
        val mappedProducts = relatedProducts.map { product -> mapToProductDTO(product) }

        return ResponseEntity.ok(mappedProducts)
    }

    @PostMapping("/toggle-favorite")
    fun toggleProductToFavorite(@Valid @RequestBody favoriteProduct: FavoriteProductDTO):
            ResponseEntity<ApiMessageResponse> {

        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId.toInt()

        val user = this.userService.getUserDetails(userId)

        val product = FavoriteProduct(0, favoriteProduct.productId, user.get())

        val favorite =
            this.favoriteProductService.getFavoriteByProductId(product.productId)

        if (favorite != null) {
            this.favoriteProductService.deleteFavoriteByProductId(product.productId)
            return ResponseEntity.ok(ApiMessageResponse("Product removed from favorites"))
        } else {
            this.favoriteProductService.saveFavorite(
                product
            )
            return ResponseEntity.ok(ApiMessageResponse("Product added to favorites"))
        }
    }

    @GetMapping("/favorite")
    fun getFavoriteProducts(): ResponseEntity<List<FavoriteProductDTO>> {
        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId
        val favorites = this.favoriteProductService.getAllUserFavorites(userId)
        return ResponseEntity.ok(favorites.map { favorite ->
            mapToFavoriteProductDTO(
                favorite
            )
        })
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(@PathVariable productId: Long):
            ResponseEntity<ApiMessageResponse> {
        this.productService.deleteProductById(productId)
        return ResponseEntity.ok(ApiMessageResponse("Product deleted successfully"))
    }
}