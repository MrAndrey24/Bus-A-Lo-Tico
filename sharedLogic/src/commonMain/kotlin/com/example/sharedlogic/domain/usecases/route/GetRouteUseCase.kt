/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.domain.usecases.route

import com.example.sharedlogic.data.repository.route.IRouteRepository
import com.example.sharedlogic.domain.entities.Route

class GetRouteUseCase(private val repository: IRouteRepository) {
  suspend operator fun invoke(routeId: String): Route? = repository.getRoute(routeId)
}
