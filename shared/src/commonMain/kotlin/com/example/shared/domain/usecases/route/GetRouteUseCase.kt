package com.example.shared.domain.usecases.route

import com.example.shared.domain.entities.Route
import com.example.shared.data.repository.route.IRouteRepository

class GetRouteUseCase(
    private val repository: IRouteRepository
) {
    suspend operator fun invoke(routeId: String): Route? = repository.getRoute(routeId)
}
