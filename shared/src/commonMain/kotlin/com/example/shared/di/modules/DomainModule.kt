package com.example.shared.di.modules

import com.example.shared.domain.usecases.GetCompanyUseCase
import com.example.shared.domain.usecases.GetRouteUseCase
import com.example.shared.domain.usecases.TrackUserLocationUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { TrackUserLocationUseCase(get()) }
    factory { GetCompanyUseCase(get()) }
    factory { GetRouteUseCase(get()) }
}
