package com.ovais.navigation3sample.features.home.presentation

import com.ovais.navigation3sample.base.BaseTest
import com.ovais.navigation3sample.features.home.data.PostResponse
import com.ovais.navigation3sample.features.home.data.PostsResult
import com.ovais.navigation3sample.features.home.domain.GetAllPostUseCase
import io.ktor.http.HttpStatusCode
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BaseTest() {

    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var getAllPostUseCase: GetAllPostUseCase

    override fun setup() {
        super.setup()
        homeViewModel = HomeViewModel(
            getAllPostUseCase = getAllPostUseCase,
            dispatcherDefault = testDispatcher,
            dispatcherMain = testDispatcher
        )
    }

    @Test
    fun `assert loading state`() {

        assertTrue(homeViewModel.uiState.value is HomeUiState.Loading)
    }

    @Test
    fun `fetch all posts when success`() = testScope.runTest {
        val postId = randomNumber
        val result = PostsResult.Success(
            posts = listOf(
                PostResponse(
                    postTitle = "Some Title",
                    postDescription = "Some description",
                    id = postId,
                    userId = randomNumber
                )
            )
        )
        `when`(getAllPostUseCase.invoke()).thenReturn(result)

        homeViewModel.fetchAllPosts()

        advanceUntilIdle()

        assertTrue(homeViewModel.uiState.value is HomeUiState.Loaded)
        assertEquals(
            (homeViewModel.uiState.value as HomeUiState.Loaded).data.posts.first().id,
            postId
        )
    }

    @Test
    fun `fetch all posts when error`() = testScope.runTest {
        val result = PostsResult.Failure(HttpStatusCode.InternalServerError.description)
        `when`(getAllPostUseCase.invoke()).thenReturn(result)

        homeViewModel.fetchAllPosts()

        advanceUntilIdle()

        assertTrue(homeViewModel.uiState.value is HomeUiState.Error)
        assertEquals(
            (homeViewModel.uiState.value as HomeUiState.Error).message,
            HttpStatusCode.InternalServerError.description
        )
    }
}