package com.example.shared.di.modules

import com.example.shared.data.local.ILocationLocalDataSource
import com.example.shared.data.local.LocationLocalDataSource
import com.example.shared.data.remote.company.CompanyRemoteDataSource
import com.example.shared.data.remote.company.ICompanyRemoteDataSource
import com.example.shared.data.remote.route.IRouteRemoteDataSource
import com.example.shared.data.remote.route.RouteRemoteDataSource
import com.example.shared.data.repository.company.CompanyRepository
import com.example.shared.data.repository.company.ICompanyRepository
import com.example.shared.data.repository.location.ILocationRepository
import com.example.shared.data.repository.location.LocationRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.data.repository.route.RouteRepository
import org.koin.dsl.module

val dataModule = module {
    single<ILocationLocalDataSource> { LocationLocalDataSource() }
    single<ILocationRepository> { LocationRepository(get()) }
    single<ICompanyRemoteDataSource> { CompanyRemoteDataSource() }
    single<IRouteRemoteDataSource> { RouteRemoteDataSource() }
    single<ICompanyRepository> { CompanyRepository(get()) }
    single<IRouteRepository> { RouteRepository(get()) }
}
