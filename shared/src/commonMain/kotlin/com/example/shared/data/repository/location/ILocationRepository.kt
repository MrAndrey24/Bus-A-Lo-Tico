package com.example.shared.data.repository.location

import com.example.shared.domain.entities.Coordinate
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.Flow

fun interface ILocationRepository {
    fun getLocationUpdates(tracker: LocationTracker) : Flow<Coordinate>
}