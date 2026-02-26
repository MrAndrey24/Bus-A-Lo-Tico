package com.example.shared.data.local

import com.example.shared.data.repository.ILocationRepository
import com.example.shared.domain.entities.Coordinate
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class LocationLocalDataSource : ILocationRepository {

    override fun getLocationUpdates(tracker: LocationTracker): Flow<Coordinate> {
        return tracker.getLocationsFlow()
            .map { location -> Coordinate(location.latitude, location.longitude) }
            .onStart {
                tracker.startTracking()
            }
            .onCompletion {
                tracker.stopTracking()
            }
    }

}