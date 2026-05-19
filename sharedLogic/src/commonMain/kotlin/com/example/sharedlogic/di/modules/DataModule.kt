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

import com.example.sharedlogic.data.local.ILocationLocalDataSource
import com.example.sharedlogic.data.local.LocationLocalDataSource
import com.example.sharedlogic.data.remote.auth.AuthRemoteDataSource
import com.example.sharedlogic.data.remote.auth.IAuthRemoteDataSource
import com.example.sharedlogic.data.remote.company.CompanyRemoteDataSource
import com.example.sharedlogic.data.remote.company.ICompanyRemoteDataSource
import com.example.sharedlogic.data.remote.datasource.notification.INotificationRemoteDataSource
import com.example.sharedlogic.data.remote.datasource.notification.NotificationRemoteDataSource
import com.example.sharedlogic.data.remote.driver.DriverRemoteDataSource
import com.example.sharedlogic.data.remote.driver.IDriverRemoteDataSource
import com.example.sharedlogic.data.remote.route.IRouteRemoteDataSource
import com.example.sharedlogic.data.remote.route.RouteRemoteDataSource
import com.example.sharedlogic.data.remote.trip.ITripExecutionRemoteDataSource
import com.example.sharedlogic.data.remote.trip.TripExecutionRemoteDataSource
import com.example.sharedlogic.data.remote.user.IUserRemoteDataSource
import com.example.sharedlogic.data.remote.user.UserRemoteDataSource
import com.example.sharedlogic.data.repository.auth.AuthRepository
import com.example.sharedlogic.data.repository.auth.IAuthRepository
import com.example.sharedlogic.data.repository.company.CompanyRepository
import com.example.sharedlogic.data.repository.company.ICompanyRepository
import com.example.sharedlogic.data.repository.driver.DriverRepository
import com.example.sharedlogic.data.repository.driver.IDriverRepository
import com.example.sharedlogic.data.repository.location.ILocationRepository
import com.example.sharedlogic.data.repository.location.LocationRepository
import com.example.sharedlogic.data.repository.mapbox.IMapboxRepository
import com.example.sharedlogic.data.repository.mapbox.MapboxRepository
import com.example.sharedlogic.data.repository.notification.INotificationRepository
import com.example.sharedlogic.data.repository.notification.NotificationRepository
import com.example.sharedlogic.data.repository.route.IRouteRepository
import com.example.sharedlogic.data.repository.route.RouteRepository
import com.example.sharedlogic.data.repository.trip.ITripExecutionRepository
import com.example.sharedlogic.data.repository.trip.TripExecutionRepository
import com.example.sharedlogic.data.repository.user.IUserRepository
import com.example.sharedlogic.data.repository.user.UserRepository
import org.koin.dsl.module

val dataModule = module {
  single<ILocationLocalDataSource> { LocationLocalDataSource() }
  single<ILocationRepository> { LocationRepository(get()) }
  single<IAuthRemoteDataSource> { AuthRemoteDataSource() }
  single<ICompanyRemoteDataSource> { CompanyRemoteDataSource() }
  single<IDriverRemoteDataSource> { DriverRemoteDataSource() }
  single<IRouteRemoteDataSource> { RouteRemoteDataSource() }
  single<ITripExecutionRemoteDataSource> { TripExecutionRemoteDataSource() }
  single<IUserRemoteDataSource> { UserRemoteDataSource() }
  single<IAuthRepository> { AuthRepository(get()) }
  single<ICompanyRepository> { CompanyRepository(get()) }
  single<IDriverRepository> { DriverRepository(get()) }
  single<IRouteRepository> { RouteRepository(get()) }
  single<ITripExecutionRepository> { TripExecutionRepository(get()) }
  single<IUserRepository> { UserRepository(get()) }
  single<IMapboxRepository> { MapboxRepository(get()) }
  single<INotificationRemoteDataSource> { NotificationRemoteDataSource() }
  single<INotificationRepository> { NotificationRepository(get()) }
}
