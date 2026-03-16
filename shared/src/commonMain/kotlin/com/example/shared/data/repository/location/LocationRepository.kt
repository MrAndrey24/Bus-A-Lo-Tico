package com.example.shared.data.repository.location

import com.example.shared.data.local.ILocationLocalDataSource
import com.example.shared.domain.entities.Coordinate
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val localDataSource: ILocationLocalDataSource
) : ILocationRepository {


    override fun getLocationUpdates(tracker: LocationTracker): Flow<Coordinate> {
        return localDataSource.getLocationUpdates(tracker)
    }
}