/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.repository.location

import com.example.shared.data.local.ILocationLocalDataSource
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.location.PlatformLocationTracker
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val localDataSource: ILocationLocalDataSource) :
    ILocationRepository {

  override fun getLocationUpdates(tracker: PlatformLocationTracker): Flow<Coordinate> {
    return localDataSource.getLocationUpdates(tracker)
  }
}
