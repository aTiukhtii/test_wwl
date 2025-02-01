package com.wwl.test.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wwl.test.domain.model.GifModel
import com.wwl.test.domain.repository.GiphyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifListViewModel @Inject constructor(
    private val giphyRepository: GiphyRepository
) : ViewModel() {
    private val _gifs = MutableStateFlow<List<GifModel>>(listOf())
    val gifs: StateFlow<List<GifModel>> = _gifs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private var currentPage = 0
    private val pageSize = 20

    init {
        getGifs()
    }

    fun getGifs() = viewModelScope.launch {
        _isLoading.value = true
        giphyRepository.getGifs(currentPage * pageSize).fold(
            onSuccess = {
                _gifs.value = (_gifs.value + it).mapIndexed { index, gifModel ->
                    GifModel(gifModel.id, gifModel.url, gifModel.aspectRatio, index)
                }
                currentPage++
            },
            onFailure = { _isError.value = true }
        )
        _isLoading.value = false
    }
}