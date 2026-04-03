package com.example.shared.data.remote.route

import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.domain.entities.RouteStop

interface IRouteRemoteDataSource {
    suspend fun getRoutes(): List<Route>
    suspend fun getRoute(routeId: String): Route?
    suspend fun getRouteByDriverId(driverId: String): Route?
    suspend fun upsertRouteStop(routeId: String, stop: RouteStop): RouteStop
    suspend fun deleteRouteStop(routeId: String, stopId: String)
    suspend fun upsertRouteSchedule(routeId: String, schedule: RouteSchedule): RouteSchedule
}
