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
package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.location.PlatformLocationTracker
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

private class IosPlatformLocationTracker(private val delegate: LocationTracker) :
    PlatformLocationTracker {
  override fun getLocationUpdates(): Flow<Coordinate> = emptyFlow()

  override suspend fun startTracking() {
    delegate.startTracking()
  }

  override suspend fun stopTracking() {
    delegate.stopTracking()
  }
}

actual fun getLocationTracker(
    permissionsController: PermissionsController
): PlatformLocationTracker {
  return IosPlatformLocationTracker(LocationTracker(permissionsController = permissionsController))
}

@Composable
actual fun BindPlatformLocationTrackerEffect(locationTracker: PlatformLocationTracker) = Unit
