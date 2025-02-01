package com.wwl.test

import android.app.Application
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: Context) = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) add(AnimatedImageDecoder.Factory())
            else add(GifDecoder.Factory())
        }
        .build()
}