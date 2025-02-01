package com.wwl.test.di

import android.content.Context
import androidx.room.Room
import com.wwl.test.data.db.GifDao
import com.wwl.test.data.db.GifDb
import com.wwl.test.data.network.GiphyApi
import com.wwl.test.data.repository.GiphyRepositoryImpl
import com.wwl.test.domain.repository.GiphyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object DataModule {
    @[Provides Singleton]
    fun provideGifsRepository(api: GiphyApi, dao: GifDao): GiphyRepository = GiphyRepositoryImpl(api, dao)

    @[Provides Singleton]
    fun provideGifDb(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, GifDb::class.java, "gif_db").build()

    @[Provides Singleton]
    fun provideGifDao(db: GifDb) = db.gifDao()
}