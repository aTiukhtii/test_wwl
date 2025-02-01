package com.wwl.test.ui.list

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wwl.test.ui.custom.FlexCustomGrid
import com.wwl.test.ui.helper.InternetHelper.isInternetAvailable
import com.wwl.test.ui.reusable.Gif
import com.wwl.test.ui.reusable.Loader

@Composable
fun GifListScreen(
    viewModel: GifListViewModel,
    onGifClicked: (String) -> Unit
) {
    val context = LocalContext.current
    val gifs by viewModel.gifs.collectAsState()
    val isError by viewModel.isError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val lazyListState = rememberLazyListState()
    var isConnected by remember { mutableStateOf(isInternetAvailable(context)) }
    var isDialogDismissed by remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            isDialogDismissed = false
        }
    }

    HandlePagination(lazyListState, gifs.size, isLoading) {
        viewModel.getGifs()
    }

    HandleErrorMessage(isError) {
        isConnected = it
    }

    Box(
        Modifier.padding(horizontal = 16.dp)
    ) {
        FlexCustomGrid(
            state = lazyListState,
            itemPadding = PaddingValues(8.dp),
            maxRowHeight = 150.dp,
        ) {
            items(gifs, {it.key}, {it.aspectRatio}) {
                Gif(it, onGifClicked = onGifClicked)
            }
        }
        if (isLoading) {
            Loader()
        }
        if (!isConnected && !isDialogDismissed) {
            NoInternetDialog({ isDialogDismissed = true }) {
                isConnected = isInternetAvailable(context)
                if (isConnected) {
                    viewModel.getGifs()
                }
            }
        }
    }
}

@Composable
private fun HandlePagination(
    lazyGridState: LazyListState,
    gifsSize: Int,
    isLoading: Boolean,
    getNewGifs: () -> Unit
) {
    //I was needed to use key here instead of index because of grid specialty
    LaunchedEffect(lazyGridState.firstVisibleItemIndex) {
        lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.key?.let { index ->
            if (index.toString().substringBefore('-').toInt() >= (gifsSize - THRESHOLD) && !isLoading) {
                getNewGifs()
            }
        }
    }
}

@Composable
private fun HandleErrorMessage(isError: Boolean, checkConnection: (Boolean) -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(isError) {
        if (isError) {
            checkConnection(isInternetAvailable(context))
            Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun NoInternetDialog(onDismiss: () -> Unit, onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("No Internet Connection") },
        text = { Text("Please check your internet connection and try again.") },
        dismissButton = { TextButton(onDismiss) {
            Text("Close")
        } },
        confirmButton = {
            TextButton(onRetry) {
                Text("Retry")
            }
        }
    )
}
private const val THRESHOLD = 30