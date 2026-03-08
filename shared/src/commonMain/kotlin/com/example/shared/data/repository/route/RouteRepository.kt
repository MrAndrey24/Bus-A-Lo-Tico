package com.example.shared.data.repository.route

import com.example.shared.data.remote.route.IRouteRemoteDataSource
import com.example.shared.domain.entities.Route

class RouteRepository(
    private val remoteDataSource: IRouteRemoteDataSource
) : IRouteRepository {

    override suspend fun getRoute(routeId: String): Route? {
        return remoteDataSource.getRoute(routeId)
    }

    override suspend fun getRoutes(): List<Route> {
        return remoteDataSource.getRoutes()
    }
}
