package com.wwl.test.ui.details

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wwl.test.domain.model.GifUiState
import com.wwl.test.ui.reusable.Gif
import com.wwl.test.ui.reusable.Loader

@Composable
fun GifDetailsScreen(
    viewModel: GifDetailsViewModel,
    navigateBack: () -> Unit
) {
    when (val gifUiState = viewModel.gif.collectAsState().value) {
        GifUiState.Loading -> Loader()

        is GifUiState.Error -> {
            Toast.makeText(LocalContext.current, "Error occurred", Toast.LENGTH_SHORT).show()
            navigateBack()
        }

        is GifUiState.Success -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                IconButton(navigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Navigate back")
                }
                Gif(
                    gifUiState.gif,
                    Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    isClickable = false
                )
            }
        }
    }
}

