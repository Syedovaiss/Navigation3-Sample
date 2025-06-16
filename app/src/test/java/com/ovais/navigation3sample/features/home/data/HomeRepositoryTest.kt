package com.ovais.navigation3sample.features.home.data

import com.ovais.navigation3sample.base.BaseTest
import com.ovais.navigation3sample.http.Navigation3HttpClient
import io.ktor.http.HttpStatusCode
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class HomeRepositoryTest : BaseTest() {

    private lateinit var homeRepository: HomeRepository

    @Mock
    private lateinit var httpClient: Navigation3HttpClient

    override fun setup() {
        super.setup()
        homeRepository = DefaultHomeRepository(
            apiClient = httpClient,
            dispatcherIO = testDispatcher
        )
    }

    @Test
    fun `get all posts when api returns success`() = testScope.runTest {
        val result = PostsResult.Success(
            posts = listOf(
                PostResponse(
                    postTitle = "Some Title",
                    postDescription = "Some description",
                    id = randomNumber,
                    userId = randomNumber
                )
            )
        )
        `when`(httpClient.getAllPosts()).thenReturn(result)

        val executedResult = homeRepository.getAllPosts()

        advanceUntilIdle()

        assertTrue(executedResult is PostsResult.Success)
        assertTrue((executedResult as PostsResult.Success).posts.isNotEmpty())
    }

    @Test
    fun `get all posts when api returns failure`() = testScope.runTest {
        val result = PostsResult.Failure(HttpStatusCode.InternalServerError.description)
        `when`(httpClient.getAllPosts()).thenReturn(result)

        val executedResult = homeRepository.getAllPosts()

        advanceUntilIdle()

        assertTrue(executedResult is PostsResult.Failure)
    }
}