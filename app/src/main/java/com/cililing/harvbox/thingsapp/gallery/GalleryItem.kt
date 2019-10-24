package com.cililing.harvbox.thingsapp.gallery

import java.time.LocalDateTime

data class GalleryItem(
    val uriString: String,
    val photoTime: LocalDateTime?
)