package com.example.shared.domain.usecases

import com.example.shared.domain.entities.Route
import com.example.shared.domain.repository.IRouteRepository

class GetRouteUseCase(
    private val repository: IRouteRepository
) {
    suspend operator fun invoke(routeId: String): Route? = repository.getRoute(routeId)
}
