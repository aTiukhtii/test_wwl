package com.wwl.test.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wwl.test.data.model.local.LocalGifModel

@Dao
interface GifDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGifToCache(gif: LocalGifModel)

    @Query("SELECT * FROM Gifs WHERE id = :id")
    suspend fun getGifById(id: String): LocalGifModel?
}