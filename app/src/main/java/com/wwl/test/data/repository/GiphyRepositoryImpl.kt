package com.wwl.test.data.repository

import com.wwl.test.data.db.GifDao
import com.wwl.test.data.helper.asyncTryCatch
import com.wwl.test.data.helper.handleApiResponse
import com.wwl.test.data.mapper.toDbModel
import com.wwl.test.data.mapper.toModel
import com.wwl.test.data.network.GiphyApi
import com.wwl.test.domain.model.GifUiState
import com.wwl.test.domain.repository.GiphyRepository
import javax.inject.Inject

class GiphyRepositoryImpl @Inject constructor(
    private val api: GiphyApi,
    private val gifDao: GifDao
) : GiphyRepository {
    override suspend fun getGifs(offset: Int) = asyncTryCatch {
        api.getGifs(offset).handleApiResponse().map { it.toModel() }
    }

    override suspend fun getGifById(id: String): Result<GifUiState> = asyncTryCatch {
        gifDao.getGifById(id)?.let {
            return@asyncTryCatch Result.success(GifUiState.Success(it.toModel()))
        }

        api.getGifById(id).handleApiResponse().map {
            gifDao.addGifToCache(it.toDbModel())
            GifUiState.Success(it.toModel())
        }
    }
}