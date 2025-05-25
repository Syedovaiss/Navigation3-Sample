package com.ovais.navigation3sample.features.home.presentation

data class HomeUiData(
    val posts: List<Post>
)

data class Post(
    val title: String,
    val description: String,
    val id: Int
)