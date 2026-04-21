/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.location.PlatformLocationTracker
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private class AndroidPlatformLocationTracker(private val delegate: LocationTracker) :
    PlatformLocationTracker {
  override fun getLocationUpdates(): Flow<Coordinate> {
    return delegate.getLocationsFlow().map { Coordinate(it.latitude, it.longitude) }
  }

  override suspend fun startTracking() {
    delegate.startTracking()
  }

  override suspend fun stopTracking() {
    delegate.stopTracking()
  }

  fun rawTracker(): LocationTracker = delegate
}

actual fun getLocationTracker(
    permissionsController: PermissionsController
): PlatformLocationTracker {
  return AndroidPlatformLocationTracker(
      delegate = LocationTracker(permissionsController = permissionsController)
  )
}

@Composable
actual fun BindPlatformLocationTrackerEffect(locationTracker: PlatformLocationTracker) {
  val androidTracker = locationTracker as? AndroidPlatformLocationTracker ?: return
  BindLocationTrackerEffect(locationTracker = androidTracker.rawTracker())
}
