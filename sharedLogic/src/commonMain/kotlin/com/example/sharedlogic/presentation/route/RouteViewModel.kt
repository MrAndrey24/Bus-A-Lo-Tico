/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedlogic.data.repository.auth.IAuthRepository
import com.example.sharedlogic.data.repository.route.IRouteRepository
import com.example.sharedlogic.data.repository.trip.ITripExecutionRepository
import com.example.sharedlogic.data.repository.user.IUserRepository
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.enums.TripExecutionStatus
import com.example.sharedlogic.domain.usecases.route.SearchRoutesByLocationUseCase
import com.example.sharedlogic.presentation.route.uistate.RouteRuntimeStatus
import com.example.sharedlogic.presentation.route.uistate.RouteRuntimeSummary
import com.example.sharedlogic.presentation.route.uistate.RouteUiState
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
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

    searchJob = viewModelScope.launch {
      delay(500.milliseconds)
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
      userRepository
          .getFavoriteRouteIds(currentUserId)
          .catch { error ->
            _uiState.value =
                _uiState.value.copy(errorMessage = error.message ?: "Error al cargar favoritas")
          }
          .collect { favoriteRouteIds ->
            _uiState.value =
                _uiState.value.copy(favoriteRouteIds = favoriteRouteIds, errorMessage = null)
          }
    }
  }

  fun toggleFavorite(routeId: String, isFavorite: Boolean) {
    val currentUserId = authRepository.getCurrentUserId() ?: return

    viewModelScope.launch {
      val previousFavorites = _uiState.value.favoriteRouteIds
      val updatedFavoriteRouteIds =
          if (isFavorite) {
            previousFavorites + routeId
          } else {
            previousFavorites - routeId
          }

      _uiState.value =
          _uiState.value.copy(favoriteRouteIds = updatedFavoriteRouteIds, errorMessage = null)

      runCatching { userRepository.setRouteFavorite(currentUserId, routeId, isFavorite) }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    favoriteRouteIds = previousFavorites,
                    errorMessage = error.message ?: "No se pudo actualizar la favorita",
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

    val executionsByDriver = driverIds.associateWith { driverId ->
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
    return "${dateTime.year}-${dateTime.month.number}-${dateTime.day}"
  }

  private fun todayDateKey(): String = dateKey(Clock.System.now().toEpochMilliseconds())
}
