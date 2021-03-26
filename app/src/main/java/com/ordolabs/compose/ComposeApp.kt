package com.ordolabs.compose

import android.app.Application
import android.content.Context
import com.ordolabs.compose.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// TODO: add landscape layouts
class ComposeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        setKoin()
    }

    private fun setKoin() {
        startKoin {
            androidContext(this@ComposeApp)
            modules(
                RoomModule,
                DataStoreModule,
                useCaseModule,
                viewModelModule,
                singletonsModule
            )
        }
    }

    companion object {
        lateinit var context: Context
    }
}