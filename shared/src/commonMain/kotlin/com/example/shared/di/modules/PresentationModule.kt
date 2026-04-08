package com.example.shared.di.modules

import com.example.shared.presentation.auth.AuthViewModel
import com.example.shared.presentation.admin.AdminCompanyViewModel
import com.example.shared.presentation.company.CompanyListViewModel
import com.example.shared.presentation.company.CompanyViewModel
import com.example.shared.presentation.driver.DriverRouteViewModel
import com.example.shared.presentation.location.LocationViewModel
import com.example.shared.presentation.route.RouteAdministrativeDetailViewModel
import com.example.shared.presentation.route.RouteDetailViewModel
import com.example.shared.presentation.route.RouteViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { AdminCompanyViewModel(get(), get(), get(), get()) }
    factory { AuthViewModel(get(), get(), get()) }
    factory { CompanyListViewModel(get()) }
    factory { LocationViewModel(get()) }
    factory { CompanyViewModel(get()) }
    factory { RouteViewModel(get(), get(), get(), get()) }
    factory { DriverRouteViewModel(get(), get(), get()) }
    factory { RouteAdministrativeDetailViewModel(get(), get()) }
    single { RouteDetailViewModel(get(), get()) }
}
