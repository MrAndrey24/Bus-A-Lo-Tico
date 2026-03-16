package com.example.shared.di.modules

import com.example.shared.presentation.auth.AuthViewModel
import com.example.shared.presentation.company.CompanyViewModel
import com.example.shared.presentation.location.LocationViewModel
import com.example.shared.presentation.route.RouteDetailViewModel
import com.example.shared.presentation.route.RouteViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { AuthViewModel(get(), get()) }
    factory { LocationViewModel(get()) }
    factory { CompanyViewModel(get()) }
    factory { RouteViewModel(get(), get(), get()) }
    single { RouteDetailViewModel(get(), get()) }
}
