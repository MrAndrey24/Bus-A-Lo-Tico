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
package com.example.sharedlogic.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedlogic.data.repository.auth.IAuthRepository
import com.example.sharedlogic.data.repository.company.ICompanyRepository
import com.example.sharedlogic.data.repository.driver.IDriverRepository
import com.example.sharedlogic.data.repository.route.IRouteRepository
import com.example.sharedlogic.data.repository.trip.ITripExecutionRepository
import com.example.sharedlogic.data.repository.user.IUserRepository
import com.example.sharedlogic.domain.entities.Driver
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.RouteStop
import com.example.sharedlogic.domain.entities.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminCompanyViewModel(
    private val authRepository: IAuthRepository,
    private val driverRepository: IDriverRepository,
    private val userRepository: IUserRepository,
    private val routeRepository: IRouteRepository,
    private val companyRepository: ICompanyRepository,
    private val tripExecutionRepository: ITripExecutionRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(AdminCompanyUiState())
  val uiState: StateFlow<AdminCompanyUiState> = _uiState.asStateFlow()
  private var companyScopeId: String? = null

  init {
    refresh()
  }

  fun setCompanyScope(companyId: String?) {
    val normalizedCompanyId = companyId?.trim().takeUnless { it.isNullOrBlank() }
    if (companyScopeId == normalizedCompanyId) return
    companyScopeId = normalizedCompanyId
    refresh(selectedRouteId = null)
  }

  fun refresh(selectedRouteId: String? = _uiState.value.selectedRoute?.id) {
    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)
      runCatching { buildUiState(selectedRouteId = selectedRouteId) }
          .onSuccess { state -> _uiState.value = state }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo cargar el panel administrativo.",
                )
          }
    }
  }

  fun selectRoute(routeId: String) {
    refresh(routeId)
  }

  fun dismissMessages() {
    _uiState.value = _uiState.value.copy(successMessage = null, errorMessage = null)
  }

  fun saveRoute(route: Route) {
    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isSavingRoute = true, errorMessage = null, successMessage = null)
      runCatching {
            routeRepository.upsertRoute(
                if (companyScopeId != null) route.copy(companyId = companyScopeId) else route
            )
          }
          .onSuccess { savedRoute ->
            _uiState.value =
                buildUiState(
                    selectedRouteId = savedRoute.id,
                    isSavingRoute = false,
                    successMessage = "Ruta guardada correctamente.",
                )
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isSavingRoute = false,
                    errorMessage = error.message ?: "No se pudo guardar la ruta.",
                )
          }
    }
  }

  fun saveSchedule(routeId: String, schedule: RouteSchedule) {
    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isSavingSchedule = true, errorMessage = null, successMessage = null)
      runCatching { routeRepository.upsertRouteSchedule(routeId, schedule) }
          .onSuccess {
            _uiState.value =
                buildUiState(
                    selectedRouteId = routeId,
                    isSavingSchedule = false,
                    successMessage = "Horario guardado correctamente.",
                )
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isSavingSchedule = false,
                    errorMessage = error.message ?: "No se pudo guardar el horario.",
                )
          }
    }
  }

  fun deleteSchedule(routeId: String, scheduleId: String) {
    if (scheduleId.isBlank()) return
    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isSavingSchedule = true, errorMessage = null, successMessage = null)
      runCatching { routeRepository.deleteRouteSchedule(routeId, scheduleId) }
          .onSuccess {
            _uiState.value =
                buildUiState(
                    selectedRouteId = routeId,
                    isSavingSchedule = false,
                    successMessage = "Horario eliminado correctamente.",
                )
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isSavingSchedule = false,
                    errorMessage = error.message ?: "No se pudo eliminar el horario.",
                )
          }
    }
  }

  fun saveStop(routeId: String, stop: RouteStop) {
    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isSavingStop = true, errorMessage = null, successMessage = null)
      runCatching { routeRepository.upsertRouteStop(routeId, stop) }
          .onSuccess {
            _uiState.value =
                buildUiState(
                    selectedRouteId = routeId,
                    isSavingStop = false,
                    successMessage = "Parada guardada correctamente.",
                )
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isSavingStop = false,
                    errorMessage = error.message ?: "No se pudo guardar la parada.",
                )
          }
    }
  }

  fun deleteStop(routeId: String, stopId: String) {
    if (stopId.isBlank()) return
    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isSavingStop = true, errorMessage = null, successMessage = null)
      runCatching { routeRepository.deleteRouteStop(routeId, stopId) }
          .onSuccess {
            _uiState.value =
                buildUiState(
                    selectedRouteId = routeId,
                    isSavingStop = false,
                    successMessage = "Parada eliminada correctamente.",
                )
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isSavingStop = false,
                    errorMessage = error.message ?: "No se pudo eliminar la parada.",
                )
          }
    }
  }

  fun moveStop(routeId: String, stopId: String, direction: Int) {
    val route = _uiState.value.selectedRoute ?: return
    val stops = route.stops.sortedBy { it.order }
    val index = stops.indexOfFirst { it.id == stopId }
    val targetIndex = index + direction
    if (index !in stops.indices || targetIndex !in stops.indices) return

    val current = stops[index]
    val target = stops[targetIndex]

    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isSavingStop = true, errorMessage = null, successMessage = null)
      runCatching {
            routeRepository.upsertRouteStop(routeId, current.copy(order = target.order))
            routeRepository.upsertRouteStop(routeId, target.copy(order = current.order))
          }
          .onSuccess {
            _uiState.value =
                buildUiState(
                    selectedRouteId = routeId,
                    isSavingStop = false,
                    successMessage = "Se actualizó el orden de las paradas.",
                )
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isSavingStop = false,
                    errorMessage = error.message ?: "No se pudo reordenar la parada.",
                )
          }
    }
  }

  fun saveDriver(name: String, email: String, password: String, companyId: String?) {
    viewModelScope.launch {
      runCatching {
            _uiState.value =
                _uiState.value.copy(
                    isSavingDriver = true,
                    errorMessage = null,
                    successMessage = null,
                )

            val userId = authRepository.signUp(email, password)
            userRepository.upsertUserProfile(
                userId = userId,
                email = email,
                name = name.substringBefore("@"),
                role = UserRole.DRIVER,
                companyId = companyId,
            )
            driverRepository.upsertDriver(
                driverId = userId.trim(),
                name = name.trim(),
                companyId = companyId?.trim().takeUnless { it.isNullOrBlank() },
            )
          }
          .onSuccess {
            _uiState.value =
                buildUiState(
                    selectedRouteId = _uiState.value.selectedRoute?.id,
                    isSavingDriver = false,
                    successMessage = "Conductor guardado correctamente.",
                )
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isSavingDriver = false,
                    errorMessage = error.message ?: "No se pudo guardar el conductor.",
                )
          }
    }
  }

  fun deleteDriver(driverId: String) {
    if (driverId.isBlank()) return
    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isSavingDriver = true, errorMessage = null, successMessage = null)
      runCatching { driverRepository.deleteDriver(driverId) }
          .onSuccess {
            _uiState.value =
                buildUiState(
                    selectedRouteId = _uiState.value.selectedRoute?.id,
                    isSavingDriver = false,
                    successMessage = "Conductor eliminado correctamente.",
                )
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isSavingDriver = false,
                    errorMessage = error.message ?: "No se pudo eliminar el conductor.",
                )
          }
    }
  }

  private suspend fun loadCompanyNames(routes: List<Route>): Map<String, String> {
    return routes
        .mapNotNull { it.companyId }
        .distinct()
        .associateWith { companyId -> companyRepository.getCompany(companyId)?.name ?: companyId }
  }

  private suspend fun loadDrivers(selectedRoute: Route?): List<Driver> {
    val companyId = selectedRoute?.companyId ?: return emptyList()
    return driverRepository.getDriversByCompany(companyId).sortedBy { it.name }
  }

  private suspend fun loadRecentIncidents(
      routes: List<Route>,
      drivers: List<Driver>,
  ): List<AdminRouteIncidentSummary> {
    if (routes.isEmpty() || drivers.isEmpty()) return emptyList()

    val routeNamesById = routes.associate { it.id to it.name }
    val driverNamesById = drivers.associate { it.id to it.name }
    val summaries = mutableListOf<AdminRouteIncidentSummary>()

    drivers.forEach { driver ->
      val tripExecutions = tripExecutionRepository.getTripExecutionsByDriver(driver.id)
      tripExecutions
          .filter { it.routeId in routeNamesById.keys && it.hadIncidents }
          .forEach { tripExecution ->
            val latestIncident =
                tripExecutionRepository.getIncidents(tripExecution.id).firstOrNull()
                    ?: return@forEach
            summaries +=
                AdminRouteIncidentSummary(
                    tripExecutionId = tripExecution.id,
                    incidentId = latestIncident.id,
                    routeName = routeNamesById[tripExecution.routeId] ?: tripExecution.routeId,
                    driverName = driverNamesById[tripExecution.driverId] ?: tripExecution.driverId,
                    incidentType = latestIncident.type,
                    incidentDetail = latestIncident.detail,
                    createdAt = latestIncident.createdAt,
                )
          }
    }

    return summaries.sortedByDescending { it.createdAt }.take(6)
  }

  fun deleteIncident(tripExecutionId: String, incidentId: String) {
    if (tripExecutionId.isBlank() || incidentId.isBlank()) return
    viewModelScope.launch {
      _uiState.value =
          _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)
      runCatching { tripExecutionRepository.deleteIncident(tripExecutionId, incidentId) }
          .onSuccess {
            _uiState.value =
                buildUiState(
                    selectedRouteId = _uiState.value.selectedRoute?.id,
                    successMessage = "Incidencia eliminada correctamente.",
                )
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo eliminar la incidencia.",
                )
          }
    }
  }

  private suspend fun buildUiState(
      selectedRouteId: String?,
      isSavingRoute: Boolean = false,
      isSavingSchedule: Boolean = false,
      isSavingStop: Boolean = false,
      isSavingDriver: Boolean = false,
      successMessage: String? = null,
  ): AdminCompanyUiState {
    val routes =
        routeRepository
            .getRoutes()
            .filter { route -> companyScopeId == null || route.companyId == companyScopeId }
            .sortedWith(compareBy<Route> { it.code }.thenBy { it.name })
    val selectedId =
        when {
          selectedRouteId != null && routes.any { it.id == selectedRouteId } -> selectedRouteId
          routes.isNotEmpty() -> routes.first().id
          else -> null
        }
    val selectedRoute = selectedId?.let { routeRepository.getRoute(it) }
    val companyNames = loadCompanyNames(routes)
    val drivers = loadDrivers(selectedRoute)
    val companyDrivers =
        when (val companyId = companyScopeId ?: selectedRoute?.companyId) {
          null -> drivers
          else -> driverRepository.getDriversByCompany(companyId).sortedBy { it.name }
        }
    val recentIncidents = loadRecentIncidents(routes, companyDrivers)

    return AdminCompanyUiState(
        isLoading = false,
        routes = routes,
        selectedRoute = selectedRoute,
        companyNames = companyNames,
        companyDrivers = companyDrivers,
        recentIncidents = recentIncidents,
        isSavingRoute = isSavingRoute,
        isSavingSchedule = isSavingSchedule,
        isSavingStop = isSavingStop,
        isSavingDriver = isSavingDriver,
        successMessage = successMessage,
    )
  }
}
