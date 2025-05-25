package com.ovais.navigation3sample.features.home.domain

import com.ovais.navigation3sample.features.home.data.HomeRepository
import com.ovais.navigation3sample.features.home.data.PostsResult

fun interface GetAllPostUseCase {
    suspend operator fun invoke(): PostsResult
}

class DefaultGetAllPostUseCase(
    private val homeRepository: HomeRepository
) : GetAllPostUseCase {
    override suspend fun invoke(): PostsResult {
        return homeRepository.getAllPosts()
    }
}