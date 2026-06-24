/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.domain.usecases.location

import com.example.sharedlogic.data.repository.location.ILocationRepository
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.location.PlatformLocationTracker
import kotlinx.coroutines.flow.Flow

class TrackUserLocationUseCase(private val repository: ILocationRepository) {
  operator fun invoke(tracker: PlatformLocationTracker): Flow<Coordinate> {
    return repository.getLocationUpdates(tracker)
  }
}
