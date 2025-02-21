package com.example.project24.media

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MediaService {

    @Autowired
    lateinit var repository: MediaRepository

    fun getAllFilesByProductId(productId: Long): List<Media> {
        return repository.findAllByProductId(productId)
    }
}