package com.example.shared.data.repository.mapbox

import com.example.shared.domain.entities.Coordinate

interface IMapboxRepository {
  suspend fun getStreetGeometry(waypoints: List<Coordinate>): List<Coordinate>

  // Returns duration in seconds for each leg between consecutive waypoints.
  // Result size = waypoints.size - 1
  suspend fun getStopDurations(waypoints: List<Coordinate>): List<Double>
}
