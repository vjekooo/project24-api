package com.example.project24.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.IOException
import java.nio.file.Files
import java.util.*

@Service
class S3Service(private val s3Client: S3Client) {
    @Value("\${spring.cloud.aws.s3.bucket}")
    private val bucketName: String? = null

    fun uploadFile(file: MultipartFile): String {
        try {
            val fileName =
                UUID.randomUUID().toString() + "_" + file.originalFilename

            val tempFile = Files.createTempFile(fileName, null)
            file.transferTo(tempFile.toFile())

            val request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build()

            s3Client.putObject(request, tempFile)

            Files.delete(tempFile)

            return fileName
        } catch (e: IOException) {
            throw RuntimeException("Failed to upload file", e)
        }
    }
}