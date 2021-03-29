package com.ordolabs.compose.di

import com.ordolabs.compose.viewmodel.ChordViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        ChordViewModel()
    }
}