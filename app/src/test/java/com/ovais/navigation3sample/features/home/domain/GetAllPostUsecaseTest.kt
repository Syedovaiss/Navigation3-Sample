package com.ovais.navigation3sample.features.home.domain

import com.ovais.navigation3sample.base.BaseTest
import com.ovais.navigation3sample.features.home.data.HomeRepository
import com.ovais.navigation3sample.features.home.data.PostResponse
import com.ovais.navigation3sample.features.home.data.PostsResult
import io.ktor.http.HttpStatusCode
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllPostUseCaseTest : BaseTest() {

    private lateinit var useCase: GetAllPostUseCase

    @Mock
    private lateinit var homeRepository: HomeRepository

    override fun setup() {
        super.setup()
        useCase = DefaultGetAllPostUseCase(homeRepository)
    }


    @Test
    fun `get all posts when repository returns success`() = testScope.runTest {
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
        `when`(homeRepository.getAllPosts()).thenReturn(result)

        val executedResult = useCase()

        advanceUntilIdle()

        assertTrue(executedResult is PostsResult.Success)
        assertTrue((executedResult as PostsResult.Success).posts.isNotEmpty())
    }

    @Test
    fun `get all posts when api returns failure`() = testScope.runTest {
        val result = PostsResult.Failure(HttpStatusCode.InternalServerError.description)
        `when`(homeRepository.getAllPosts()).thenReturn(result)

        val executedResult = useCase()

        advanceUntilIdle()

        assertTrue(executedResult is PostsResult.Failure)
    }
}