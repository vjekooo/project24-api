package com.example.project24.media

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MediaService {

    @Autowired
    lateinit var repository: MediaRepository

    fun getAllFilesByProductId(productId: Long): List<Media> {
        return repository.findAllByProductId(productId)
    }

    fun getAllFilesByStoreId(storeId: Long): List<Media> {
        return repository.findAllByStoreId(storeId)
    }

    @Transactional
    fun getMediaById(id: Long): List<MediaDTO>? {
        val files = repository.findAllByProductId(id)
        return files.map{ mapToMediaDTO(it) }
    }
}