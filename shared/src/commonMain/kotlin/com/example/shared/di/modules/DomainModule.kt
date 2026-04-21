/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.di.modules

import com.example.shared.domain.usecases.company.GetCompanyUseCase
import com.example.shared.domain.usecases.location.TrackUserLocationUseCase
import com.example.shared.domain.usecases.route.GetRouteUseCase
import com.example.shared.domain.usecases.route.GetRoutesUseCase
import com.example.shared.domain.usecases.route.SearchRoutesByLocationUseCase
import org.koin.dsl.module

val domainModule = module {
  factory { TrackUserLocationUseCase(get()) }
  factory { GetCompanyUseCase(get()) }
  factory { GetRoutesUseCase(get()) }
  factory { GetRouteUseCase(get()) }
  factory { SearchRoutesByLocationUseCase(get(), get()) }
}
