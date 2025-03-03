//package com.example.project24.search
//
//import com.example.project24.address.Address
//import com.example.project24.category.CategoryDTO
//import com.example.project24.media.MediaDTO
//import com.example.project24.product.ProductDTO
//import com.example.project24.store.StoreDTO
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//import org.mockito.Mockito.`when`
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.http.MediaType
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.get
//import java.math.BigDecimal
//
//@SpringBootTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.NONE)
//@AutoConfigureMockMvc
//class SearchControllerTest {
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @MockBean
//    private lateinit var searchService: SearchService
//
//    @Test
//    fun `should return search results when category and subcategory are provided`() {
//        val mockResult = SearchResult(
//            products = listOf(
//                ProductDTO(
//                    1,
//                    name = "Product",
//                    description = "Test",
//                    media = mutableListOf(MediaDTO(1, imageUrl = "")),
//                    price = BigDecimal(12),
//                    discount = 20.00,
//                    finalPrice = BigDecimal(8),
//                    storeId = 12,
//                    isFeatured = false,
//                    categories = mutableListOf(CategoryDTO(1, name = "Test"))
//                ),
//            ),
//            stores = listOf(StoreDTO(
//                1,
//                name = "Store",
//                description = "Test",
//                media = mutableListOf(MediaDTO(1, imageUrl = "")),
//                address = Address(),
//                userId = 1,
//                products = mutableListOf(),
//                categories = mutableListOf()
//            ))
//        )
//        val categories = listOf("electronics", "laptops")
//
//        `when`(searchService.searchByFilter(categories)).thenReturn(mockResult)
//
//        val response = mockMvc.get("/api/search") {
//            param("category", "electronics")
//            param("subCategory", "laptops")
//            accept(MediaType.APPLICATION_JSON)
//        }.andExpect {
//            status { isOk() }
//            content { contentType(MediaType.APPLICATION_JSON) }
//        }.andReturn()
//
//        val responseBody = response.response.contentAsString
//        assertEquals(
//            """{"products":[{"name":"Product"}],"stores":[{"name":"Store"}]}""",
//            responseBody
//        )
//    }
//
//    @Test
//    fun `should return search results when only category is provided`() {
//        val mockResult = SearchResult(
//            products = listOf(
//                ProductDTO(
//                    1,
//                    name = "Product",
//                    description = "Test",
//                    media = mutableListOf(MediaDTO(1, imageUrl = "")),
//                    price = BigDecimal(12),
//                    discount = 20.00,
//                    finalPrice = BigDecimal(8),
//                    storeId = 12,
//                    isFeatured = false,
//                    categories = mutableListOf(CategoryDTO(1, name = "Test"))
//                ),
//            ),
//            stores = listOf(StoreDTO(
//                1,
//                name = "Store",
//                description = "Test",
//                media = mutableListOf(MediaDTO(1, imageUrl = "")),
//                address = Address(),
//                userId = 1,
//                products = mutableListOf(),
//                categories = mutableListOf()
//            ))
//        )
//        val categories = listOf("clothing")
//
//        `when`(searchService.searchByFilter(categories)).thenReturn(mockResult)
//
//        val response = mockMvc.get("/api/search") {
//            param("category", "clothing")
//            accept(MediaType.APPLICATION_JSON)
//        }.andExpect {
//            status { isOk() }
//            content { contentType(MediaType.APPLICATION_JSON) }
//        }.andReturn()
//
//        val responseBody = response.response.contentAsString
//        assertEquals(
//            """{"products":[{"name":"Product"}],"stores":[{"name":"Store"}]}""",
//            responseBody
//        )
//    }
//
//    @Test
//    fun `should return empty results when no parameters are provided`() {
//        val mockResult =
//            SearchResult(products = emptyList(), stores = emptyList())
//
//        `when`(searchService.searchByFilter(emptyList())).thenReturn(mockResult)
//
//        val response = mockMvc.get("/api/search") {
//            accept(MediaType.APPLICATION_JSON)
//        }.andExpect {
//            status { isOk() }
//            content { contentType(MediaType.APPLICATION_JSON) }
//        }.andReturn()
//
//        val responseBody = response.response.contentAsString
//        assertEquals(
//            """{"products":[],"stores":[]}""",
//            responseBody
//        )
//    }
//}