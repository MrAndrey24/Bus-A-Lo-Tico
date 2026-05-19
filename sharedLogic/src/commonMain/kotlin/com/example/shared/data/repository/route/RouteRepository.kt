/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.shared.data.repository.route

import com.example.shared.data.remote.route.IRouteRemoteDataSource
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.domain.entities.RouteStop

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
