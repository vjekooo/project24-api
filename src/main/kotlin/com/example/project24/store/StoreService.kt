package com.example.project24.store

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StoreService {

    @Autowired
    lateinit var storeRepository: StoreRepository

    fun createStore(store: Store) {
        this.storeRepository.save(store)
    }

    fun getUserStores(userId: Long): List<Store>? {
        return this.storeRepository.findAllByUserId(userId)
    }

    fun getAllStores(): List<Store> {
        return this.storeRepository.findAll()
    }

    fun getStoreById(id: Long): Store? {
        return this.storeRepository.findById(id).orElse(null)
    }
}