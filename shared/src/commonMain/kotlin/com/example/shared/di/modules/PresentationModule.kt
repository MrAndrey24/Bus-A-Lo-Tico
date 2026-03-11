package com.example.shared.di.modules

import com.example.shared.presentation.company.CompanyViewModel
import com.example.shared.presentation.location.LocationViewModel
import com.example.shared.presentation.route.RouteViewModel
import com.example.shared.presentation.RouteDetailViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { LocationViewModel(get()) }
    factory { CompanyViewModel(get()) }
    factory { RouteViewModel(get()) }
    single { RouteDetailViewModel() }
}
