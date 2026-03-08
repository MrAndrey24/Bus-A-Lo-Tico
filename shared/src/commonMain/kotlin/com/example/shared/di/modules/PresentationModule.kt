package com.example.shared.di.modules

import com.example.shared.presentation.CompanyViewModel
import com.example.shared.presentation.LocationViewModel
import com.example.shared.presentation.RouteViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { LocationViewModel(get()) }
    factory { CompanyViewModel(get()) }
    factory { RouteViewModel(get()) }
}
