package com.example.shared.data.repository.route

import com.example.shared.data.remote.route.IRouteRemoteDataSource
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.domain.entities.RouteStop

class RouteRepository(
    private val remoteDataSource: IRouteRemoteDataSource
) : IRouteRepository {
    override suspend fun getRoutes(): List<Route> = remoteDataSource.getRoutes()
    override suspend fun getRoute(routeId: String): Route? = remoteDataSource.getRoute(routeId)
    override suspend fun getRouteByDriverId(driverId: String): Route? =
        remoteDataSource.getRouteByDriverId(driverId)
    override suspend fun upsertRoute(route: Route): Route = remoteDataSource.upsertRoute(route)
    override suspend fun upsertRouteStop(routeId: String, stop: RouteStop): RouteStop =
        remoteDataSource.upsertRouteStop(routeId, stop)
    override suspend fun deleteRouteStop(routeId: String, stopId: String) =
        remoteDataSource.deleteRouteStop(routeId, stopId)
    override suspend fun upsertRouteSchedule(routeId: String, schedule: RouteSchedule): RouteSchedule =
        remoteDataSource.upsertRouteSchedule(routeId, schedule)
    override suspend fun deleteRouteSchedule(routeId: String, scheduleId: String) =
        remoteDataSource.deleteRouteSchedule(routeId, scheduleId)
}
