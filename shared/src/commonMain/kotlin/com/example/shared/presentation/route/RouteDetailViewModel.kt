package com.example.shared.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.mapbox.IMapboxRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
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

  // Estimated arrival time per stop as formatted string e.g. "08:27"
  // Index 0 = first stop (already "arrived"), index N = last stop
  private val _stopEtas = MutableStateFlow<List<String>>(emptyList())
  val stopEtas: StateFlow<List<String>> = _stopEtas.asStateFlow()

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

          coordinates.add(coordinates.first())

          loadMapboxLine(coordinates)

          // Fetch durations between stops (not including the loop-back)
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

      // durations[i] = seconds from stop i to stop i+1
      // We calculate absolute arrival time for each stop from now
      val now = getCurrentTimeMinutes()

      val etas = mutableListOf<String>()

      // First stop — the bus is already here (or departing)
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

    simulationJob =
        viewModelScope.launch {
          if (points.isEmpty()) return@launch

          while (isActive) {
            for (point in points) {
              _busLocation.value = point
              updateCurrentStop(point)
              delay(3000)
            }
          }
        }
  }

  private fun updateCurrentStop(busLocation: Coordinate) {
    val stops = _stops.value
    if (stops.isEmpty()) return

    val nextIndex = _currentStopIndex.value + 1

    if (nextIndex < stops.size) {
      val nextStop = stops[nextIndex]
      val distanceToNext =
          haversineDistance(
              busLocation.latitude,
              busLocation.longitude,
              nextStop.latitude,
              nextStop.longitude,
          )
      if (distanceToNext < 300) {
        _currentStopIndex.value = nextIndex
      }
    }
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

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

  private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371000.0
    val dLat = (lat2 - lat1) * PI / 180.0
    val dLon = (lon2 - lon1) * PI / 180.0
    val a =
        sin(dLat / 2).pow(2) +
            cos(lat1 * PI / 180.0) * cos(lat2 * PI / 180.0) * sin(dLon / 2).pow(2)
    return r * 2 * atan2(sqrt(a), sqrt(1 - a))
  }
}
