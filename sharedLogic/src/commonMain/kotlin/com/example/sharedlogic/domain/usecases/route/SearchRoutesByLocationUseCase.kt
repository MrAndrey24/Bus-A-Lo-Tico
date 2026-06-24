/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.domain.usecases.route

import com.example.sharedlogic.data.repository.mapbox.IMapboxRepository
import com.example.sharedlogic.data.repository.route.IRouteRepository
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteLocation

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
