package com.ovais.navigation3sample.core.app

import android.app.Application
import com.ovais.navigation3sample.di.networkModule
import com.ovais.navigation3sample.di.factoryModule
import com.ovais.navigation3sample.di.singletonModule
import com.ovais.navigation3sample.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Navigation3SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Navigation3SampleApplication)
            modules(networkModule, singletonModule, factoryModule, viewModelModule)
        }
    }
}