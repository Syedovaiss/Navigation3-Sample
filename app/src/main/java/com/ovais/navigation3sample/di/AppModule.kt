package com.ovais.navigation3sample.di

import android.util.Log
import com.ovais.navigation3sample.BuildConfig
import com.ovais.navigation3sample.features.home.data.DefaultHomeRepository
import com.ovais.navigation3sample.features.home.data.HomeRepository
import com.ovais.navigation3sample.features.home.domain.DefaultGetAllPostUseCase
import com.ovais.navigation3sample.features.home.domain.GetAllPostUseCase
import com.ovais.navigation3sample.features.home.presentation.HomeViewModel
import com.ovais.navigation3sample.http.DefaultNavigation3HttpClient
import com.ovais.navigation3sample.http.Navigation3HttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val singletonModule = module {
    single<CoroutineDispatcher> {
        Dispatchers.IO
    }
    single<CoroutineDispatcher> {
        Dispatchers.Default
    }
    single<CoroutineDispatcher> {
        Dispatchers.Main
    }

}
val factoryModule = module {
    factory<GetAllPostUseCase> { DefaultGetAllPostUseCase(get()) }
    factory<HomeRepository> { DefaultHomeRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
}

val networkModule = module {
    single<Navigation3HttpClient> { DefaultNavigation3HttpClient(get()) }

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            engine {
                connectTimeout = 60000
                socketTimeout = 60000
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        if (BuildConfig.DEBUG)
                            Log.d("Network Logs:", message)
                    }
                }
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            }
            install(ResponseObserver) {
                onResponse { response ->
                    if (BuildConfig.DEBUG) {
                        Log.d("Http Request Host:", response.request.url.host)
                        Log.d("Http Status:", response.status.value.toString())
                    }
                }
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "jsonplaceholder.typicode.com"
                }
                header(HttpHeaders.ContentType, "application/json")

            }
        }
    }
}