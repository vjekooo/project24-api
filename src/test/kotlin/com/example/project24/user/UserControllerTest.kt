package com.example.project24.user

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.test.context.support.WithMockUser

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @WithMockUser(username = "test@example.com")
    fun `findByEmail should return user when user exists`() {
        val user = User(
            id = 1L,
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe",
            password = "test123",
            role = Role.USER,
            enabled = true,
            address = null,
            store = null
        )

        Mockito.`when`(userRepository.findByEmail("test@example.com"))
            .thenReturn(user)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(user.email)
            )
    }

    @Test
    @WithMockUser(username = "notfound@example.com")
    fun `findByEmail should return 404 when user does not exist`() {
        Mockito.`when`(userRepository.findByEmail("notfound@example.com"))
            .thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `createUser should return 201 Created when user is successfully created`() {
        val user = User(
            id = 0L,
            email = "newuser@example.com",
            firstName = "Jane",
            lastName = "Doe",
            password = "password123",
            role = Role.USER,
            enabled = true,
            address = null,
            store = null
        )

        val userJson = objectMapper.writeValueAsString(user)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)

        Mockito.verify(userRepository).save(user)
    }

    @Test
    fun `updateUser should return 201 Created when user is successfully updated`() {
        val existingUser = User(
            id = 1L,
            email = "existinguser@example.com",
            firstName = "Existing",
            lastName = "User",
            password = "password123",
            role = Role.USER,
            enabled = true,
            address = null,
            store = null
        )

        val updatedUser = User(
            id = 1L,
            email = "existinguser@example.com",
            firstName = "Updated",
            lastName = "User",
            password = "newpassword123",
            role = Role.ADMIN,
            enabled = false,
            address = null,
            store = null
        )

        Mockito.`when`(userRepository.findByEmail("existinguser@example.com"))
            .thenReturn(existingUser)

        val updatedUserJson = objectMapper.writeValueAsString(updatedUser)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedUserJson)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)

        Mockito.verify(userRepository)
            .save(Mockito.argThat { it.id == existingUser.id && it.firstName == "Updated" })
    }

    @Test
    fun `updateUser should return 404 when user to update does not exist`() {
        val user = User(
            id = 2L,
            email = "nonexistentuser@example.com",
            firstName = "Nonexistent",
            lastName = "User",
            password = "password123",
            role = Role.USER,
            enabled = true,
            address = null,
            store = null
        )

        Mockito.`when`(userRepository.findByEmail("nonexistentuser@example.com"))
            .thenReturn(null)

        val userJson = objectMapper.writeValueAsString(user)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}