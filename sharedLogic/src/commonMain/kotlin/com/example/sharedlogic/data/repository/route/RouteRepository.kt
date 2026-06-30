/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.data.repository.route

import com.example.sharedlogic.data.remote.route.IRouteRemoteDataSource
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.RouteStop

class RouteRepository(private val remoteDataSource: IRouteRemoteDataSource) : IRouteRepository {
  override suspend fun getRoutes(): List<Route> = remoteDataSource.getRoutes()

  override suspend fun getRoute(routeId: String): Route? = remoteDataSource.getRoute(routeId)

  override suspend fun getRouteByDriverId(driverId: String): Route? =
      remoteDataSource.getRouteByDriverId(driverId)

  override suspend fun getRoutesByDriverId(driverId: String): List<Route> =
      remoteDataSource.getRoutesByDriverId(driverId)

  override suspend fun upsertRoute(route: Route): Route = remoteDataSource.upsertRoute(route)

  override suspend fun upsertRouteStop(routeId: String, stop: RouteStop): RouteStop =
      remoteDataSource.upsertRouteStop(routeId, stop)

  override suspend fun deleteRouteStop(routeId: String, stopId: String) =
      remoteDataSource.deleteRouteStop(routeId, stopId)

  override suspend fun upsertRouteSchedule(
      routeId: String,
      schedule: RouteSchedule,
  ): RouteSchedule = remoteDataSource.upsertRouteSchedule(routeId, schedule)

  override suspend fun deleteRouteSchedule(routeId: String, scheduleId: String) =
      remoteDataSource.deleteRouteSchedule(routeId, scheduleId)
}
