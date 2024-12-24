package com.example.project24.store

import com.example.project24.address.Address
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
@AutoConfigureMockMvc
class StoreControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var storeService: StoreService

    @Test
    fun `should create store successfully`() {
        val address = Address(
            1, "Test Street", "1", "TS", "12345",
        )
        val media = listOf(
            Media(
                "http://example.com/image1.jpg", com.example.project24
                    .store.MediaType.IMAGE
            ),
            Media(
                "http://example.com/image2.jpg", com.example.project24
                    .store.MediaType.IMAGE
            )
        )
        val store = Store(
            id = 0,
            name = "Test Store",
            description = "A test store description",
            media = media,
            address = address
        )
        `when`(storeService.createStore(store)).then { /* Do nothing */ }

        val requestBody = objectMapper.writeValueAsString(store)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        assertEquals(201, result.response.status)
    }

    @Test
    fun `should return bad request for invalid store data`() {
        val store = mapOf(
            "name" to "",
            "description" to "",
            "media" to emptyList<Map<String, String>>(),
            "address" to null
        )
        val requestBody = objectMapper.writeValueAsString(store)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}