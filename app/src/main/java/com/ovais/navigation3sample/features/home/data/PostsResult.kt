package com.ovais.navigation3sample.features.home.data

sealed interface PostsResult {
    data class Success(val posts: List<PostResponse>) : PostsResult
    data class Failure(val message: String) : PostsResult
}