package com.example.shared.presentation.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.usecases.location.TrackUserLocationUseCase
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val trackLocationUseCase: TrackUserLocationUseCase
): ViewModel() {
    private val _coordinate = MutableStateFlow(Coordinate(9.9895775, -84.8931864))
    val coordinate: StateFlow<Coordinate> = _coordinate.asStateFlow()
    private var trackingJob: Job? = null

    fun startTracking(tracker: LocationTracker) {
        if (trackingJob?.isActive == true) return

        trackingJob = viewModelScope.launch {
            trackLocationUseCase(tracker).collect {
                newCoordinate ->
                _coordinate.value = newCoordinate
            }
        }
    }

    fun stopTracking() {
        trackingJob?.cancel()
        trackingJob = null
    }
}