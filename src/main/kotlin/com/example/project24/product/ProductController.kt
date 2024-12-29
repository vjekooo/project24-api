package com.example.project24.product

import com.example.project24.config.ApiMessageResponse
import com.example.project24.store.StoreService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
class ProductController {

    @Autowired
    lateinit var productService: ProductService

    @Autowired
    lateinit var storeService: StoreService

    @PostMapping("")
    fun createProduct(@Valid @RequestBody product: ProductDTO):
            ResponseEntity<ApiMessageResponse> {

        val store = this.storeService.getStoreById(product.storeId)

        if (store == null) {
            return ResponseEntity.badRequest().body(
                ApiMessageResponse("Store not found")
            )
        } else {
            val mappedProduct = mapToProduct(product)
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

    @GetMapping("/{storeId}")
    fun getAllStoreProducts(@PathVariable storeId: Long):
            ResponseEntity<List<ProductDTO>> {
        val products = this.productService.getProductsByStoreId(storeId)

        val productsDTO =
            products.map { product -> mapToProductDTO(product) }

        return ResponseEntity.ok(productsDTO)
    }
}