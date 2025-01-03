package com.example.project24.media

data class MediaDTO(
    val id: Long,
    val imageUrl: String
)

fun mapToMediaDTO(media: Media): MediaDTO {
    return MediaDTO(
        id = media.id,
        imageUrl = media.imageUrl
    )
}

fun mapMediaDTOtoMedia(mediaDTO: MediaDTO): Media {
    return Media(
        id = mediaDTO.id,
        imageUrl = mediaDTO.imageUrl
    )
}
