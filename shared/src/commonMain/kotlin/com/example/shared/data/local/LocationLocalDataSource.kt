/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.local

import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.location.PlatformLocationTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class LocationLocalDataSource : ILocationLocalDataSource {

  override fun getLocationUpdates(tracker: PlatformLocationTracker): Flow<Coordinate> {
    return tracker
        .getLocationUpdates()
        .map { location -> Coordinate(location.latitude, location.longitude) }
        .onStart { tracker.startTracking() }
        .onCompletion { tracker.stopTracking() }
  }
}
