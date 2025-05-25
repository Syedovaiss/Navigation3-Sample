package com.ovais.navigation3sample.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.navigation3sample.features.home.data.PostResponse
import com.ovais.navigation3sample.features.home.data.PostsResult
import com.ovais.navigation3sample.features.home.domain.GetAllPostUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val getAllPostUseCase: GetAllPostUseCase,
    private val dispatcherDefault: CoroutineDispatcher = Dispatchers.Default,
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private val _uiState by lazy {
        MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    }
    val uiState: StateFlow<HomeUiState>
        get() = _uiState

    fun fetchAllPosts() {
        startFetchingPosts()
    }

    private fun startFetchingPosts() {
        viewModelScope.launch {
            val postResult = getAllPostUseCase()
            when (postResult) {
                is PostsResult.Success -> convertPostToUiData(postResult.posts)
                is PostsResult.Failure -> {
                    _uiState.update { HomeUiState.Error(postResult.message) }
                }
            }
        }
    }

    private suspend fun convertPostToUiData(post: List<PostResponse>) {
        withContext(dispatcherDefault) {
            val posts = post.map {
                Post(
                    title = it.postTitle,
                    description = it.postDescription,
                    id = it.id
                )
            }
            updateUiData(posts)
        }
    }

    private suspend fun updateUiData(post: List<Post>) {
        withContext(dispatcherMain) {
            _uiState.update {
                HomeUiState.Loaded(
                    data = HomeUiData(post)
                )
            }
        }
    }
}