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
package com.example.sharedlogic.presentation.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.location.PlatformLocationTracker
import com.example.sharedlogic.domain.usecases.location.TrackUserLocationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val trackLocationUseCase: TrackUserLocationUseCase) : ViewModel() {
  private val _coordinate = MutableStateFlow(Coordinate(9.9895775, -84.8931864))
  val coordinate: StateFlow<Coordinate> = _coordinate.asStateFlow()
  private var trackingJob: Job? = null

  fun startTracking(tracker: PlatformLocationTracker) {
    if (trackingJob?.isActive == true) return

    trackingJob =
        viewModelScope.launch {
          trackLocationUseCase(tracker).collect { newCoordinate ->
            _coordinate.value = newCoordinate
          }
        }
  }

  fun stopTracking() {
    trackingJob?.cancel()
    trackingJob = null
  }
}
