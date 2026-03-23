package com.example.shared.data.repository.route

import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.Route

interface IRouteRepository {
    suspend fun getRoute(routeId: String): Route?
    suspend fun getRouteByDriverId(driverId: String): Route?
    suspend fun getRoutes(): List<Route>
}
