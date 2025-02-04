package com.wwl.test.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wwl.test.domain.model.GifUiState
import com.wwl.test.domain.repository.GiphyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifDetailsViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val giphyRepository: GiphyRepository
) : ViewModel() {
    private val _gif = MutableStateFlow<GifUiState>(GifUiState.Loading)
    val gif = _gif
        .onStart { getGif(stateHandle.get<String>("id")?:"") }
        .stateIn(viewModelScope, Lazily, GifUiState.Loading)

    private fun getGif(id: String) = viewModelScope.launch {
        giphyRepository.getGifById(id).fold(
            onSuccess = { _gif.value = it },
            onFailure = { _gif.value = GifUiState.Error }
        )
    }
}