/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.domain.usecases.route

import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.domain.entities.Route

class GetRoutesUseCase(private val repository: IRouteRepository) {
  suspend operator fun invoke(): List<Route> = repository.getRoutes()
}
