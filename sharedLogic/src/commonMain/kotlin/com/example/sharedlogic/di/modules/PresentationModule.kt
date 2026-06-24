/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.di.modules

import com.example.sharedlogic.presentation.admin.AdminCompanyViewModel
import com.example.sharedlogic.presentation.auth.AuthViewModel
import com.example.sharedlogic.presentation.company.CompanyListViewModel
import com.example.sharedlogic.presentation.company.CompanyViewModel
import com.example.sharedlogic.presentation.driver.DriverRouteViewModel
import com.example.sharedlogic.presentation.location.LocationViewModel
import com.example.sharedlogic.presentation.notification.NotificationViewModel
import com.example.sharedlogic.presentation.route.RouteViewModel
import com.example.sharedlogic.presentation.route.admin.RouteAdministrativeDetailViewModel
import com.example.sharedlogic.presentation.route.admin.RouteDetailViewModel
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
