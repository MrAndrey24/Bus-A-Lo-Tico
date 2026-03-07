package com.example.shared.domain.repository

import com.example.shared.domain.entities.Route

interface IRouteRepository {
    suspend fun getRoute(routeId: String): Route?
}
