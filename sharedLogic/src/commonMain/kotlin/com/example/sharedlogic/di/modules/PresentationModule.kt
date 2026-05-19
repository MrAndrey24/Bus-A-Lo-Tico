/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.sharedlogic.di.modules

import com.example.sharedlogic.presentation.admin.AdminCompanyViewModel
import com.example.sharedlogic.presentation.auth.AuthViewModel
import com.example.sharedlogic.presentation.company.CompanyListViewModel
import com.example.sharedlogic.presentation.company.CompanyViewModel
import com.example.sharedlogic.presentation.driver.DriverRouteViewModel
import com.example.sharedlogic.presentation.location.LocationViewModel
import com.example.sharedlogic.presentation.notification.NotificationViewModel
import com.example.sharedlogic.presentation.route.RouteAdministrativeDetailViewModel
import com.example.sharedlogic.presentation.route.RouteDetailViewModel
import com.example.sharedlogic.presentation.route.RouteViewModel
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
