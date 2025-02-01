package com.wwl.test.domain.model

sealed class GifUiState {
    data object Loading : GifUiState()
    data class Success(val gif: GifModel) : GifUiState()
    data object Error : GifUiState()
}
