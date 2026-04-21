/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.repository.location

import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.location.PlatformLocationTracker
import kotlinx.coroutines.flow.Flow

fun interface ILocationRepository {
  fun getLocationUpdates(tracker: PlatformLocationTracker): Flow<Coordinate>
}
