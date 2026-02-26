package com.example.shared.domain.usecases

import com.example.shared.data.repository.ILocationRepository
import com.example.shared.domain.entities.Coordinate
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.Flow

class TrackUserLocationUseCase(private val repository: ILocationRepository) {
    operator fun invoke(tracker: LocationTracker): Flow<Coordinate> {
        return repository.getLocationUpdates(tracker)
    }
}