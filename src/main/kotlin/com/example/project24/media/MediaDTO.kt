package com.example.project24.media

data class MediaDTO(
    val id: Long,
    val imageUrl: String
)

val MEDIA_URL: String
    get() = "https://project24-files.s3.eu-west-1.amazonaws.com"

fun mapToMediaDTO(media: Media): MediaDTO {
    return MediaDTO(
        id = media.id,
        imageUrl = "${MEDIA_URL}/${media.imageUrl}"
    )
}