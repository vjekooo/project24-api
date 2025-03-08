package com.example.project24.store

import com.example.project24.category.CategoryService
import com.example.project24.config.ApiMessageResponse
import com.example.project24.config.CustomAuthenticationToken
import com.example.project24.media.Media
import com.example.project24.media.MediaService
import com.example.project24.storage.S3Service
import com.example.project24.user.User
import com.example.project24.user.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
    lateinit var userService: UserService

    @Autowired
    lateinit var favoriteStoreService: FavoriteStoreService

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
    fun createStore(@Valid @ModelAttribute storeRequest: StoreRequest):
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

        val categories = storeRequest.category.map { categoryId ->
            categoryService.getCategoryById(categoryId.toLong())
                ?: throw IllegalArgumentException("Category not found for ID: $categoryId")
        }.toMutableList()

        val mappedStore = mapRequestToStore(storeRequest)

        mappedStore.user = user.get()
        mappedStore.category = categories

        val savedMedia = storeRequest.newImages?.map { image ->
            val fileName = s3Service.uploadFile(image)
            Media(
                0,
                imageUrl = fileName,
                store = mappedStore
            )
        }

        mappedStore.media = savedMedia?.toMutableList()

        try {
            this.storeService.createStore(mappedStore)
        } catch (e: Exception) {
            mappedStore.media?.map { media -> {
                s3Service.deleteFile(media.imageUrl)
            }}
            return ResponseEntity(
                ApiMessageResponse("Store create failed"),
                HttpStatus.BAD_REQUEST
            )
        }

        return ResponseEntity(
            ApiMessageResponse("Store created successfully"),
            HttpStatus.CREATED
        )
    }

    @PutMapping("")
    fun updateStore(@Valid @ModelAttribute storeRequest: StoreRequest):
            ResponseEntity<ApiMessageResponse> {

        val storeId = storeRequest.id?.toLong() ?: return ResponseEntity(
            ApiMessageResponse("Store id is required"),
            HttpStatus.BAD_REQUEST
        )

        val existingStore = storeService.getStoreById(storeId) ?: return ResponseEntity(
            ApiMessageResponse("Store not found"),
            HttpStatus.BAD_REQUEST
        )

        val categories = storeRequest.category.map { categoryId ->
            categoryService.getCategoryById(categoryId.toLong())
                ?: throw IllegalArgumentException("Category not found for ID: $categoryId")
        }.toMutableList()

        val mappedStore = mapRequestToStore(storeRequest)

        mappedStore.user = existingStore.user
        mappedStore.address = existingStore.address
        mappedStore.category = categories
        mappedStore.product = existingStore.product

        val existingMedia = mediaService.getAllFilesByStoreId(storeId)

        val mediaUrl = "https://$s3Bucket.s3.$awsRegion.amazonaws.com"

        val imagesToDelete = existingMedia
            .filter {
                "${mediaUrl}/${it.imageUrl}" !in (storeRequest
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

        val newMedia = storeRequest.newImages?.map { image ->
            val fileName = s3Service.uploadFile(image)
            Media(
                0,
                imageUrl = fileName,
                store = mappedStore
            )
        }.orEmpty()

        val oldMedia = existingMedia
            .filter {
                "${mediaUrl}/${it.imageUrl}" in (storeRequest
                    .existingImages ?: emptyList())
            }

        mappedStore.media = (newMedia + oldMedia).toMutableList()

        try {
            this.storeService.updateStore(mappedStore)
        } catch (e: Exception) {
            mappedStore.media?.map { media -> {
                s3Service.deleteFile(media.imageUrl)
            }}
            return ResponseEntity(
                ApiMessageResponse("Store update failed"),
                HttpStatus.BAD_REQUEST
            )
        }

        return ResponseEntity.ok(
            ApiMessageResponse(
                "Store updated " +
                        "successfully"
            )
        )

    }

    @GetMapping("/user")
    fun getUserStores():
            ResponseEntity<List<Store>> {

        val authentication = SecurityContextHolder.getContext()
            .authentication as CustomAuthenticationToken
        val userId = authentication.userId

        val allStores = this.storeService.getUserStores(userId)

        return ResponseEntity(
            allStores ?: emptyList(),
            if (allStores != null) HttpStatus.OK else HttpStatus.NOT_FOUND
        )
    }

    @GetMapping("/all")
    fun getAllStores(): ResponseEntity<List<StoreDTO>> {
        val stores = this.storeService.getAllStores()

        val storesDTO = stores.map { store -> mapToStoreDTO(store) }
        return ResponseEntity.ok(storesDTO)
    }

    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: Long): ResponseEntity<StoreDTO> {
        val store = this.storeService.getStoreById(id) ?: return ResponseEntity.notFound().build()
        val storeDTO = mapToStoreDTO(store)
        this.storeService.incrementViewCount(id)
        return ResponseEntity.ok(storeDTO)
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

    @DeleteMapping("/{id}")
    fun deleteStore(@PathVariable id: Long): ResponseEntity<ApiMessageResponse> {
        try {
            val store = this.storeService.getStoreById(id) ?: return ResponseEntity.notFound().build()
            this.storeService.deleteStoreById(store.id)
            store.media?.forEach { media ->
                s3Service.deleteFile(media.imageUrl)
            }
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiMessageResponse("Store delete failed")
            )
        }
        return ResponseEntity.ok(ApiMessageResponse("Store deleted successfully"))
    }
}