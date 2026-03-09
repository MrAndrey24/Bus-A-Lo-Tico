package com.example.shared.di.modules

import com.example.shared.data.local.LocationLocalDataSource
import com.example.shared.data.remote.company.CompanyRemoteDataSource
import com.example.shared.data.remote.company.ICompanyRemoteDataSource
import com.example.shared.data.remote.route.IRouteRemoteDataSource
import com.example.shared.data.remote.route.RouteRemoteDataSource
import com.example.shared.data.repository.company.CompanyRepository
import com.example.shared.data.repository.company.ICompanyRepository
import com.example.shared.data.repository.location.ILocationRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.data.repository.route.RouteRepository
import org.koin.dsl.module

val dataModule = module {
    single<ILocationRepository> { LocationLocalDataSource() }
    single<ICompanyRemoteDataSource> { CompanyRemoteDataSource() }
    single<IRouteRemoteDataSource> { RouteRemoteDataSource() }
    single<ICompanyRepository> { CompanyRepository(get()) }
    single<IRouteRepository> { RouteRepository(get()) }
}
