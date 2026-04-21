/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
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
