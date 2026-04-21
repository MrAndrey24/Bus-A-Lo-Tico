/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.domain.location

import com.example.shared.domain.entities.Coordinate
import kotlinx.coroutines.flow.Flow

interface PlatformLocationTracker {
  fun getLocationUpdates(): Flow<Coordinate>

  suspend fun startTracking()

  suspend fun stopTracking()
}
