/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.presentation.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.driver.IDriverRepository
import com.example.shared.data.repository.notification.INotificationRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.data.repository.trip.ITripExecutionRepository
import com.example.shared.data.repository.user.IUserRepository
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.domain.entities.TripExecution
import com.example.shared.domain.entities.TripExecutionStatus
import com.example.shared.domain.entities.TripIncident
import com.example.shared.domain.entities.TripIncidentType
import com.example.shared.domain.entities.UserIncidentNotification
import com.example.shared.presentation.notification.toNotificationDisplayLabel
import com.example.shared.presentation.notification.toNotificationMessage
import com.example.shared.util.currentCostaRicaTimeMinutes
import com.example.shared.util.formatTimeToMeridiem
import kotlin.time.Clock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DriverRouteViewModel(
    private val driverRepository: IDriverRepository,
    private val routeRepository: IRouteRepository,
    private val tripExecutionRepository: ITripExecutionRepository,
    private val notificationRepository: INotificationRepository,
    private val userRepository: IUserRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(DriverRouteUiState())
  val uiState: StateFlow<DriverRouteUiState> = _uiState.asStateFlow()
  private var currentDriverId: String = ""

  fun loadRouteForDriver(driverId: String) {
    if (driverId.isBlank()) {
      _uiState.value = DriverRouteUiState(errorMessage = "No se encontró el conductor autenticado.")
      return
    }

    currentDriverId = driverId
    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

    viewModelScope.launch {
      runCatching { buildUiState(driverId) }
          .onSuccess { newState -> _uiState.value = newState }
          .onFailure {
            _uiState.value =
                DriverRouteUiState(
                    errorMessage = "No se pudo cargar la ruta del conductor.",
                )
          }
    }
  }

  fun startTrip(
      routeId: String,
      scheduleId: String,
      driverId: String,
      onSuccess: (TripExecution) -> Unit = {},
      onFailure: (String) -> Unit = {},
  ) {
    if (routeId.isBlank() || scheduleId.isBlank() || driverId.isBlank()) {
      onFailure("Faltan datos para iniciar el viaje.")
      return
    }

    _uiState.value = _uiState.value.copy(isStartingTrip = true)

    viewModelScope.launch {
      runCatching {
            tripExecutionRepository.createTripExecution(
                TripExecution(
                    routeId = routeId,
                    scheduleId = scheduleId,
                    driverId = driverId,
                    startedAt = Clock.System.now().toEpochMilliseconds(),
                )
            )
          }
          .onSuccess { tripExecution ->
            refreshDriverData()
            _uiState.value =
                _uiState.value.copy(
                    isStartingTrip = false,
                    activeTripExecutionId = tripExecution.id,
                )
            onSuccess(tripExecution)
          }
          .onFailure {
            _uiState.value = _uiState.value.copy(isStartingTrip = false)
            onFailure("No se pudo iniciar el viaje.")
          }
    }
  }

  fun endTrip(
      tripExecutionId: String,
      onSuccess: (TripExecution) -> Unit = {},
      onFailure: (String) -> Unit = {},
  ) {
    if (tripExecutionId.isBlank()) {
      onFailure("No se encontró el viaje activo.")
      return
    }

    _uiState.value = _uiState.value.copy(isEndingTrip = true)

    viewModelScope.launch {
      runCatching {
            val existingTrip =
                tripExecutionRepository.getTripExecution(tripExecutionId)
                    ?: error("No se encontró el viaje activo.")
            val endedAt = Clock.System.now().toEpochMilliseconds()
            val durationSeconds =
                ((endedAt - existingTrip.startedAt).coerceAtLeast(0L) / 1000L).toInt()
            tripExecutionRepository.updateTripExecution(
                existingTrip.copy(
                    endedAt = endedAt,
                    durationSeconds = durationSeconds,
                    status = TripExecutionStatus.COMPLETED,
                )
            )
          }
          .onSuccess { tripExecution ->
            refreshDriverData()
            _uiState.value = _uiState.value.copy(isEndingTrip = false, activeTripExecutionId = null)
            onSuccess(tripExecution)
          }
          .onFailure {
            _uiState.value = _uiState.value.copy(isEndingTrip = false)
            onFailure("No se pudo finalizar el viaje.")
          }
    }
  }

  fun resumeTrip(
      tripExecutionId: String,
      onSuccess: (TripExecution) -> Unit = {},
      onFailure: (String) -> Unit = {},
  ) {
    if (tripExecutionId.isBlank()) {
      onFailure("No se encontró el viaje detenido.")
      return
    }

    _uiState.value = _uiState.value.copy(isResumingTrip = true)

    viewModelScope.launch {
      runCatching {
            val existingTrip =
                tripExecutionRepository.getTripExecution(tripExecutionId)
                    ?: error("No se encontró el viaje detenido.")
            tripExecutionRepository.updateTripExecution(
                existingTrip.copy(
                    status = TripExecutionStatus.IN_PROGRESS,
                )
            )
          }
          .onSuccess { tripExecution ->
            refreshDriverData()
            _uiState.value = _uiState.value.copy(isResumingTrip = false)
            onSuccess(tripExecution)
          }
          .onFailure {
            _uiState.value = _uiState.value.copy(isResumingTrip = false)
            onFailure("No se pudo reanudar el viaje.")
          }
    }
  }

  fun reportIncident(
      tripExecutionId: String,
      type: TripIncidentType,
      detail: String,
      location: Coordinate?,
      onSuccess: (TripIncident) -> Unit = {},
      onFailure: (String) -> Unit = {},
  ) {
    if (tripExecutionId.isBlank()) {
      onFailure("No hay un viaje activo para reportar incidencias.")
      return
    }
    if (detail.isBlank()) {
      onFailure("Agrega un detalle para la incidencia.")
      return
    }

    _uiState.value = _uiState.value.copy(isReportingIncident = true)

    viewModelScope.launch {
      runCatching {
            val incident =
                tripExecutionRepository.addIncident(
                    tripExecutionId = tripExecutionId,
                    incident =
                        TripIncident(
                            type = type,
                            detail = detail.trim(),
                            createdAt = Clock.System.now().toEpochMilliseconds(),
                            location = location,
                        ),
                )
            val existingTrip =
                tripExecutionRepository.getTripExecution(tripExecutionId)
                    ?: error("No se encontró el viaje activo.")
            val route =
                routeRepository.getRoute(existingTrip.routeId)
                    ?: error("No se encontró la ruta asociada al viaje.")
            tripExecutionRepository.updateTripExecution(
                existingTrip.copy(
                    status = TripExecutionStatus.DELAYED,
                )
            )
            Triple(incident, existingTrip.routeId, route.name)
          }
          .onSuccess { (incident, routeId, routeName) ->
            refreshDriverData()
            _uiState.value = _uiState.value.copy(isReportingIncident = false)
            dispatchIncidentNotifications(
                tripExecutionId = tripExecutionId,
                routeId = routeId,
                routeName = routeName,
                incident = incident,
            )
            onSuccess(incident)
          }
          .onFailure {
            _uiState.value = _uiState.value.copy(isReportingIncident = false)
            onFailure("No se pudo reportar la incidencia.")
          }
    }
  }

  fun transferTrip(
      tripExecutionId: String,
      targetDriverId: String,
      onSuccess: () -> Unit = {},
      onFailure: (String) -> Unit = {},
  ) {
    if (tripExecutionId.isBlank() || targetDriverId.isBlank()) {
      onFailure("Selecciona un chofer para trasladar la ruta.")
      return
    }

    _uiState.value = _uiState.value.copy(isTransferringTrip = true)

    viewModelScope.launch {
      runCatching {
            val existingTrip =
                tripExecutionRepository.getTripExecution(tripExecutionId)
                    ?: error("No se encontró el viaje a trasladar.")
            val route =
                routeRepository.getRoute(existingTrip.routeId)
                    ?: error("No se encontró la ruta a trasladar.")
            val sourceSchedule =
                route.schedules.firstOrNull { it.id == existingTrip.scheduleId }
                    ?: error("No se encontró el horario original.")
            val transferTimestamp = Clock.System.now().toEpochMilliseconds()

            routeRepository.upsertRouteSchedule(
                routeId = route.id,
                schedule =
                    RouteSchedule(
                        driverId = targetDriverId,
                        departureTime = sourceSchedule.departureTime,
                        arrivalTime = sourceSchedule.arrivalTime,
                    ),
            )

            tripExecutionRepository.updateTripExecution(
                existingTrip.copy(
                    status = TripExecutionStatus.TRANSFERRED,
                    transferredToDriverId = targetDriverId,
                    transferredAt = transferTimestamp,
                ),
            )
          }
          .onSuccess {
            refreshDriverData()
            _uiState.value =
                _uiState.value.copy(isTransferringTrip = false, activeTripExecutionId = null)
            onSuccess()
          }
          .onFailure {
            _uiState.value = _uiState.value.copy(isTransferringTrip = false)
            onFailure("No se pudo trasladar la ruta.")
          }
    }
  }

  private suspend fun refreshDriverData() {
    if (currentDriverId.isBlank()) return
    _uiState.value = buildUiState(currentDriverId)
  }

  private suspend fun buildUiState(driverId: String): DriverRouteUiState {
    val currentDriver = driverRepository.getDriver(driverId)
    val assignedRoutes = routeRepository.getRoutesByDriverId(driverId)
    if (assignedRoutes.isEmpty()) {
      return DriverRouteUiState(
          errorMessage = "No hay una ruta asignada a este conductor.",
      )
    }

    val tripExecutions = tripExecutionRepository.getTripExecutionsByDriver(driverId)
    val currentTripExecution =
        tripExecutions
            .filter {
              it.status == TripExecutionStatus.IN_PROGRESS ||
                  it.status == TripExecutionStatus.DELAYED
            }
            .maxByOrNull { it.startedAt }
    val routeCache = assignedRoutes.associateBy { it.id }.toMutableMap()
    val selectedRoute =
        currentTripExecution
            ?.let { execution ->
              routeCache[execution.routeId] ?: routeRepository.getRoute(execution.routeId)
            }
            ?.also { route -> routeCache[route.id] = route }
            ?: chooseNextAssignedRoute(
                assignedRoutes = routeCache.values.toList(),
                tripExecutions = tripExecutions,
                driverId = driverId,
            )
            ?: routeCache.values.firstOrNull()
            ?: return DriverRouteUiState(
                errorMessage = "No hay una ruta asignada a este conductor.",
            )

    val todayKey = todayDateKey()
    val completedTodayScheduleIds =
        tripExecutions
            .filter {
              (it.status == TripExecutionStatus.COMPLETED ||
                  it.status == TripExecutionStatus.TRANSFERRED) && it.routeId == selectedRoute.id
            }
            .filter { execution -> dateKey(execution.startedAt) == todayKey }
            .map { it.scheduleId }
            .toSet()

    val transferDrivers =
        currentDriver
            ?.companyId
            ?.let { companyId -> driverRepository.getDriversByCompany(companyId) }
            .orEmpty()
            .filter { it.id != driverId }
            .sortedBy { it.name }

    val history =
        tripExecutions
            .filter {
              (it.status == TripExecutionStatus.COMPLETED && it.endedAt != null) ||
                  it.status == TripExecutionStatus.TRANSFERRED
            }
            .sortedByDescending { it.startedAt }
            .map { execution ->
              val executionRoute =
                  routeCache.getOrPut(execution.routeId) {
                    routeRepository.getRoute(execution.routeId) ?: selectedRoute
                  }
              val departureTime =
                  executionRoute.schedules
                      .firstOrNull { it.id == execution.scheduleId }
                      ?.departureTime ?: "--:--"
              val latestIncident =
                  if (execution.hadIncidents) {
                    tripExecutionRepository.getIncidents(execution.id).firstOrNull()
                  } else {
                    null
                  }
              DriverTripHistoryRecord(
                  tripExecutionId = execution.id,
                  routeName = executionRoute.name,
                  departureTime = departureTime,
                  startedAtLabel = formatCostaRicaDateTimeLabel(execution.startedAt),
                  endedAtLabel =
                      when {
                        execution.endedAt != null -> formatCostaRicaDateTimeLabel(execution.endedAt)
                        execution.transferredAt != null ->
                            formatCostaRicaDateTimeLabel(execution.transferredAt)
                        else -> "--"
                      },
                  statusLabel = execution.status.toHistoryStatusLabel(),
                  incidentTypeLabel = latestIncident?.type?.toDisplayLabel(),
                  incidentDetail = latestIncident?.detail?.takeIf { it.isNotBlank() },
              )
            }

    return DriverRouteUiState(
        route = selectedRoute,
        isLoading = false,
        errorMessage = null,
        activeTripExecutionId = currentTripExecution?.id,
        currentTripExecution = currentTripExecution,
        completedTodayScheduleIds = completedTodayScheduleIds,
        tripHistory = history,
        transferDrivers = transferDrivers,
    )
  }

  private fun formatCostaRicaDateTimeLabel(epochMilliseconds: Long): String {
    val instant = kotlin.time.Instant.fromEpochMilliseconds(epochMilliseconds)
    val dateTime = instant.toLocalDateTime(TimeZone.of("America/Costa_Rica"))
    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    val month = dateTime.monthNumber.toString().padStart(2, '0')
    val year = dateTime.year.toString()
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')
    val second = dateTime.second.toString().padStart(2, '0')
    return "$day/$month/$year $hour:$minute:$second"
  }

  private fun dateKey(epochMilliseconds: Long): String {
    val dateTime =
        kotlin.time.Instant.fromEpochMilliseconds(epochMilliseconds)
            .toLocalDateTime(TimeZone.of("America/Costa_Rica"))
    return "${dateTime.year}-${dateTime.monthNumber}-${dateTime.dayOfMonth}"
  }

  private fun todayDateKey(): String = dateKey(Clock.System.now().toEpochMilliseconds())

  private fun chooseNextAssignedRoute(
      assignedRoutes: List<Route>,
      tripExecutions: List<TripExecution>,
      driverId: String,
  ): Route? {
    val currentMinutes = currentCostaRicaTimeMinutes()
    val todayKey = todayDateKey()

    data class Candidate(
        val route: Route,
        val schedule: RouteSchedule,
        val departureMinutes: Int,
    )

    val completedByRoute =
        tripExecutions
            .filter {
              (it.status == TripExecutionStatus.COMPLETED ||
                  it.status == TripExecutionStatus.TRANSFERRED) && dateKey(it.startedAt) == todayKey
            }
            .groupBy { it.routeId }
            .mapValues { (_, executions) -> executions.map { it.scheduleId }.toSet() }

    val candidates =
        assignedRoutes.mapNotNull { route ->
          val completedSchedules = completedByRoute[route.id].orEmpty()
          val remainingSchedules =
              route.schedules
                  .filter { it.driverId == driverId && it.id !in completedSchedules }
                  .sortedBy { parseTimeToMinutes(it.departureTime) }

          val preferredSchedule =
              remainingSchedules.firstOrNull {
                parseTimeToMinutes(it.departureTime) >= currentMinutes
              } ?: remainingSchedules.firstOrNull() ?: return@mapNotNull null

          Candidate(
              route = route,
              schedule = preferredSchedule,
              departureMinutes = parseTimeToMinutes(preferredSchedule.departureTime),
          )
        }

    return candidates
        .sortedWith(
            compareBy<Candidate> { if (it.departureMinutes >= currentMinutes) 0 else 1 }
                .thenBy {
                  if (it.departureMinutes >= currentMinutes) it.departureMinutes else Int.MAX_VALUE
                }
                .thenBy {
                  if (it.departureMinutes < currentMinutes) it.departureMinutes else Int.MAX_VALUE
                }
        )
        .firstOrNull()
        ?.route
  }

  private fun parseTimeToMinutes(value: String): Int {
    val parts = value.split(":")
    val hours = parts.getOrNull(0)?.toIntOrNull() ?: return 0
    val minutes = parts.getOrNull(1)?.toIntOrNull() ?: return 0
    return hours * 60 + minutes
  }

  private fun TripIncidentType.toDisplayLabel(): String {
    return toNotificationDisplayLabel()
  }

  private fun TripExecutionStatus.toHistoryStatusLabel(): String {
    return when (this) {
      TripExecutionStatus.TRANSFERRED -> "TRASLADADO"
      TripExecutionStatus.COMPLETED -> "COMPLETADO"
      TripExecutionStatus.DELAYED -> "DETENIDO"
      TripExecutionStatus.IN_PROGRESS -> "EN CURSO"
      TripExecutionStatus.PENDING -> "PENDIENTE"
      TripExecutionStatus.CANCELLED -> "CANCELADO"
    }
  }

  private fun dispatchIncidentNotifications(
      tripExecutionId: String,
      routeId: String,
      routeName: String,
      incident: TripIncident,
  ) {
    val now = Clock.System.now().toEpochMilliseconds()

    viewModelScope.launch {
      val normalizedRouteId = routeId.trim()
      val tripExecution = tripExecutionRepository.getTripExecution(tripExecutionId)
      val route = routeRepository.getRoute(normalizedRouteId)
      val scheduledTimeLabel =
          tripExecution
              ?.scheduleId
              ?.let { scheduleId ->
                route?.schedules?.firstOrNull { it.id == scheduleId }?.departureTime
              }
              ?.let(::formatTimeToMeridiem)
      val targets =
          (userRepository.getPassengerIdsByFavoriteRoute(normalizedRouteId))
              .filter { it.isNotBlank() }
              .distinct()

      targets.forEach { userId ->
        val normalizedRouteName = routeName.trim()
        notificationRepository.addNotification(
            UserIncidentNotification(
                userId = userId,
                tripExecutionId = tripExecutionId,
                routeId = normalizedRouteId,
                incidentType = incident.type.toNotificationDisplayLabel(),
                detail = incident.detail,
                sanitizedDetail =
                    incident.type.toNotificationMessage(normalizedRouteName, scheduledTimeLabel),
                routeName = normalizedRouteName,
                dispatchedAt = now,
                createdAt = now,
            )
        )
      }
    }
  }
}
