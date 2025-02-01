package com.wwl.test.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Gifs")
data class LocalGifModel(
    @PrimaryKey val id: String,
    val url: String,
    val aspectRatio: Float
)
