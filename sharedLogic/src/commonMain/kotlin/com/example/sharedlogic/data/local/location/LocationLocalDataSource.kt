/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.data.local.location

import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.location.PlatformLocationTracker
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
