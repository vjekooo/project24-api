package com.example.project24.product

import com.example.project24.category.CategoryService
import com.example.project24.config.ApiMessageResponse
import com.example.project24.config.CustomAuthenticationToken
import com.example.project24.media.Media
import com.example.project24.media.MediaService
import com.example.project24.storage.S3Service
import com.example.project24.store.StoreService
import com.example.project24.user.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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

    @Autowired
    lateinit var s3Service: S3Service

    @Autowired
    lateinit var mediaService: MediaService

    @Value("\${spring.cloud.aws.region.static}")
    lateinit var awsRegion: String

    @Value("\${spring.cloud.aws.s3.bucket}")
    lateinit var s3Bucket: String

    @PostMapping("")
    fun createProduct(@Valid @ModelAttribute productRequest: ProductRequest):
    ResponseEntity<ApiMessageResponse> {

        val store = this.storeService.getStoreById(productRequest.storeId.toLong())

        if (store == null) {
            return ResponseEntity.badRequest().body(
                ApiMessageResponse("Store not found")
            )
        } else {
            val mappedProduct = mapRequestToProduct(productRequest)

            val categories = productRequest.category.map { categoryId ->
                categoryService.getCategoryById(categoryId.toLong())
                    ?: throw IllegalArgumentException("Category not found for ID: $categoryId")
            }.toMutableList()

            mappedProduct.store = store
            mappedProduct.category = categories

            val savedMedia = productRequest.newImages?.map { image ->
                val fileName = s3Service.uploadFile(image)
                Media(
                    0,
                    imageUrl = fileName,
                    product = mappedProduct
                )
            }

            mappedProduct.media = savedMedia?.toMutableList()

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
    fun updateProduct(@Valid @ModelAttribute productRequest: ProductRequest):
            ResponseEntity<ApiMessageResponse> {

        val productId = productRequest.id?.toLong()
            ?: return ResponseEntity.badRequest().body(
                ApiMessageResponse("Product id is required")
            )

        val store = this.storeService.getStoreById(productRequest.storeId.toLong())
            ?: return ResponseEntity.badRequest().body(
                ApiMessageResponse("Store not found")
            )

        val existingMedia = mediaService.getAllFilesByProductId(productId)
        val mappedProduct = mapRequestToProduct(productRequest)

        val categories = productRequest.category.map { categoryId ->
            categoryService.getCategoryById(categoryId.toLong())
                ?: throw IllegalArgumentException("Category not found for ID: $categoryId")
        }.toMutableList()

        mappedProduct.store = store
        mappedProduct.category = categories

        val mediaUrl = "https://$s3Bucket.s3.$awsRegion.amazonaws.com"

        val imagesToDelete = existingMedia
            .filter {
                "${mediaUrl}/${it.imageUrl}" !in (productRequest
                    .existingImages ?: emptyList())
        }

        if (imagesToDelete.isNotEmpty()) {
            imagesToDelete.forEach { file ->
                run {
                    s3Service.deleteFile(file.imageUrl)
                    mediaService.repository.delete(file)
                }
            }
        }

        val newMedia = productRequest.newImages?.map { image ->
            val fileName = s3Service.uploadFile(image)
            Media(
                0,
                imageUrl = fileName,
                product = mappedProduct
            )
        }.orEmpty()

        val oldMedia = existingMedia
            .filter {
                "${mediaUrl}/${it.imageUrl}" in (productRequest
                    .existingImages ?: emptyList())
            }

        mappedProduct.media = (newMedia + oldMedia).toMutableList()

        this.productService.updateProduct(mappedProduct)

        return ResponseEntity.ok(
            ApiMessageResponse(
                "Product updated " +
                        "successfully"
            )
        )
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
            ?: return ResponseEntity.notFound().build()

        val categoryIds = currentProduct.category.map { category -> category.id }
            .toMutableList()

        val relatedProducts =
            currentProduct.id.let {
                this.productService.getRelatedProducts(it, categoryIds)
            }

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
        try {
            val product = this.productService.getProductById(productId)
            this.productService.deleteProductById(productId)
            product?.media?.forEach { media ->
                println("Deleting file: ${media.imageUrl}")
                s3Service.deleteFile(media.imageUrl)
            }
        } catch (e: Exception) {
            return ResponseEntity.status(400).body(
                ApiMessageResponse("Product delete failed")
            )
        }
        return ResponseEntity.ok(ApiMessageResponse("Product deleted successfully"))
    }

    @GetMapping("/latest")
    fun getLatestProducts(): ResponseEntity<List<ProductDTO>> {
        val products = this.productService.getLatestProducts()
        return ResponseEntity.ok(products)
    }
}