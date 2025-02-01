package com.wwl.test.data.network

import com.wwl.test.data.model.remote.GifResponseDto
import com.wwl.test.data.model.remote.GifsListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GiphyApi {
    @GET("trending")
    suspend fun getGifs(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 50,
        @Query("rating") rating: String = "r"
    ): Response<GifsListResponseDto>

    @GET("{id}")
    suspend fun getGifById(@Path("id") id: String): Response<GifResponseDto>
}