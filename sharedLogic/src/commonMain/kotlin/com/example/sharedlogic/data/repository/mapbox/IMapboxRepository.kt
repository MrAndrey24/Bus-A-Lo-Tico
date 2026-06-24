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
package com.example.sharedlogic.data.repository.mapbox

import com.example.sharedlogic.domain.entities.Coordinate

interface IMapboxRepository {
  suspend fun getStreetGeometry(waypoints: List<Coordinate>): List<Coordinate>

  suspend fun getStopDurations(waypoints: List<Coordinate>): List<Double>

  suspend fun searchLocation(query: String): List<Coordinate>
}
