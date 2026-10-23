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
package com.example.sharedlogic.data.repository.location

import com.example.sharedlogic.data.local.location.ILocationLocalDataSource
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.location.PlatformLocationTracker
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val localDataSource: ILocationLocalDataSource) :
    ILocationRepository {

  override fun getLocationUpdates(tracker: PlatformLocationTracker): Flow<Coordinate> {
    return localDataSource.getLocationUpdates(tracker)
  }
}
