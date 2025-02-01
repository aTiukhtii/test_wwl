package com.wwl.test.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wwl.test.data.model.local.LocalGifModel

@Database([LocalGifModel::class], version = 1, exportSchema = false)
abstract class GifDb : RoomDatabase() {
    abstract fun gifDao (): GifDao
}