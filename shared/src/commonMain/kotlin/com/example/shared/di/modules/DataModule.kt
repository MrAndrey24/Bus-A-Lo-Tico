package com.example.shared.di.modules

import com.example.shared.data.local.LocationLocalDataSource
import com.example.shared.data.repository.ILocationRepository
import org.koin.dsl.module

val dataModule = module {
    single<ILocationRepository> { LocationLocalDataSource() }
}