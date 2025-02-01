package com.wwl.test.data.model.remote

import com.google.gson.annotations.SerializedName

data class GifDto(
    val id: String,
    val images: ImageDataDto
) {
    data class ImageDataDto(
        @SerializedName("preview_gif") val preview: ImageUrlDto,
        @SerializedName("original") val original: ImageUrlDto
    ) {
        data class ImageUrlDto(
            val url: String,
            val width: Int,
            val height: Int
        )
    }
}
