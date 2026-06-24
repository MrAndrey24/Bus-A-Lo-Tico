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
