package com.example.shared.domain.usecases.route

import com.example.shared.domain.entities.Route
import com.example.shared.data.repository.route.IRouteRepository

class GetRoutesUseCase(
    private val repository: IRouteRepository
) {
    suspend operator fun invoke(): List<Route> = repository.getRoutes()
}
