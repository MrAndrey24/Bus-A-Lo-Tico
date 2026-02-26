package com.example.shared.di.modules

import com.example.shared.presentation.LocationViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { LocationViewModel(get()) }

}