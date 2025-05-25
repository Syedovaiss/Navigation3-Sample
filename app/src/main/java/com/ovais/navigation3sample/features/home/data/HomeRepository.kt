package com.ovais.navigation3sample.features.home.data

import com.ovais.navigation3sample.http.Navigation3HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun interface HomeRepository {
    suspend fun getAllPosts(): PostsResult
}

class DefaultHomeRepository(
    private val apiClient: Navigation3HttpClient,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
) : HomeRepository {
    override suspend fun getAllPosts(): PostsResult {
        return withContext(dispatcherIO) {
            apiClient.getAllPosts()
        }
    }
}