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
package com.example.sharedlogic.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedlogic.data.repository.mapbox.IMapboxRepository
import com.example.sharedlogic.data.repository.route.IRouteRepository
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.entities.RouteStop
import kotlin.time.Clock
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class RouteDetailViewModel(
    private val mapboxRepository: IMapboxRepository,
    private val routeRepository: IRouteRepository,
) : ViewModel() {

  private val _selectedRouteId = MutableStateFlow("")
  val selectedRouteId: StateFlow<String> = _selectedRouteId.asStateFlow()

  private val _routeName = MutableStateFlow("")
  val routeName: StateFlow<String> = _routeName.asStateFlow()

  private val _polylinePoints = MutableStateFlow<List<Coordinate>>(emptyList())
  val polylinePoints: StateFlow<List<Coordinate>> = _polylinePoints.asStateFlow()

  private val _stops = MutableStateFlow<List<RouteStop>>(emptyList())
  val stops: StateFlow<List<RouteStop>> = _stops.asStateFlow()

  private val _busLocation = MutableStateFlow<Coordinate?>(null)
  val busLocation: StateFlow<Coordinate?> = _busLocation.asStateFlow()

  private val _currentStopIndex = MutableStateFlow(0)
  val currentStopIndex: StateFlow<Int> = _currentStopIndex.asStateFlow()

  private val _stopEtas = MutableStateFlow<List<String>>(emptyList())
  val stopEtas: StateFlow<List<String>> = _stopEtas.asStateFlow()

  private val _isSimulationCompleted = MutableStateFlow(false)
  val isSimulationCompleted: StateFlow<Boolean> = _isSimulationCompleted.asStateFlow()

  private val ARRIVAL_THRESHOLD_METERS = 300.0
  private var simulationJob: Job? = null

  fun loadStops(routeId: String) {
    _selectedRouteId.value = routeId

    viewModelScope.launch {
      try {
        val route = routeRepository.getRoute(routeId)

        if (route != null) {
          _stops.value = route.stops
          _routeName.value = route.name
          _currentStopIndex.value = 0

          val coordinates =
              route.stops.map { Coordinate(it.latitude, it.longitude) }.toMutableList()

          loadMapboxLine(coordinates)
          loadEtas(route.stops.map { Coordinate(it.latitude, it.longitude) })
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  private suspend fun loadEtas(stopCoordinates: List<Coordinate>) {
    if (stopCoordinates.size < 2) return

    try {
      val durations = mapboxRepository.getStopDurations(stopCoordinates)
      val now = getCurrentTimeMinutes()
      val etas = mutableListOf<String>()

      etas.add(formatMinutes(now))

      var accumulated = 0.0
      for (i in durations.indices) {
        accumulated += durations[i]
        etas.add(formatMinutes(now + (accumulated / 60).toInt()))
      }

      _stopEtas.value = etas
    } catch (e: Exception) {
      _stopEtas.value = emptyList()
    }
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
    _isSimulationCompleted.value = false

    simulationJob =
        viewModelScope.launch {
          if (points.isEmpty()) return@launch

          for (point in points) {
            if (!isActive) break
            _busLocation.value = point
            updateCurrentStop(point)
            delay(3000)
          }

          _isSimulationCompleted.value = true
        }
  }

  private fun updateCurrentStop(busLocation: Coordinate) {
    val stops = _stops.value
    val currentIndex = _currentStopIndex.value

    for (index in (currentIndex + 1) until stops.size) {
      val stop = stops[index]
      val stopCoordinate = Coordinate(stop.latitude, stop.longitude)
      val distanceMeters = busLocation.distanceTo(stopCoordinate) * 1000.0

      if (distanceMeters < ARRIVAL_THRESHOLD_METERS) {
        _currentStopIndex.value = index
        break
      }
    }
  }

  fun clearSimulation() {
    simulationJob?.cancel()
    _isSimulationCompleted.value = false
    _selectedRouteId.value = ""
    _routeName.value = ""
    _polylinePoints.value = emptyList()
    _stops.value = emptyList()
    _busLocation.value = null
    _currentStopIndex.value = 0
    _stopEtas.value = emptyList()
  }

  // ── Helpers ─────────────────────────────────────────────────────────────

  private fun getCurrentTimeMinutes(): Int {
    val now = Clock.System.now()
    val tz = TimeZone.of("America/Costa_Rica")
    val local = now.toLocalDateTime(tz)
    return local.hour * 60 + local.minute
  }

  private fun formatMinutes(totalMinutes: Int): String {
    val clamped = ((totalMinutes % (24 * 60)) + 24 * 60) % (24 * 60)
    val h = clamped / 60
    val m = clamped % 60
    return "${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}"
  }
}
