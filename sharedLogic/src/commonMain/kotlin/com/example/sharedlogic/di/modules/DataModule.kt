/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.di.modules

import busalotico.data.local.AppDatabase
import com.example.sharedlogic.data.local.DriverFactory
import com.example.sharedlogic.data.local.location.ILocationLocalDataSource
import com.example.sharedlogic.data.local.location.LocationLocalDataSource
import com.example.sharedlogic.data.local.user.ILocalUserDataSource
import com.example.sharedlogic.data.local.user.LocalUserDataSource
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
import com.example.sharedlogic.data.remote.user.IRemoteUserDataSource
import com.example.sharedlogic.data.remote.user.RemoteUserDataSource
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val dataModule = module {
  single { get<DriverFactory>().createDriver() }
  single { AppDatabase(get()) }
  single { get<AppDatabase>().appDatabaseQueries }
  single<ILocalUserDataSource> { LocalUserDataSource(get()) }
  single<ILocationLocalDataSource> { LocationLocalDataSource() }
  single<ILocationRepository> { LocationRepository(get()) }
  single<IAuthRemoteDataSource> { AuthRemoteDataSource() }
  single<ICompanyRemoteDataSource> { CompanyRemoteDataSource() }
  single<IDriverRemoteDataSource> { DriverRemoteDataSource() }
  single<IRouteRemoteDataSource> { RouteRemoteDataSource() }
  single<ITripExecutionRemoteDataSource> { TripExecutionRemoteDataSource() }
  single<IRemoteUserDataSource> { RemoteUserDataSource() }
  single<IAuthRepository> { AuthRepository(get()) }
  single<ICompanyRepository> { CompanyRepository(get()) }
  single<IDriverRepository> { DriverRepository(get()) }
  single<IRouteRepository> { RouteRepository(get()) }
  single<ITripExecutionRepository> { TripExecutionRepository(get()) }
  single<IUserRepository> { UserRepository(get(), get(), Dispatchers.IO) }
  single<IMapboxRepository> { MapboxRepository(get()) }
  single<INotificationRemoteDataSource> { NotificationRemoteDataSource() }
  single<INotificationRepository> { NotificationRepository(get()) }
}
