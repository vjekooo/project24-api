package com.example.project24.product

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/product")
class ProductController {

    @Autowired
    lateinit var productService: ProductService

    @PostMapping("")
    fun createProduct(@Valid @RequestBody product: Product) {
        this.productService.createProduct(product)
    }
}