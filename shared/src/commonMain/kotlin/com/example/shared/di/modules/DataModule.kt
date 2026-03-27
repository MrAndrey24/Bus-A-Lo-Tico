package com.example.shared.di.modules

import com.example.shared.data.local.ILocationLocalDataSource
import com.example.shared.data.local.LocationLocalDataSource
import com.example.shared.data.remote.auth.AuthRemoteDataSource
import com.example.shared.data.remote.auth.IAuthRemoteDataSource
import com.example.shared.data.remote.company.CompanyRemoteDataSource
import com.example.shared.data.remote.company.ICompanyRemoteDataSource
import com.example.shared.data.remote.driver.DriverRemoteDataSource
import com.example.shared.data.remote.driver.IDriverRemoteDataSource
import com.example.shared.data.remote.route.IRouteRemoteDataSource
import com.example.shared.data.remote.route.RouteRemoteDataSource
import com.example.shared.data.remote.user.IUserRemoteDataSource
import com.example.shared.data.remote.user.UserRemoteDataSource
import com.example.shared.data.repository.auth.AuthRepository
import com.example.shared.data.repository.auth.IAuthRepository
import com.example.shared.data.repository.company.CompanyRepository
import com.example.shared.data.repository.company.ICompanyRepository
import com.example.shared.data.repository.driver.DriverRepository
import com.example.shared.data.repository.driver.IDriverRepository
import com.example.shared.data.repository.location.ILocationRepository
import com.example.shared.data.repository.location.LocationRepository
import com.example.shared.data.repository.mapbox.IMapboxRepository
import com.example.shared.data.repository.mapbox.MapboxRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.data.repository.route.RouteRepository
import com.example.shared.data.repository.user.IUserRepository
import com.example.shared.data.repository.user.UserRepository
import org.koin.dsl.module

val dataModule = module {
    single<ILocationLocalDataSource> { LocationLocalDataSource() }
    single<ILocationRepository> { LocationRepository(get()) }
    single<IAuthRemoteDataSource> { AuthRemoteDataSource() }
    single<ICompanyRemoteDataSource> { CompanyRemoteDataSource() }
    single<IDriverRemoteDataSource> { DriverRemoteDataSource() }
    single<IRouteRemoteDataSource> { RouteRemoteDataSource() }
    single<IUserRemoteDataSource> { UserRemoteDataSource() }
    single<IAuthRepository> { AuthRepository(get()) }
    single<ICompanyRepository> { CompanyRepository(get()) }
    single<IDriverRepository> { DriverRepository(get()) }
    single<IRouteRepository> { RouteRepository(get()) }
    single<IUserRepository> { UserRepository(get()) }
    single<IMapboxRepository> { MapboxRepository(get()) }
}
