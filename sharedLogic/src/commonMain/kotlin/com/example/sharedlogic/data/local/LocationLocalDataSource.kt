/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.sharedlogic.data.local

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
