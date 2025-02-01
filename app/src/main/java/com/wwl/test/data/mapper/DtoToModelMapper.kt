package com.wwl.test.data.mapper

import com.wwl.test.data.model.local.LocalGifModel
import com.wwl.test.data.model.remote.GifResponseDto
import com.wwl.test.data.model.remote.GifsListResponseDto
import com.wwl.test.domain.model.GifModel

fun GifsListResponseDto.toModel() = data.map {
    GifModel(
        id = it.id,
        url = it.images.preview.url,
        aspectRatio = it.images.preview.width.toFloat() / it.images.preview.height
    )
}

fun GifResponseDto.toModel() = GifModel(
    id = data.id,
    url = data.images.original.url,
    aspectRatio = data.images.original.width.toFloat() / data.images.original.height
)

fun GifResponseDto.toDbModel() = LocalGifModel(
    id = data.id,
    url = data.images.original.url,
    aspectRatio = data.images.original.width.toFloat() / data.images.original.height
)

fun LocalGifModel.toModel() = GifModel(
    id = id,
    url = url,
    aspectRatio = aspectRatio
)