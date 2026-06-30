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
package com.example.sharedlogic.domain.location

import com.example.sharedlogic.domain.entities.Coordinate
import kotlinx.coroutines.flow.Flow

interface PlatformLocationTracker {
  fun getLocationUpdates(): Flow<Coordinate>

  suspend fun startTracking()

  suspend fun stopTracking()
}
