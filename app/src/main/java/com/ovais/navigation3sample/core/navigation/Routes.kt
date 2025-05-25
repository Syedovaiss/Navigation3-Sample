package com.ovais.navigation3sample.core.navigation

import com.ovais.navigation3sample.features.home.presentation.Post

sealed interface Routes {
    data object Home : Routes
    data class PostDetails(val post: Post) : Routes
}