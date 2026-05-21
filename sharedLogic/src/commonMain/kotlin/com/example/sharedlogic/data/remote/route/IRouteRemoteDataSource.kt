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
