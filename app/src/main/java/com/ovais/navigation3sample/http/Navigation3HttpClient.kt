package com.ovais.navigation3sample.http

import com.ovais.navigation3sample.features.home.data.PostResponse
import com.ovais.navigation3sample.features.home.data.PostsResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get

fun interface Navigation3HttpClient {
    suspend fun getAllPosts(): PostsResult
}

class DefaultNavigation3HttpClient(
    private val httpClient: HttpClient
) : Navigation3HttpClient {
    override suspend fun getAllPosts(): PostsResult {
        return try {
            val result = httpClient.get("posts").body<List<PostResponse>>()
            PostsResult.Success(result)

        } catch (e: SocketTimeoutException) {
            PostsResult.Failure("Socket Timeout:${e.message}")

        } catch (e: ClientRequestException) {
            PostsResult.Failure("Request Error:${e.message}")

        } catch (e: ServerResponseException) {
            PostsResult.Failure("Server Error:${e.message}")

        } catch (e: Exception) {
            PostsResult.Failure(e.message.toString())
        }
    }
}