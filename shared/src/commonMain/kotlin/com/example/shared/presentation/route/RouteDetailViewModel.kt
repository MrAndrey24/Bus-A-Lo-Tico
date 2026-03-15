package com.example.shared.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.mapbox.IMapboxRepository
import com.example.shared.domain.entities.Coordinate
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RouteDetailViewModel(
    private val mapboxRepository: IMapboxRepository
) : ViewModel() {

    private val _selectedRouteId = MutableStateFlow("")
    val selectedRouteId: StateFlow<String> = _selectedRouteId.asStateFlow()

    private val _polylinePoints = MutableStateFlow<List<Coordinate>>(emptyList())
    val polylinePoints: StateFlow<List<Coordinate>> = _polylinePoints.asStateFlow()

    private val _busLocation = MutableStateFlow<Coordinate?>(null)
    val busLocation: StateFlow<Coordinate?> = _busLocation.asStateFlow()

    private var simulationJob: Job? = null

    fun updateSelectedRoute(id: String) {
        _selectedRouteId.value = id
    }
    fun loadMapboxLine(stops: List<Coordinate>) {
        if (stops.isEmpty()) return

        viewModelScope.launch {
            try {
                val line = mapboxRepository.getStreetGeometry(stops)
                _polylinePoints.value = line
                startSimulation(line)
            } catch (e: Exception) {
                _polylinePoints.value = stops
                startSimulation(stops)
            }
        }
    }

    private fun startSimulation(points: List<Coordinate>) {
        simulationJob?.cancel()

        simulationJob = viewModelScope.launch {
            if (points.isEmpty()) {
                return@launch
            }

            while (isActive) {
                for (point in points) {
                    _busLocation.value = point
                    delay(5500)
                }
            }
        }
    }
}