package com.wwl.test.domain.repository

import com.wwl.test.domain.model.GifModel
import com.wwl.test.domain.model.GifUiState

interface GiphyRepository {
    suspend fun getGifs(offset: Int): Result<List<GifModel>>
    suspend fun getGifById(id: String): Result<GifUiState>
}