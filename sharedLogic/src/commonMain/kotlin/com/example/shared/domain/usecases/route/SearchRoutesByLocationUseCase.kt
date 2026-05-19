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
package com.example.shared.domain.usecases.route

import com.example.shared.data.repository.mapbox.IMapboxRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteLocation

class SearchRoutesByLocationUseCase(
    private val routeRepository: IRouteRepository,
    private val mapboxRepository: IMapboxRepository,
) {
  suspend operator fun invoke(query: String, radiusInKilometers: Double = 5.0): List<Route> {
    if (query.isBlank()) return emptyList()

    val allRoutes = routeRepository.getRoutes()

    val textMatches = allRoutes.filter { it.matchesText(query) }

    val searchCoordinates = mapboxRepository.searchLocation(query)
    val geographicMatches =
        searchCoordinates.firstOrNull()?.let { targetPoint ->
          allRoutes.filter { it.isNearLocation(targetPoint, radiusInKilometers) }
        } ?: emptyList()

    return (textMatches + geographicMatches).distinctBy { it.id }
  }

  private fun Route.matchesText(query: String): Boolean {
    return name.contains(query, ignoreCase = true) || code.contains(query, ignoreCase = true)
  }

  private fun Route.isNearLocation(target: Coordinate, radiusInKilometers: Double): Boolean {
    val startCoordinate = startLocation.toCoordinateOrNull()
    val endCoordinate = endLocation.toCoordinateOrNull()

    val isNearStart = startCoordinate?.let { it.distanceTo(target) <= radiusInKilometers } ?: false
    val isNearEnd = endCoordinate?.let { it.distanceTo(target) <= radiusInKilometers } ?: false

    return isNearStart || isNearEnd
  }

  private fun RouteLocation.toCoordinateOrNull(): Coordinate? {
    if (latitude == null || longitude == null) return null

    return Coordinate(latitude, longitude)
  }
}
