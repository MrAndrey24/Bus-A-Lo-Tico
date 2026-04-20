package com.example.shared.di.modules

import com.example.shared.presentation.admin.AdminCompanyViewModel
import com.example.shared.presentation.auth.AuthViewModel
import com.example.shared.presentation.company.CompanyListViewModel
import com.example.shared.presentation.company.CompanyViewModel
import com.example.shared.presentation.driver.DriverRouteViewModel
import com.example.shared.presentation.location.LocationViewModel
import com.example.shared.presentation.notification.NotificationViewModel
import com.example.shared.presentation.route.RouteAdministrativeDetailViewModel
import com.example.shared.presentation.route.RouteDetailViewModel
import com.example.shared.presentation.route.RouteViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { AdminCompanyViewModel(get(), get(), get(), get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { CompanyListViewModel(get()) }
    viewModel { LocationViewModel(get()) }
    viewModel { CompanyViewModel(get()) }
    viewModel { RouteViewModel(get(), get(), get(), get(), get()) }
    viewModel { DriverRouteViewModel(get(), get(), get(), get(), get()) }
    viewModel { RouteAdministrativeDetailViewModel(get(), get(), get()) }
    viewModel { NotificationViewModel(get(), get()) }
    viewModel { RouteDetailViewModel(get(), get()) }
}
