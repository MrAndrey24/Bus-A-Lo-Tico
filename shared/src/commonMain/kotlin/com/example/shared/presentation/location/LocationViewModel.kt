/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.presentation.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.location.PlatformLocationTracker
import com.example.shared.domain.usecases.location.TrackUserLocationUseCase
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
