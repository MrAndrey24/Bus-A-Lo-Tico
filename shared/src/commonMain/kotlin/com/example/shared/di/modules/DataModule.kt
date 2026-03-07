package com.example.shared.di.modules

import com.example.shared.data.local.LocationLocalDataSource
import com.example.shared.data.remote.CompanyFirestoreDataSource
import com.example.shared.data.remote.RouteFirestoreDataSource
import com.example.shared.data.repository.ILocationRepository
import com.example.shared.domain.repository.ICompanyRepository
import com.example.shared.domain.repository.IRouteRepository
import org.koin.dsl.module

val dataModule = module {
    single<ILocationRepository> { LocationLocalDataSource() }
    single<ICompanyRepository> { CompanyFirestoreDataSource() }
    single<IRouteRepository> { RouteFirestoreDataSource() }
}
