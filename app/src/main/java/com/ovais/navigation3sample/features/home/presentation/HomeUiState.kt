package com.ovais.navigation3sample.features.home.presentation

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Error(val message: String) : HomeUiState
    data class Loaded(val data: HomeUiData) : HomeUiState
}