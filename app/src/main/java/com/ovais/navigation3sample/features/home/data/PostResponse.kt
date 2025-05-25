package com.ovais.navigation3sample.features.home.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    @SerialName("title")
    val postTitle: String,
    @SerialName("body")
    val postDescription: String,
    val id: Int,
    val userId: Int
)