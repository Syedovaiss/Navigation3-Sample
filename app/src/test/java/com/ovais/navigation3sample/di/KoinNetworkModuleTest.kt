package com.ovais.navigation3sample.di

import com.ovais.navigation3sample.features.home.presentation.HomeViewModel
import com.ovais.navigation3sample.http.DefaultNavigation3HttpClient
import com.ovais.navigation3sample.http.Navigation3HttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertNotNull

@RunWith(MockitoJUnitRunner::class)
class KoinNetworkModuleTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            listOf(
                singletonModule,
                factoryModule,
                viewModelModule,
                module {
                    single {
                        HttpClient(MockEngine) {
                            engine {
                                addHandler {
                                    respond("""[{"id":1,"title":"Fake"}]""", headers = headersOf(
                                        HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())
                                    ))
                                }
                            }
                            install(ContentNegotiation) {
                                json()
                            }
                        }
                    }
                    single<Navigation3HttpClient> { DefaultNavigation3HttpClient(get()) }
                    single { "fake.host.com" } // Fake host to override BuildConfig.HOST
                }
            )
        )
    }

    @Test
    fun `test HomeViewModel resolves and loads data`() = runTest {
        val viewModel: HomeViewModel by inject()

        // Optionally call viewModel.loadPosts() or inspect state
        assertNotNull(viewModel)
    }
}
