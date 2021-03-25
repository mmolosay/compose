package com.ordolabs.compose.di

import com.ordolabs.data.datastore.ComposeDS
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DataStoreModule = module {

    single {
        ComposeDS(androidContext())
    }
}