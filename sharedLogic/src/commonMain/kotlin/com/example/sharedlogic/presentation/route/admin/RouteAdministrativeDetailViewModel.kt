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
package com.example.sharedlogic.presentation.route.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedlogic.data.repository.driver.IDriverRepository
import com.example.sharedlogic.data.repository.route.IRouteRepository
import com.example.sharedlogic.data.repository.trip.ITripExecutionRepository
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.TripExecution
import com.example.sharedlogic.domain.entities.enums.TripExecutionStatus
import com.example.sharedlogic.util.currentCostaRicaTimeMinutes
import com.example.sharedlogic.util.parseFlexibleTimeToMinutes
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

class RouteAdministrativeDetailViewModel(
    private val routeRepository: IRouteRepository,
    private val driverRepository: IDriverRepository,
    private val tripExecutionRepository: ITripExecutionRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(RouteAdministrativeDetailUiState())
  val uiState: StateFlow<RouteAdministrativeDetailUiState> = _uiState.asStateFlow()

  fun loadRoute(routeId: String) {
    if (routeId.isBlank()) {
      _uiState.value =
          RouteAdministrativeDetailUiState(
              errorMessage = "No se encontró la ruta seleccionada.",
          )
      return
    }

    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

    viewModelScope.launch {
      runCatching { routeRepository.getRoute(routeId) }
          .onSuccess { route ->
            _uiState.value =
                if (route != null) {
                  val driverIds =
                      route.schedules.map { it.driverId }.filter { it.isNotBlank() }.distinct()

                  val driverNamesById =
                      driverIds
                          .map { driverId ->
                            async {
                              driverId to
                                  driverRepository.getDriver(driverId)?.name?.takeIf {
                                    it.isNotBlank()
                                  }
                            }
                          }
                          .awaitAll()
                          .mapNotNull { (driverId, name) -> name?.let { driverId to it } }
                          .toMap()

                  val tripExecutions =
                      driverIds
                          .map { driverId ->
                            async { tripExecutionRepository.getTripExecutionsByDriver(driverId) }
                          }
                          .awaitAll()
                          .flatten()

                  val runtimeSnapshot = buildRuntimeSnapshot(route, tripExecutions)

                  RouteAdministrativeDetailUiState(
                      route = route,
                      driverNamesById = driverNamesById,
                      routeRuntimeStatus = runtimeSnapshot.routeStatus,
                      scheduleStatusesById = runtimeSnapshot.scheduleStatusesById,
                      highlightedScheduleId = runtimeSnapshot.highlightedScheduleId,
                  )
                } else {
                  RouteAdministrativeDetailUiState(
                      errorMessage = "No se encontró la ficha administrativa de esta ruta.",
                  )
                }
          }
          .onFailure {
            _uiState.value =
                RouteAdministrativeDetailUiState(
                    errorMessage = "No se pudo cargar la ficha administrativa.",
                )
          }
    }
  }

  private data class RuntimeSnapshot(
      val routeStatus: AdministrativeRouteRuntimeStatus,
      val scheduleStatusesById: Map<String, AdministrativeScheduleRuntimeStatus>,
      val highlightedScheduleId: String?,
  )

  private fun buildRuntimeSnapshot(
      route: Route,
      tripExecutions: List<TripExecution>,
  ): RuntimeSnapshot {
    val todayKey = todayDateKey()
    val routeScheduleIds = route.schedules.map { it.id }.toSet()
    val routeExecutionsToday =
        tripExecutions
            .filter { it.routeId == route.id && it.scheduleId in routeScheduleIds }
            .filter { dateKey(it.startedAt) == todayKey }

    val latestBySchedule =
        routeExecutionsToday
            .groupBy { it.scheduleId }
            .mapValues { (_, executions) -> executions.maxByOrNull { it.startedAt } }
            .mapNotNull { (scheduleId, execution) -> execution?.let { scheduleId to it } }
            .toMap()

    val scheduleStatusesById =
        latestBySchedule.mapValues { (_, execution) ->
          when (execution.status) {
            TripExecutionStatus.IN_PROGRESS -> AdministrativeScheduleRuntimeStatus.ACTIVE
            TripExecutionStatus.DELAYED -> AdministrativeScheduleRuntimeStatus.DELAYED
            TripExecutionStatus.COMPLETED,
            TripExecutionStatus.TRANSFERRED -> AdministrativeScheduleRuntimeStatus.COMPLETED
            TripExecutionStatus.PENDING,
            TripExecutionStatus.CANCELLED -> AdministrativeScheduleRuntimeStatus.SCHEDULED
          }
        }

    val currentExecution =
        routeExecutionsToday
            .filter {
              it.status == TripExecutionStatus.IN_PROGRESS ||
                  it.status == TripExecutionStatus.DELAYED
            }
            .maxByOrNull { it.startedAt }

    val routeStatus =
        when (currentExecution?.status) {
          TripExecutionStatus.IN_PROGRESS ->
              AdministrativeRouteRuntimeStatus(
                  state = AdministrativeRouteStatus.ACTIVE,
                  detail = "Viaje iniciado por el conductor",
              )
          TripExecutionStatus.DELAYED ->
              AdministrativeRouteRuntimeStatus(
                  state = AdministrativeRouteStatus.DELAYED,
                  detail = "Ruta detenida por incidencia reportada",
              )
          else -> {
            val hasCompletedTrip =
                routeExecutionsToday.any {
                  it.status == TripExecutionStatus.COMPLETED ||
                      it.status == TripExecutionStatus.TRANSFERRED
                }
            AdministrativeRouteRuntimeStatus(
                state = AdministrativeRouteStatus.IDLE,
                detail =
                    if (hasCompletedTrip) "Último viaje completado"
                    else "Esperando inicio del conductor",
            )
          }
        }

    val highlightedScheduleId =
        currentExecution?.scheduleId
            ?: chooseNextSchedule(route.schedules, scheduleStatusesById)?.id

    return RuntimeSnapshot(
        routeStatus = routeStatus,
        scheduleStatusesById = scheduleStatusesById,
        highlightedScheduleId = highlightedScheduleId,
    )
  }

  private fun chooseNextSchedule(
      schedules: List<RouteSchedule>,
      scheduleStatusesById: Map<String, AdministrativeScheduleRuntimeStatus>,
  ): RouteSchedule? {
    val currentMinutes = currentCostaRicaTimeMinutes()
    val remainingSchedules =
        schedules
            .filter { scheduleStatusesById[it.id] != AdministrativeScheduleRuntimeStatus.COMPLETED }
            .sortedBy { parseFlexibleTimeToMinutes(it.departureTime) ?: Int.MAX_VALUE }

    return remainingSchedules.firstOrNull {
      (parseFlexibleTimeToMinutes(it.departureTime) ?: Int.MAX_VALUE) >= currentMinutes
    } ?: remainingSchedules.firstOrNull()
  }

  private fun dateKey(epochMilliseconds: Long): String {
    val dateTime =
        Instant.fromEpochMilliseconds(epochMilliseconds)
            .toLocalDateTime(TimeZone.of("America/Costa_Rica"))
    return "${dateTime.year}-${dateTime.month.number}-${dateTime.day}"
  }

  private fun todayDateKey(): String = dateKey(Clock.System.now().toEpochMilliseconds())
}
