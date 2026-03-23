package com.example.shared.data.remote.route

import com.example.shared.domain.entities.Route

interface IRouteRemoteDataSource {
    suspend fun getRoutes(): List<Route>
    suspend fun getRoute(routeId: String): Route?
    suspend fun getRouteByDriverId(driverId: String): Route?
}
