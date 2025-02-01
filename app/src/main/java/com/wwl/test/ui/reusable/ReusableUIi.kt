package com.wwl.test.ui.reusable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.wwl.test.domain.model.GifModel

@Composable
fun Loader() {
    Box(Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Gif(
    gif: GifModel,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true,
    onGifClicked: ((String) -> Unit)? = null
) {
    AsyncImage(
        gif.url,
        null,
        ImageLoader(LocalContext.current),
        modifier
            .aspectRatio(gif.aspectRatio)
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .let {
                if (isClickable) {
                    it.clickable { onGifClicked?.invoke(gif.id) }
                } else it
            },
        contentScale = ContentScale.Fit
    )
}