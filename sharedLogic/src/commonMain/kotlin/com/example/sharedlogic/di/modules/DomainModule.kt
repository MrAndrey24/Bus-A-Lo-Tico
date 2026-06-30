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

import com.example.sharedlogic.domain.usecases.company.GetCompanyUseCase
import com.example.sharedlogic.domain.usecases.location.TrackUserLocationUseCase
import com.example.sharedlogic.domain.usecases.route.GetRouteUseCase
import com.example.sharedlogic.domain.usecases.route.GetRoutesUseCase
import com.example.sharedlogic.domain.usecases.route.SearchRoutesByLocationUseCase
import org.koin.dsl.module

val domainModule = module {
  factory { TrackUserLocationUseCase(get()) }
  factory { GetCompanyUseCase(get()) }
  factory { GetRoutesUseCase(get()) }
  factory { GetRouteUseCase(get()) }
  factory { SearchRoutesByLocationUseCase(get(), get()) }
}
