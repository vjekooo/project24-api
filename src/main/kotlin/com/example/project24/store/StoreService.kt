package com.example.project24.store

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StoreService {

    @Autowired
    lateinit var repository: StoreRepository

    fun createStore(store: Store) {
        this.repository.save(store)
    }

    fun updateStore(store: Store) {
        this.repository.save(store)
    }

    fun getUserStores(userId: Long): List<Store>? {
        return this.repository.findAllByUserId(userId)
    }

    fun getAllStores(): List<Store> {
        return this.repository.findAll()
    }

    fun getStoreById(id: Long): Store? {
        return this.repository.findById(id).orElse(null)
    }

    fun getStoreByProductId(productId: Long): Store? {
        return this.repository.findByProductId(productId)
    }

    fun searchByFilter(category: String): List<StoreDTO> {
        val stores = this.repository.findByFilter(category)
        return stores.map({ store ->
            mapToStoreDTO(store)
        })
    }

    fun getStoresByProductCategory(category: String): List<StoreDTO> {
        val stores = this.repository.findAllStoresBySubcategory(category)
        return stores.map({ store ->
            mapToStoreDTO(store)
        })
    }
}