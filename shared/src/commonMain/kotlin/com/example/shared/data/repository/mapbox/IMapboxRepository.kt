package com.example.shared.data.repository.mapbox

import com.example.shared.domain.entities.Coordinate

interface IMapboxRepository {
    suspend fun getStreetGeometry(waypoints: List<Coordinate>): List<Coordinate>
}