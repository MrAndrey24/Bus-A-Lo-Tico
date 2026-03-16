package com.example.shared.data.local

import com.example.shared.domain.entities.Coordinate
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.Flow

fun interface ILocationLocalDataSource {
    fun getLocationUpdates(tracker: LocationTracker) : Flow<Coordinate>
}