package com.example.shared.domain.usecases.location

import com.example.shared.data.repository.location.ILocationRepository
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.location.PlatformLocationTracker
import kotlinx.coroutines.flow.Flow

class TrackUserLocationUseCase(private val repository: ILocationRepository) {
    operator fun invoke(tracker: PlatformLocationTracker): Flow<Coordinate> {
        return repository.getLocationUpdates(tracker)
    }
}
