package com.example.project24.address

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var addressRepository: AddressRepository

    @Test
    fun `should create a new address when valid input is provided`() {
        // Arrange
        val address = Address(
            id = 0,
            street = "Test Street",
            houseNumber = "123A",
            city = "Test City",
            postalCode = "12345"
        )
        `when`(addressRepository.save(address.copy(id = 0))).thenReturn(
            address.copy(
                id = 1
            )
        )

        // Act
        val result = mockMvc.post("/api/address") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(address)
        }.andReturn()

        // Assert
        assertEquals(201, result.response.status)
        verify(addressRepository).save(address.copy(id = 0))
    }

    @Test
    fun `should return 400 when street is missing`() {
        // Arrange
        val address = """
            {
                "houseNumber": "123A",
                "city": "Test City",
                "postalCode": "12345"
            }
        """.trimIndent()

        // Act
        val result = mockMvc.post("/api/address") {
            contentType = MediaType.APPLICATION_JSON
            content = address
        }.andReturn()

        // Assert
        assertEquals(400, result.response.status)
    }

    @Test
    fun `should return 400 when house number is missing`() {
        // Arrange
        val address = """
            {
                "street": "Test Street",
                "city": "Test City",
                "postalCode": "12345"
            }
        """.trimIndent()

        // Act
        val result = mockMvc.post("/api/address") {
            contentType = MediaType.APPLICATION_JSON
            content = address
        }.andReturn()

        // Assert
        assertEquals(400, result.response.status)
    }

    @Test
    fun `should return 400 when city is missing`() {
        // Arrange
        val address = """
            {
                "street": "Test Street",
                "houseNumber": "123A",
                "postalCode": "12345"
            }
        """.trimIndent()

        // Act
        val result = mockMvc.post("/api/address") {
            contentType = MediaType.APPLICATION_JSON
            content = address
        }.andReturn()

        // Assert
        assertEquals(400, result.response.status)
    }

    @Test
    fun `should return 400 when postal code is missing`() {
        // Arrange
        val address = """
            {
                "street": "Test Street",
                "houseNumber": "123A",
                "city": "Test City"
            }
        """.trimIndent()

        // Act
        val result = mockMvc.post("/api/address") {
            contentType = MediaType.APPLICATION_JSON
            content = address
        }.andReturn()

        // Assert
        assertEquals(400, result.response.status)
    }
}