package com.ovais.navigation3sample.http

import com.ovais.navigation3sample.base.BaseTest
import com.ovais.navigation3sample.features.home.data.PostsResult
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test

class Navigation3HttpClientTest : BaseTest() {

    private lateinit var client: Navigation3HttpClient

    private fun createMockClient(responseJson: String, status: HttpStatusCode): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond(
                        content = responseJson,
                        status = status,
                        headers = headersOf(HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))
                    )
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Test
    fun `get all post when api returns success`() = testScope.runTest {
        client = DefaultNavigation3HttpClient(
            httpClient = createMockClient(
                responseJson = """
                    [
                      {
                        "userId": 1,
                        "id": 1,
                        "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                        "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
                      },
                      {
                        "userId": 1,
                        "id": 2,
                        "title": "qui est esse",
                        "body": "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla"
                      }
                    ]
                """.trimIndent(),
                status = HttpStatusCode.OK
            )
        )

        val result = client.getAllPosts()

        assertTrue(result is PostsResult.Success)
    }

    @Test
    fun `get all posts when returns failure on 404`() = testScope.runTest {
        val client =
            DefaultNavigation3HttpClient(
                httpClient = createMockClient(
                    responseJson = "",
                    status = HttpStatusCode.NotFound
                )
            )

        val result = client.getAllPosts()

        assertTrue(result is PostsResult.Failure)
    }

    @Test
    fun `get all posts when api returns failure on timeout`() = testScope.runTest {
        val engine = MockEngine { throw SocketTimeoutException("timeout") }
        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json()
            }
        }

        val client = DefaultNavigation3HttpClient(httpClient)
        val result = client.getAllPosts()

        assertTrue(result is PostsResult.Failure)
        assertTrue((result as PostsResult.Failure).message.contains("Socket Timeout"))
    }

    @Test
    fun `get all posts when api returns failure on server response`() = testScope.runTest {
        val engine = MockEngine {
            respond(
                content = "Internal Server Error",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf("Content-Type" to listOf(ContentType.Text.Plain.toString()))
            )
        }

        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json()
            }
        }

        val client = DefaultNavigation3HttpClient(httpClient)
        val result = client.getAllPosts()

        assertTrue(result is PostsResult.Failure)
        assertTrue((result as PostsResult.Failure).message.contains("Internal Server Error"))
    }

    @Test
    fun `get all post when api returns failure on client request error`() = runTest {
        val engine = MockEngine {
            respond(
                content = "Not Found",
                status = HttpStatusCode.NotFound,
                headers = headersOf("Content-Type" to listOf(ContentType.Text.Plain.toString()))
            )
        }

        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json()
            }
        }

        val client = DefaultNavigation3HttpClient(httpClient)
        val result = client.getAllPosts()

        assertTrue(result is PostsResult.Failure)
    }

}
