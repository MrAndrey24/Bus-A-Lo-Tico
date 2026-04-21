/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.auth.IAuthRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.data.repository.trip.ITripExecutionRepository
import com.example.shared.data.repository.user.IUserRepository
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.TripExecutionStatus
import com.example.shared.domain.usecases.route.SearchRoutesByLocationUseCase
import kotlin.time.Clock
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class RouteViewModel(
    private val routeRepository: IRouteRepository,
    private val tripExecutionRepository: ITripExecutionRepository,
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val searchRoutesByLocationUseCase: SearchRoutesByLocationUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(RouteUiState())
  val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()
  private var activeUserId: String? = null
  private var searchJob: Job? = null

  init {
    loadRoutes()
    onUserChanged(authRepository.getCurrentUserId())
  }

  fun loadRoutes() {
    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isLoading = true, routes = emptyList(), errorMessage = null)
      try {
        val routes = routeRepository.getRoutes()
        val routeRuntimeById = buildRouteRuntimeById(routes)
        _uiState.value =
            _uiState.value.copy(
                isLoading = false,
                routes = routes,
                routeRuntimeById = routeRuntimeById,
                errorMessage = null,
            )
      } catch (e: Exception) {
        _uiState.value =
            _uiState.value.copy(
                isLoading = false,
                routes = emptyList(),
                routeRuntimeById = emptyMap(),
                errorMessage = e.message ?: "Error loading routes",
            )
      }
    }
  }

  fun onSearchQueryChanged(newQuery: String) {
    _uiState.value = _uiState.value.copy(searchQuery = newQuery)

    searchJob?.cancel()

    if (newQuery.isBlank()) {
      _uiState.value = _uiState.value.copy(searchResults = emptyList(), isSearching = false)
      return
    }

    searchJob =
        viewModelScope.launch {
          delay(500)
          _uiState.value = _uiState.value.copy(isSearching = true)

          try {
            val results = searchRoutesByLocationUseCase(newQuery)
            _uiState.value = _uiState.value.copy(searchResults = results, isSearching = false)
          } catch (e: Exception) {
            _uiState.value =
                _uiState.value.copy(
                    isSearching = false,
                    errorMessage = e.message ?: "Error searching routes",
                )
          }
        }
  }

  fun onRouteSelected(route: Route) {
    searchJob?.cancel()
    _uiState.value =
        _uiState.value.copy(
            searchQuery = route.name,
            searchResults = emptyList(),
            isSearching = false,
        )
  }

  fun onUserChanged(userId: String?) {
    if (activeUserId == userId) return

    activeUserId = userId
    _uiState.value = _uiState.value.copy(favoriteRouteIds = emptySet(), errorMessage = null)

    loadFavoriteRouteIds(userId)
  }

  private fun loadFavoriteRouteIds(userId: String?) {
    val currentUserId = userId ?: return

    viewModelScope.launch {
      runCatching { userRepository.getFavoriteRouteIds(currentUserId) }
          .onSuccess { favoriteRouteIds ->
            _uiState.value =
                _uiState.value.copy(favoriteRouteIds = favoriteRouteIds, errorMessage = null)
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    errorMessage = error.message ?: "No se pudieron cargar las favoritas"
                )
          }
    }
  }

  fun toggleFavorite(routeId: String, isFavorite: Boolean) {
    val currentUserId = authRepository.getCurrentUserId() ?: return

    viewModelScope.launch {
      runCatching { userRepository.setRouteFavorite(currentUserId, routeId, isFavorite) }
          .onSuccess {
            val updatedFavoriteRouteIds =
                if (isFavorite) {
                  _uiState.value.favoriteRouteIds + routeId
                } else {
                  _uiState.value.favoriteRouteIds - routeId
                }

            _uiState.value =
                _uiState.value.copy(favoriteRouteIds = updatedFavoriteRouteIds, errorMessage = null)
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    errorMessage = error.message ?: "No se pudo actualizar la favorita"
                )
          }
    }
  }

  private suspend fun buildRouteRuntimeById(routes: List<Route>): Map<String, RouteRuntimeSummary> {
    val driverIds =
        routes
            .flatMap { route -> route.schedules.map { it.driverId } }
            .filter { it.isNotBlank() }
            .distinct()

    if (driverIds.isEmpty()) return emptyMap()

    val executionsByDriver =
        driverIds.associateWith { driverId ->
          runCatching { tripExecutionRepository.getTripExecutionsByDriver(driverId) }
              .getOrDefault(emptyList())
        }

    val todayKey = todayDateKey()

    return routes.associate { route ->
      val routeScheduleIds = route.schedules.map { it.id }.toSet()
      val routeExecutionsToday =
          route.schedules
              .mapNotNull { schedule -> executionsByDriver[schedule.driverId] }
              .flatten()
              .filter { execution ->
                execution.routeId == route.id &&
                    execution.scheduleId in routeScheduleIds &&
                    dateKey(execution.startedAt) == todayKey
              }

      val activeExecution =
          routeExecutionsToday
              .filter {
                it.status == TripExecutionStatus.IN_PROGRESS ||
                    it.status == TripExecutionStatus.DELAYED
              }
              .maxByOrNull { it.startedAt }

      val runtime =
          when (activeExecution?.status) {
            TripExecutionStatus.IN_PROGRESS ->
                RouteRuntimeSummary(
                    status = RouteRuntimeStatus.ACTIVE,
                    statusText = "ACTIVA",
                )
            TripExecutionStatus.DELAYED ->
                RouteRuntimeSummary(
                    status = RouteRuntimeStatus.DELAYED,
                    statusText = "DETENIDA",
                )
            else ->
                RouteRuntimeSummary(
                    status = RouteRuntimeStatus.IDLE,
                    statusText = "EN ESPERA",
                )
          }

      route.id to runtime
    }
  }

  private fun dateKey(epochMilliseconds: Long): String {
    val dateTime =
        kotlin.time.Instant.fromEpochMilliseconds(epochMilliseconds)
            .toLocalDateTime(TimeZone.of("America/Costa_Rica"))
    return "${dateTime.year}-${dateTime.monthNumber}-${dateTime.dayOfMonth}"
  }

  private fun todayDateKey(): String = dateKey(Clock.System.now().toEpochMilliseconds())
}
