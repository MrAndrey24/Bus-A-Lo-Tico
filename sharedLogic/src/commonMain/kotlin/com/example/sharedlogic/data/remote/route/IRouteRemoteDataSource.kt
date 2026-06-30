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
package com.example.sharedlogic.data.remote.route

import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.RouteStop

interface IRouteRemoteDataSource {
  suspend fun getRoutes(): List<Route>

  suspend fun getRoute(routeId: String): Route?

  suspend fun getRouteByDriverId(driverId: String): Route?

  suspend fun getRoutesByDriverId(driverId: String): List<Route>

  suspend fun upsertRoute(route: Route): Route

  suspend fun upsertRouteStop(routeId: String, stop: RouteStop): RouteStop

  suspend fun deleteRouteStop(routeId: String, stopId: String)

  suspend fun upsertRouteSchedule(routeId: String, schedule: RouteSchedule): RouteSchedule

  suspend fun deleteRouteSchedule(routeId: String, scheduleId: String)
}
