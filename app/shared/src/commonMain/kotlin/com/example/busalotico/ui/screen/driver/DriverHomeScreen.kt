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
package com.example.busalotico.ui.screen.driver

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busalotico.ui.components.button.PillActionButton
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.entities.Driver
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.enums.TripExecutionStatus
import com.example.sharedlogic.domain.entities.enums.TripIncidentType
import com.example.sharedlogic.presentation.driver.DriverRouteViewModel
import com.example.sharedlogic.presentation.driver.DriverTripHistoryRecord
import com.example.sharedlogic.util.currentCostaRicaTimeMinutes
import com.example.sharedlogic.util.parseFlexibleTimeToMinutes
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

private val DriverPageBackground = Color(0xFFF7F8FA)
private val DriverCardBackground = Color.White
private val DriverSoftPanelBackground = Color(0xFFFAFBFD)
private val DriverSoftBorder = Color.Transparent
private val DriverBorderWidth = 0.dp
private val DriverTabContainerBackground = Color(0xFFF1F3F6)

private enum class DriverPanelTab {
  ACTIVE_ROUTE,
  TRAVEL_HISTORY,
}

private enum class DriverNoticeTone {
  INFO,
  WARNING,
  ERROR,
}

private data class DriverNotice(
    val message: String,
    val tone: DriverNoticeTone,
)

private data class IncidentDialogContext(
    val type: TripIncidentType,
    val routeName: String,
    val locationLabel: String,
    val reportedAtLabel: String,
)

@Composable
fun DriverHomeScreen(
    coordinate: Coordinate?,
    userId: String,
    userName: String,
    viewModel: DriverRouteViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsState()

  LaunchedEffect(userId) { viewModel.loadRouteForDriver(userId) }

  Surface(modifier = modifier.fillMaxSize(), color = DriverPageBackground) {
    Column(modifier = Modifier.fillMaxSize()) {
      ScreenHeader(
          title = "Panel de conductor",
          subtitle = userName.ifBlank { "Conductor" },
      )

      Column(
          modifier =
              Modifier.fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 12.dp)
                  .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        when {
          uiState.isLoading -> DriverLoadingCard()
          uiState.route != null ->
              DriverDashboardContent(
                  route = uiState.route!!,
                  userId = userId,
                  activeTripExecutionId = uiState.activeTripExecutionId,
                  activeTripScheduleId = uiState.currentTripExecution?.scheduleId,
                  currentTripStartedAt = uiState.currentTripExecution?.startedAt,
                  completedTodayScheduleIds = uiState.completedTodayScheduleIds,
                  history = uiState.tripHistory,
                  transferDrivers = uiState.transferDrivers,
                  isStartingTrip = uiState.isStartingTrip,
                  isEndingTrip = uiState.isEndingTrip,
                  isResumingTrip = uiState.isResumingTrip,
                  isTransferringTrip = uiState.isTransferringTrip,
                  isReportingIncident = uiState.isReportingIncident,
                  currentTripStatus = uiState.currentTripExecution?.status,
                  coordinate = coordinate,
                  onStartTrip = { routeId, scheduleId, driverId, onSuccess ->
                    viewModel.startTrip(
                        routeId = routeId,
                        scheduleId = scheduleId,
                        driverId = driverId,
                        onSuccess = { onSuccess() },
                    )
                  },
                  onEndTrip = { tripExecutionId, onSuccess ->
                    viewModel.endTrip(
                        tripExecutionId = tripExecutionId,
                        onSuccess = { onSuccess() },
                    )
                  },
                  onResumeTrip = { tripExecutionId, onSuccess ->
                    viewModel.resumeTrip(
                        tripExecutionId = tripExecutionId,
                        onSuccess = { onSuccess() },
                    )
                  },
                  onTransferTrip = { tripExecutionId, targetDriverId, onSuccess, onFailure ->
                    viewModel.transferTrip(
                        tripExecutionId = tripExecutionId,
                        targetDriverId = targetDriverId,
                        onSuccess = onSuccess,
                        onFailure = onFailure,
                    )
                  },
                  onReportIncident = {
                      tripExecutionId,
                      type,
                      detail,
                      incidentLocation,
                      onSuccess,
                      onFailure ->
                    viewModel.reportIncident(
                        tripExecutionId = tripExecutionId,
                        type = type,
                        detail = detail,
                        location = incidentLocation,
                        onSuccess = { onSuccess() },
                        onFailure = onFailure,
                    )
                  },
              )
          else ->
              DriverEmptyState(
                  userName = userName,
                  errorMessage =
                      uiState.errorMessage ?: "No hay una ruta asignada a este conductor.",
              )
        }
      }
    }
  }
}

@Composable
private fun DriverDashboardContent(
    route: Route,
    userId: String,
    activeTripExecutionId: String?,
    activeTripScheduleId: String?,
    currentTripStartedAt: Long?,
    completedTodayScheduleIds: Set<String>,
    history: List<DriverTripHistoryRecord>,
    transferDrivers: List<Driver>,
    isStartingTrip: Boolean,
    isEndingTrip: Boolean,
    isResumingTrip: Boolean,
    isTransferringTrip: Boolean,
    isReportingIncident: Boolean,
    currentTripStatus: TripExecutionStatus?,
    coordinate: Coordinate?,
    onStartTrip:
        (routeId: String, scheduleId: String, driverId: String, onSuccess: () -> Unit) -> Unit,
    onEndTrip: (tripExecutionId: String, onSuccess: () -> Unit) -> Unit,
    onResumeTrip: (tripExecutionId: String, onSuccess: () -> Unit) -> Unit,
    onTransferTrip:
        (
            tripExecutionId: String,
            targetDriverId: String,
            onSuccess: () -> Unit,
            onFailure: (String) -> Unit,
        ) -> Unit,
    onReportIncident:
        (
            tripExecutionId: String,
            type: TripIncidentType,
            detail: String,
            location: Coordinate?,
            onSuccess: () -> Unit,
            onFailure: (String) -> Unit,
        ) -> Unit,
) {
  val schedules =
      route.schedules
          .filter { it.driverId == userId }
          .sortedBy { parseTimeToMinutes(it.departureTime) }
  val currentMinutes = currentCostaRicaTimeMinutes()
  var selectedTab by remember(route.id) { mutableStateOf(DriverPanelTab.ACTIVE_ROUTE) }
  var tripElapsedSeconds by
      remember(route.id, activeTripExecutionId) {
        mutableIntStateOf(initialElapsedSeconds(currentTripStartedAt))
      }
  var routeNotice by remember(route.id) { mutableStateOf<DriverNotice?>(null) }

  val isTripRunning = activeTripExecutionId != null
  val isTripDelayed = currentTripStatus == TripExecutionStatus.DELAYED
  val remainingSchedules = schedules.filterNot { completedTodayScheduleIds.contains(it.id) }
  val currentSchedule =
      activeTripScheduleId?.let { activeId -> remainingSchedules.firstOrNull { it.id == activeId } }
          ?: remainingSchedules.firstOrNull {
            parseTimeToMinutes(it.departureTime) >= currentMinutes
          }
          ?: remainingSchedules.firstOrNull()
  val nextSchedule =
      currentSchedule?.let { current ->
        remainingSchedules.dropWhile { it.id != current.id }.drop(1).firstOrNull()
      }
  val routeStatusLabel = if (isTripDelayed) "Detenida" else "Activa"
  val routeStatusColor =
      if (isTripDelayed) Color(0xFF8A6B2B) else MaterialTheme.colorScheme.tertiary
  val routeStatusContainer =
      if (isTripDelayed) Color(0xFFFFF4CC) else MaterialTheme.colorScheme.tertiaryContainer
  val nextShiftStatusLabel = if (nextSchedule != null) "Programada" else "Sin más"
  val nextShiftStatusColor = Color(0xFF8A6B2B)
  val nextShiftStatusContainer = Color(0xFFFFF4CC)
  var incidentDialogContext by remember { mutableStateOf<IncidentDialogContext?>(null) }
  var showTransferDialog by remember { mutableStateOf(false) }
  var showAllAssignedSchedules by remember(route.id) { mutableStateOf(false) }
  var incidentError by remember { mutableStateOf<String?>(null) }

  LaunchedEffect(activeTripExecutionId, currentTripStartedAt) {
    tripElapsedSeconds = initialElapsedSeconds(currentTripStartedAt)
  }

  LaunchedEffect(activeTripExecutionId) {
    if (activeTripExecutionId != null) {
      incidentError = null
    }
  }

  LaunchedEffect(isTripRunning, currentTripStartedAt) {
    while (isTripRunning) {
      delay(1000.milliseconds)
      tripElapsedSeconds = initialElapsedSeconds(currentTripStartedAt)
    }
  }

  LaunchedEffect(routeNotice) {
    if (routeNotice != null) {
      delay(2200.milliseconds)
      routeNotice = null
    }
  }

  DriverPanelTabs(
      selectedTab = selectedTab,
      onTabSelected = { selectedTab = it },
  )

  when (selectedTab) {
    DriverPanelTab.ACTIVE_ROUTE -> {
      routeNotice?.let { notice ->
        DriverStatusNotice(
            message = notice.message,
            tone = notice.tone,
        )
      }

      if (currentSchedule != null) {
        DriverCurrentRouteCard(
            route = route,
            schedule = currentSchedule,
            statusLabel = routeStatusLabel,
            statusColor = routeStatusColor,
            statusContainer = routeStatusContainer,
            isTripRunning = isTripRunning,
            isTripDelayed = isTripDelayed,
            elapsedTime = formatElapsedTime(tripElapsedSeconds),
            tripCompleted = false,
            startedAt = null,
            endedAt = null,
            isStartingTrip = isStartingTrip,
            isEndingTrip = isEndingTrip,
            isResumingTrip = isResumingTrip,
            isTransferringTrip = isTransferringTrip,
            onPrimaryAction = {
              if (isTripDelayed) {
                activeTripExecutionId?.let { tripId ->
                  onResumeTrip(tripId) {
                    routeNotice =
                        DriverNotice(
                            message = "Ruta reanudada. El recorrido continúa.",
                            tone = DriverNoticeTone.INFO,
                        )
                  }
                }
              } else if (isTripRunning) {
                activeTripExecutionId?.let { tripId ->
                  onEndTrip(tripId) {
                    tripElapsedSeconds = 0
                    routeNotice =
                        DriverNotice(
                            message = "Ruta finalizada. Cargando próxima salida...",
                            tone = DriverNoticeTone.WARNING,
                        )
                  }
                }
              } else {
                onStartTrip(route.id, currentSchedule.id, userId) {
                  tripElapsedSeconds = 0
                  routeNotice =
                      DriverNotice(
                          message = "Ruta iniciada. Seguimiento activo.",
                          tone = DriverNoticeTone.INFO,
                      )
                }
              }
            },
            onTransferClick = {
              if (activeTripExecutionId == null) {
                routeNotice =
                    DriverNotice(
                        message = "No hay una ruta detenida para trasladar.",
                        tone = DriverNoticeTone.ERROR,
                    )
              } else {
                showTransferDialog = true
              }
            },
        )
      } else {
        DriverNoMoreRoutesCard()
      }

      DriverAssignedSchedulesCard(
          schedules = schedules,
          defaultSchedule = if (isTripRunning) nextSchedule else currentSchedule,
          activeScheduleId = activeTripScheduleId,
          completedScheduleIds = completedTodayScheduleIds,
          showAll = showAllAssignedSchedules,
          onToggleExpanded = { showAllAssignedSchedules = !showAllAssignedSchedules },
      )

      incidentError?.let { message ->
        if (incidentDialogContext == null && activeTripExecutionId == null) {
          DriverInlineAlert(
              message = message,
              tone = DriverNoticeTone.ERROR,
          )
        }
      }

      DriverIncidentCard(
          onAccidentClick = {
            if (activeTripExecutionId == null) {
              incidentError = "Inicia una ruta antes de reportar incidencias."
            } else {
              incidentError = null
              incidentDialogContext =
                  IncidentDialogContext(
                      type = TripIncidentType.ACCIDENT,
                      routeName = route.name.toDriverDisplayName(),
                      locationLabel = coordinate.toIncidentLocationLabel(),
                      reportedAtLabel = currentCostaRicaDateTimeLabel(),
                  )
            }
          },
          onBreakdownClick = {
            if (activeTripExecutionId == null) {
              incidentError = "Inicia una ruta antes de reportar incidencias."
            } else {
              incidentError = null
              incidentDialogContext =
                  IncidentDialogContext(
                      type = TripIncidentType.BREAKDOWN,
                      routeName = route.name.toDriverDisplayName(),
                      locationLabel = coordinate.toIncidentLocationLabel(),
                      reportedAtLabel = currentCostaRicaDateTimeLabel(),
                  )
            }
          },
          onSecurityClick = {
            if (activeTripExecutionId == null) {
              incidentError = "Inicia una ruta antes de reportar incidencias."
            } else {
              incidentError = null
              incidentDialogContext =
                  IncidentDialogContext(
                      type = TripIncidentType.SECURITY,
                      routeName = route.name.toDriverDisplayName(),
                      locationLabel = coordinate.toIncidentLocationLabel(),
                      reportedAtLabel = currentCostaRicaDateTimeLabel(),
                  )
            }
          },
      )
    }

    DriverPanelTab.TRAVEL_HISTORY -> {
      DriverHistoryCard(history = history)
    }
  }

  incidentDialogContext?.let { context ->
    IncidentReportDialog(
        type = context.type,
        routeName = context.routeName,
        locationLabel = context.locationLabel,
        reportedAtLabel = context.reportedAtLabel,
        isSubmitting = isReportingIncident,
        onDismiss = {
          if (!isReportingIncident) {
            incidentDialogContext = null
            incidentError = null
          }
        },
        onSubmit = { detail ->
          onReportIncident(
              activeTripExecutionId.orEmpty(),
              context.type,
              detail,
              coordinate,
              {
                incidentDialogContext = null
                incidentError = null
              },
              { message -> incidentError = message },
          )
        },
        errorMessage = incidentError,
    )
  }

  if (showTransferDialog && activeTripExecutionId != null) {
    TransferRouteDialog(
        drivers = transferDrivers,
        isSubmitting = isTransferringTrip,
        onDismiss = {
          if (!isTransferringTrip) {
            showTransferDialog = false
          }
        },
        onConfirm = { targetDriverId ->
          onTransferTrip(
              activeTripExecutionId,
              targetDriverId,
              {
                showTransferDialog = false
                routeNotice =
                    DriverNotice(
                        message = "Ruta trasladada correctamente.",
                        tone = DriverNoticeTone.WARNING,
                    )
              },
              { message ->
                routeNotice =
                    DriverNotice(
                        message = message,
                        tone = DriverNoticeTone.ERROR,
                    )
              },
          )
        },
    )
  }
}

@Composable
private fun DriverPanelTabs(
    selectedTab: DriverPanelTab,
    onTabSelected: (DriverPanelTab) -> Unit,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(18.dp),
      color = DriverTabContainerBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      DriverPanelTabButton(
          label = "Panel de trabajo",
          selected = selectedTab == DriverPanelTab.ACTIVE_ROUTE,
          modifier = Modifier.weight(1f),
          onClick = { onTabSelected(DriverPanelTab.ACTIVE_ROUTE) },
      )
      DriverPanelTabButton(
          label = "Histórico",
          selected = selectedTab == DriverPanelTab.TRAVEL_HISTORY,
          modifier = Modifier.weight(1f),
          onClick = { onTabSelected(DriverPanelTab.TRAVEL_HISTORY) },
      )
    }
  }
}

@Composable
private fun DriverPanelTabButton(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
  Surface(
      modifier = modifier.clickable(onClick = onClick),
      shape = RoundedCornerShape(12.dp),
      color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
  ) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
      Text(
          text = label,
          style = MaterialTheme.typography.bodySmall,
          color =
              if (selected) MaterialTheme.colorScheme.onPrimary
              else MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
          textAlign = TextAlign.Center,
      )
    }
  }
}

@Composable
private fun DriverNoMoreRoutesCard() {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(28.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Text(
          text = "JORNADA",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.SemiBold,
      )
      Text(
          text = "No hay más rutas asignadas hoy",
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
      )
      Text(
          text = "Ya completaste las salidas programadas para este día.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
private fun DriverCurrentRouteCard(
    route: Route,
    schedule: RouteSchedule?,
    statusLabel: String,
    statusColor: Color,
    statusContainer: Color,
    isTripRunning: Boolean,
    isTripDelayed: Boolean,
    elapsedTime: String,
    tripCompleted: Boolean,
    startedAt: String?,
    endedAt: String?,
    onPrimaryAction: () -> Unit,
    isStartingTrip: Boolean = false,
    isEndingTrip: Boolean = false,
    isResumingTrip: Boolean = false,
    isTransferringTrip: Boolean = false,
    onTransferClick: () -> Unit = {},
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = DriverSoftPanelBackground,
            border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
        ) {
          Text(
              text = "RUTA ACTUAL",
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.SemiBold,
          )
        }
        StatusChip(
            label = statusLabel,
            containerColor = statusContainer,
            contentColor = statusColor,
        )
      }

      Text(
          text = route.name.toDriverDisplayName(),
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
      )

      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.Top,
      ) {
        Surface(
            modifier = Modifier.weight(1f).height(96.dp),
            shape = RoundedCornerShape(16.dp),
            color = DriverSoftPanelBackground,
            border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
        ) {
          Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.Top,
          ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
            DriverQuickInfo(
                label = "Hora de salida",
                value = schedule?.departureTime?.toDisplayMeridiemTime() ?: "No disponible",
            )
          }
        }

        DriverEmbeddedTripStatus(
            elapsedTime = elapsedTime,
            isTripRunning = isTripRunning,
            isTripDelayed = isTripDelayed,
            tripCompleted = tripCompleted,
            startedAt = startedAt,
            endedAt = endedAt,
            modifier = Modifier.weight(1f).height(96.dp),
        )
      }
      PillActionButton(
          text =
              when {
                isStartingTrip -> "INICIANDO..."
                isResumingTrip -> "REANUDANDO..."
                isEndingTrip -> "FINALIZANDO..."
                isTripDelayed -> "REANUDAR RUTA"
                isTripRunning -> "FINALIZAR RUTA"
                else -> "INICIAR RUTA"
              },
          onClick = onPrimaryAction,
          modifier = Modifier.fillMaxWidth(),
          containerColor = MaterialTheme.colorScheme.primary,
          enabled = !isStartingTrip && !isEndingTrip && !isResumingTrip,
      )
      if (isTripDelayed) {
        PillActionButton(
            text = if (isTransferringTrip) "TRASLADANDO..." else "TRASLADAR RUTA",
            onClick = onTransferClick,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            border = BorderStroke(0.dp, Color.Transparent),
            enabled = !isTransferringTrip && !isResumingTrip && !isEndingTrip,
        )
      }
    }
  }
}

@Composable
private fun DriverStatusNotice(
    message: String,
    tone: DriverNoticeTone,
) {
  DriverInlineAlert(
      message = message,
      tone = tone,
  )
}

@Composable
private fun DriverInlineAlert(
    message: String,
    tone: DriverNoticeTone,
) {
  val (tint, containerColor) =
      when (tone) {
        DriverNoticeTone.INFO -> Color(0xFF2A6F97) to Color(0xFFDDF1FF)
        DriverNoticeTone.WARNING -> Color(0xFF8A6B2B) to Color(0xFFFFF4CC)
        DriverNoticeTone.ERROR ->
            MaterialTheme.colorScheme.error to
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
      }
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      color = containerColor,
      border = BorderStroke(DriverBorderWidth, tint.copy(alpha = 0.18f)),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Surface(
          modifier = Modifier.size(30.dp),
          shape = CircleShape,
          color = Color.White.copy(alpha = 0.7f),
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
              imageVector = Icons.Default.ReportProblem,
              contentDescription = null,
              tint = tint,
              modifier = Modifier.size(16.dp),
          )
        }
      }
      Text(
          text = message,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Medium,
      )
    }
  }
}

@Composable
private fun DriverQuickInfo(
    label: String,
    value: String,
) {
  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = value,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
    )
  }
}

@Composable
private fun DriverMetricCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
  Surface(
      modifier = modifier,
      shape = RoundedCornerShape(14.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
          modifier = Modifier.size(52.dp),
          contentAlignment = Alignment.Center,
      ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = DriverSoftPanelBackground,
            border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
        ) {}
        icon()
      }
      Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(2.dp),
      ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Composable
private fun DriverEmbeddedTripStatus(
    elapsedTime: String,
    isTripRunning: Boolean,
    isTripDelayed: Boolean,
    tripCompleted: Boolean,
    startedAt: String?,
    endedAt: String?,
    modifier: Modifier = Modifier,
) {
  Surface(
      modifier = modifier,
      shape = RoundedCornerShape(16.dp),
      color = DriverSoftPanelBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      Text(
          text = "Recorrido",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
      )
      Text(
          text = elapsedTime,
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
      )
      Text(
          text =
              when {
                isTripDelayed -> "Ruta detenida por avería"
                isTripRunning -> "Ruta en curso"
                tripCompleted -> "Ruta finalizada"
                else -> "Aun no ha iniciado"
              },
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          fontStyle = FontStyle.Italic,
      )
    }
  }
}

@Composable
private fun DriverInfoStrip(
    title: String,
    value: String,
    iconTint: Color,
    statusLabel: String,
    statusColor: Color,
    statusContainer: Color,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
              imageVector = Icons.Default.AccessTime,
              contentDescription = null,
              tint = iconTint,
              modifier = Modifier.size(15.dp),
          )
          Text(
              text = value,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface,
              fontWeight = FontWeight.SemiBold,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis,
          )
        }
      }
      Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        StatusChip(
            label = statusLabel,
            containerColor = statusContainer,
            contentColor = statusColor,
            compact = true,
        )
      }
    }
  }
}

@Composable
private fun DriverAssignedSchedulesCard(
    schedules: List<RouteSchedule>,
    defaultSchedule: RouteSchedule?,
    activeScheduleId: String?,
    completedScheduleIds: Set<String>,
    showAll: Boolean,
    onToggleExpanded: () -> Unit,
) {
  val visibleSchedules =
      when {
        schedules.isEmpty() -> emptyList()
        showAll -> schedules
        defaultSchedule != null -> listOf(defaultSchedule)
        else -> listOf(schedules.first())
      }

  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
            text = "HORARIOS ASIGNADOS",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
        )
        if (schedules.size > 1) {
          TextButton(onClick = onToggleExpanded) { Text(if (showAll) "Ocultar" else "Ver todos") }
        }
      }

      if (visibleSchedules.isEmpty()) {
        Text(
            text = "No hay más rutas asignadas hoy",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          visibleSchedules.forEach { schedule ->
            DriverAssignedScheduleRow(
                schedule = schedule,
                marker =
                    when {
                      activeScheduleId == schedule.id -> AssignedScheduleMarker.CURRENT
                      completedScheduleIds.contains(schedule.id) -> AssignedScheduleMarker.COMPLETED
                      defaultSchedule?.id == schedule.id -> AssignedScheduleMarker.NEXT
                      else -> null
                    },
            )
          }
        }
      }
    }
  }
}

@Composable
private fun DriverAssignedScheduleRow(
    schedule: RouteSchedule,
    marker: AssignedScheduleMarker?,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      color = DriverSoftPanelBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            modifier = Modifier.size(15.dp),
        )
        Text(
            text =
                "${schedule.departureTime.toDisplayMeridiemTime()} → ${schedule.arrivalTime.toDisplayMeridiemTime()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight =
                if (
                    marker == AssignedScheduleMarker.NEXT ||
                        marker == AssignedScheduleMarker.CURRENT
                )
                    FontWeight.Bold
                else FontWeight.Medium,
        )
      }
      marker?.let {
        StatusChip(
            label = it.label,
            containerColor = it.containerColor,
            contentColor = it.contentColor,
            compact = true,
        )
      }
    }
  }
}

private enum class AssignedScheduleMarker(
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
) {
  COMPLETED(
      label = "Completado",
      containerColor = Color(0xFFE9EEF5),
      contentColor = Color(0xFF6B7280),
  ),
  CURRENT(
      label = "Actual",
      containerColor = Color(0xFFE7F7EA),
      contentColor = Color(0xFF2F8D46),
  ),
  NEXT(
      label = "Próximo",
      containerColor = Color(0xFFFFF4CC),
      contentColor = Color(0xFF8A6B2B),
  ),
}

@Composable
private fun StatusChip(
    label: String,
    containerColor: Color,
    contentColor: Color,
    compact: Boolean = false,
) {
  Surface(
      shape = RoundedCornerShape(999.dp),
      color = containerColor,
  ) {
    Text(
        text = label.uppercase(),
        modifier =
            Modifier.padding(
                horizontal = if (compact) 7.dp else 9.dp,
                vertical = if (compact) 3.dp else 4.dp,
            ),
        style =
            MaterialTheme.typography.labelSmall.copy(
                fontSize =
                    if (compact) MaterialTheme.typography.labelSmall.fontSize * 0.88f
                    else MaterialTheme.typography.labelSmall.fontSize * 0.94f
            ),
        color = contentColor,
        fontWeight = FontWeight.SemiBold,
    )
  }
}

@Composable
private fun DriverIncidentCard(
    onAccidentClick: () -> Unit,
    onBreakdownClick: () -> Unit,
    onSecurityClick: () -> Unit,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(18.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
          text = "REPORTE DE INCIDENCIAS",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.SemiBold,
      )
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        IncidentAction(
            label = "Accidente",
            icon = Icons.Default.ReportProblem,
            tint = Color(0xFFB14D4D),
            modifier = Modifier.weight(1f),
            onClick = onAccidentClick,
        )
        IncidentAction(
            label = "Avería",
            icon = Icons.Default.Build,
            tint = Color(0xFF8A6B2B),
            modifier = Modifier.weight(1f),
            onClick = onBreakdownClick,
        )
        IncidentAction(
            label = "Seguridad",
            icon = Icons.Default.Security,
            tint = Color(0xFF51677F),
            modifier = Modifier.weight(1f),
            onClick = onSecurityClick,
        )
      }
    }
  }
}

@Composable
private fun IncidentReportDialog(
    type: TripIncidentType,
    routeName: String,
    locationLabel: String,
    reportedAtLabel: String,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit,
    errorMessage: String?,
) {
  val defaultMessage =
      remember(type, routeName, locationLabel, reportedAtLabel) {
        buildDefaultIncidentMessage(
            type = type,
            routeName = routeName,
            locationLabel = locationLabel,
            reportedAtLabel = reportedAtLabel,
        )
      }
  var detail by
      remember(routeName, type, locationLabel, reportedAtLabel) { mutableStateOf(defaultMessage) }

  AlertDialog(
      onDismissRequest = onDismiss,
      shape = RoundedCornerShape(24.dp),
      containerColor = DriverCardBackground,
      tonalElevation = 0.dp,
      title = {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top,
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                  text =
                      when (type) {
                        TripIncidentType.ACCIDENT -> "Reportar accidente"
                        TripIncidentType.BREAKDOWN -> "Reportar avería"
                        TripIncidentType.SECURITY -> "Reportar seguridad"
                      },
                  style = MaterialTheme.typography.titleMedium,
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold,
              )
              Text(
                  text =
                      when (type) {
                        TripIncidentType.ACCIDENT -> "Completa los detalles de la incidencia vial"
                        TripIncidentType.BREAKDOWN ->
                            "Completa los detalles de la incidencia técnica"
                        TripIncidentType.SECURITY ->
                            "Completa los detalles de la incidencia de seguridad"
                      },
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontWeight = FontWeight.Medium,
              )
            }

            Surface(
                modifier =
                    Modifier.size(40.dp).clickable(enabled = !isSubmitting, onClick = onDismiss),
                shape = CircleShape,
                color = DriverSoftPanelBackground,
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
              }
            }
          }
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(18.dp),
              color = DriverSoftPanelBackground,
          ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              DriverIncidentContextRow(
                  icon = Icons.Default.Flag,
                  label = "Ruta actual",
                  value = routeName,
              )
              DriverIncidentContextRow(
                  icon = Icons.Default.AccessTime,
                  label = "Fecha y hora",
                  value = reportedAtLabel,
              )
              DriverIncidentContextRow(
                  icon = Icons.Default.LocationOn,
                  label = "Ubicación",
                  value = locationLabel,
              )
            }
          }
          Text(
              text = "MENSAJE DEL REPORTE",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.8.sp,
          )
          OutlinedTextField(
              value = detail,
              onValueChange = { detail = it },
              modifier = Modifier.fillMaxWidth(),
              minLines = 5,
              enabled = !isSubmitting,
              placeholder = {
                Text("Describe detalladamente el problema técnico observado en la unidad...")
              },
              shape = RoundedCornerShape(16.dp),
              textStyle =
                  MaterialTheme.typography.bodySmall.copy(
                      color = MaterialTheme.colorScheme.onSurface
                  ),
              colors =
                  OutlinedTextFieldDefaults.colors(
                      focusedContainerColor = Color.White,
                      unfocusedContainerColor = Color.White,
                      disabledContainerColor = Color.White,
                      focusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                      unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.22f),
                      disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                  ),
          )
          Row(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = "Puedes ajustar el mensaje antes de enviarlo.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          errorMessage?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
          }
          PillActionButton(
              text = if (isSubmitting) "Enviando..." else "Enviar reporte",
              onClick = { onSubmit(detail) },
              enabled = !isSubmitting && detail.isNotBlank(),
              modifier = Modifier.fillMaxWidth(),
              containerColor = MaterialTheme.colorScheme.primary,
              leadingIcon = Icons.Default.Send,
          )
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically,
          ) {
            TextButton(
                onClick = onDismiss,
                enabled = !isSubmitting,
            ) {
              Text("Cancelar")
            }
          }
        }
      },
      confirmButton = {},
      dismissButton = {},
  )
}

@Composable
private fun TransferRouteDialog(
    drivers: List<Driver>,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
  var selectedDriverId by remember(drivers) { mutableStateOf(drivers.firstOrNull()?.id) }

  AlertDialog(
      onDismissRequest = onDismiss,
      shape = RoundedCornerShape(24.dp),
      containerColor = DriverCardBackground,
      tonalElevation = 0.dp,
      title = {
        Text(
            text = "Trasladar ruta",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
              text = "Selecciona otro chofer de la misma compañía para asignarle esta ruta.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          if (drivers.isEmpty()) {
            DriverInlineAlert(
                message = "No hay otros choferes disponibles en la misma compañía.",
                tone = DriverNoticeTone.ERROR,
            )
          } else {
            drivers.forEach { driver ->
              Surface(
                  modifier =
                      Modifier.fillMaxWidth().clickable(enabled = !isSubmitting) {
                        selectedDriverId = driver.id
                      },
                  shape = RoundedCornerShape(14.dp),
                  color = DriverSoftPanelBackground,
                  border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
              ) {
                Row(
                    modifier =
                        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                  RadioButton(
                      selected = selectedDriverId == driver.id,
                      onClick = if (isSubmitting) null else ({ selectedDriverId = driver.id }),
                  )
                  Text(
                      text = driver.name,
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.onSurface,
                      fontWeight = FontWeight.Medium,
                  )
                }
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(
            onClick = { selectedDriverId?.let(onConfirm) },
            enabled = !isSubmitting && selectedDriverId != null && drivers.isNotEmpty(),
        ) {
          Text(if (isSubmitting) "Trasladando..." else "Confirmar")
        }
      },
      dismissButton = {
        TextButton(
            onClick = onDismiss,
            enabled = !isSubmitting,
        ) {
          Text("Cancelar")
        }
      },
  )
}

@Composable
private fun DriverHistoryCard(history: List<DriverTripHistoryRecord>) {
  var historyQuery by remember { mutableStateOf("") }
  val filteredHistory =
      history.filter { item ->
        if (historyQuery.isBlank()) return@filter true
        val normalizedQuery = historyQuery.trim().lowercase()
        listOf(item.routeName, item.departureTime, item.startedAtLabel, item.endedAtLabel)
            .joinToString(" ")
            .lowercase()
            .contains(normalizedQuery)
      }

  Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    DriverHistoryFilterField(
        value = historyQuery,
        onValueChange = { historyQuery = it },
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
          text = "RUTAS RECIENTES",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.SemiBold,
      )
      if (history.isNotEmpty()) {
        Text(
            text = "Ver todo",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
        )
      }
    }

    when {
      history.isEmpty() -> {
        DriverInfoCard(
            title = "Histórico",
            body = "Sin recorridos recientes",
            supporting = "Aún no hay recorridos finalizados en esta sesión.",
        )
      }
      filteredHistory.isEmpty() -> {
        DriverInfoCard(
            title = "Histórico",
            body = "Sin coincidencias",
            supporting = "No hay recorridos que coincidan con ese filtro.",
        )
      }
      else -> {
        filteredHistory.forEach { item ->
          val startParts = splitCostaRicaDateTimeLabel(item.startedAtLabel)
          val endParts = splitCostaRicaDateTimeLabel(item.endedAtLabel)
          val compactDuration =
              formatTripDurationCompactFromLabels(item.startedAtLabel, item.endedAtLabel)
          val incidentDetail = item.incidentDetail

          Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              color = DriverCardBackground,
              border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
          ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.Top,
              ) {
                Text(
                    text = item.routeName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f).padding(top = 4.dp),
                )
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color =
                        if (item.statusLabel == "TRASLADADO") Color(0xFFFFF4CC)
                        else Color(0xFFDDF1FF),
                ) {
                  Text(
                      text = item.statusLabel,
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                      style = MaterialTheme.typography.labelSmall,
                      color =
                          if (item.statusLabel == "TRASLADADO") Color(0xFF8A6B2B)
                          else Color(0xFF2A6F97),
                      fontWeight = FontWeight.SemiBold,
                  )
                }
              }

              Row(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = "Salida programada: ${item.departureTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )
              }

              Surface(
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(14.dp),
                  color = DriverSoftPanelBackground,
                  border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
              ) {
                Row(
                    modifier =
                        Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                  DriverHistoryMetricColumn(
                      icon = Icons.Default.LocationOn,
                      label = "INICIO",
                      primaryValue = startParts?.second ?: item.startedAtLabel,
                      secondaryValue = startParts?.first,
                      modifier = Modifier.weight(1f),
                  )
                  DriverHistoryMetricColumn(
                      icon = Icons.Default.AccessTime,
                      label = "DURACIÓN",
                      primaryValue = compactDuration,
                      modifier = Modifier.weight(1f),
                  )
                  DriverHistoryMetricColumn(
                      icon = Icons.Default.Flag,
                      label = "FINAL",
                      primaryValue = endParts?.second ?: item.endedAtLabel,
                      secondaryValue = endParts?.first,
                      modifier = Modifier.weight(1f),
                  )
                }
              }

              if (!incidentDetail.isNullOrBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = DriverSoftPanelBackground,
                    border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
                ) {
                  Column(
                      modifier =
                          Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                      verticalArrangement = Arrangement.spacedBy(6.dp),
                  ) {
                    Text(
                        text = item.incidentTypeLabel?.uppercase() ?: "INCIDENCIA",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = incidentDetail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun DriverHistoryFilterField(
    value: String,
    onValueChange: (String) -> Unit,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(18.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Icon(
          imageVector = Icons.Default.Search,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(18.dp),
      )

      BasicTextField(
          value = value,
          onValueChange = onValueChange,
          modifier = Modifier.weight(1f).padding(vertical = 12.dp),
          singleLine = true,
          textStyle =
              MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface),
          decorationBox = { innerTextField ->
            Box {
              if (value.isBlank()) {
                Text(
                    text = "Buscar por ruta, salida, inicio o fin",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
              innerTextField()
            }
          },
      )

      if (value.isNotBlank()) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Limpiar filtro",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp).clickable { onValueChange("") },
        )
      }
    }
  }
}

@Composable
private fun DriverHistoryMetricColumn(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    primaryValue: String,
    secondaryValue: String? = null,
    modifier: Modifier = Modifier,
) {
  Column(
      modifier = modifier,
      verticalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
          imageVector = icon,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(12.dp),
      )
      Text(
          text = label,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.SemiBold,
      )
    }
    Text(
        text = primaryValue,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
    )
    secondaryValue?.let {
      Text(
          text = it,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
private fun DriverIncidentContextRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
  Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.Top,
  ) {
    Surface(
        modifier = Modifier.size(32.dp),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp),
        )
      }
    }
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
      Text(
          text = label.uppercase(),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp,
      )
      Text(
          text = value,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.SemiBold,
      )
    }
  }
}

@Composable
private fun IncidentAction(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
  Surface(
      modifier = modifier.height(98.dp).clickable(onClick = onClick),
      shape = RoundedCornerShape(16.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Surface(
          modifier = Modifier.size(42.dp),
          shape = CircleShape,
          color = DriverSoftPanelBackground,
          border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
              imageVector = icon,
              contentDescription = label,
              tint = tint,
          )
        }
      }
      Text(
          text = label.uppercase(),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(top = 8.dp),
      )
    }
  }
}

@Composable
private fun DriverEmptyState(
    userName: String,
    errorMessage: String,
) {
  DriverInfoCard(
      title = "Conductor",
      body = userName.ifBlank { "Conductor" },
      supporting = errorMessage,
  )
}

@Composable
private fun DriverInfoCard(
    title: String,
    body: String,
    supporting: String? = null,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(18.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Text(
          text = title.uppercase(),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
      )
      Text(
          text = body,
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.SemiBold,
      )
      supporting?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

@Composable
private fun DriverLoadingCard() {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(18.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      CircularProgressIndicator(
          modifier = Modifier.size(22.dp),
          strokeWidth = 2.5.dp,
          color = MaterialTheme.colorScheme.onSurface,
      )
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Cargando ruta",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Estamos buscando la ruta asignada al conductor autenticado.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

private fun parseTimeToMinutes(time: String): Int {
  return parseFlexibleTimeToMinutes(time) ?: Int.MAX_VALUE
}

private fun formatShiftDuration(schedule: RouteSchedule): String {
  val departure = parseTimeToMinutes(schedule.departureTime)
  val arrival = parseTimeToMinutes(schedule.arrivalTime)
  if (departure == Int.MAX_VALUE || arrival == Int.MAX_VALUE) return "--:--"

  val duration = if (arrival >= departure) arrival - departure else (24 * 60 - departure) + arrival
  val hours = duration / 60
  val minutes = duration % 60
  return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
}

private fun formatElapsedTime(totalSeconds: Int): String {
  val hours = totalSeconds / 3600
  val minutes = (totalSeconds % 3600) / 60
  val seconds = totalSeconds % 60
  return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

private fun formatMinutes(totalMinutes: Int): String {
  val clamped = ((totalMinutes % (24 * 60)) + 24 * 60) % (24 * 60)
  val hours = clamped / 60
  val minutes = clamped % 60
  return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
}

private fun String.toDisplayMeridiemTime(): String {
  val totalMinutes = parseFlexibleTimeToMinutes(this) ?: return this
  val hour24 = totalMinutes / 60
  val minutes = totalMinutes % 60
  val meridiem = if (hour24 >= 12) "PM" else "AM"
  val hour12 =
      when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
      }
  return "${hour12.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')} $meridiem"
}

private fun initialElapsedSeconds(startedAtEpoch: Long?): Int {
  if (startedAtEpoch == null) return 0
  val nowEpoch = Clock.System.now().toEpochMilliseconds()
  return ((nowEpoch - startedAtEpoch).coerceAtLeast(0L) / 1000L).toInt()
}

private fun currentCostaRicaDateTimeLabel(): String {
  val now = Clock.System.now()
  val local = now.toLocalDateTime(TimeZone.of("America/Costa_Rica"))
  val day = local.day.toString().padStart(2, '0')
  val month = local.month.number.toString().padStart(2, '0')
  val year = local.year.toString()
  val hour = local.hour.toString().padStart(2, '0')
  val minute = local.minute.toString().padStart(2, '0')
  val second = local.second.toString().padStart(2, '0')
  return "$day/$month/$year $hour:$minute:$second"
}

private fun formatTripDurationFromLabels(startedAt: String, endedAt: String): String {
  val totalSeconds = tripDurationSecondsFromLabels(startedAt, endedAt)
  val hours = totalSeconds / 3600
  val minutes = (totalSeconds % 3600) / 60
  val seconds = totalSeconds % 60
  return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

private fun formatTripDurationCompactFromLabels(startedAt: String, endedAt: String): String =
    formatCompactDuration(tripDurationSecondsFromLabels(startedAt, endedAt))

private fun tripDurationSecondsFromLabels(startedAt: String, endedAt: String): Long {
  val startInstant = parseCostaRicaDateTimeLabel(startedAt) ?: return 0L
  val endInstant = parseCostaRicaDateTimeLabel(endedAt) ?: return 0L
  return (endInstant.epochSeconds - startInstant.epochSeconds).coerceAtLeast(0)
}

private fun splitCostaRicaDateTimeLabel(value: String): Pair<String, String>? {
  val parts = value.trim().split(" ")
  if (parts.size != 2) return null
  return parts[0] to parts[1]
}

private fun formatCompactDuration(totalSeconds: Long): String {
  val seconds = totalSeconds.coerceAtLeast(0)
  val hours = seconds / 3600
  val minutes = (seconds % 3600) / 60
  val remainingSeconds = seconds % 60

  return when {
    hours > 0 -> "${hours}h ${minutes}m"
    minutes > 0 -> "$minutes min"
    else -> "$remainingSeconds s"
  }
}

private fun parseCostaRicaDateTimeLabel(value: String) =
    runCatching {
          val parts = value.trim().split(" ")
          if (parts.size != 2) return null
          val dateParts = parts[0].split("/")
          val timeParts = parts[1].split(":")
          if (dateParts.size != 3 || timeParts.size != 3) return null

          val day = dateParts[0].toInt()
          val month = dateParts[1].toInt()
          val year = dateParts[2].toInt()
          val hour = timeParts[0].toInt()
          val minute = timeParts[1].toInt()
          val second = timeParts[2].toInt()

          LocalDateTime(year, month, day, hour, minute, second)
              .toInstant(TimeZone.of("America/Costa_Rica"))
        }
        .getOrNull()

private fun String.toDriverDisplayName(): String =
    lowercase()
        .split(" ", "-", "/")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
          word.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
          }
        }

private fun buildDefaultIncidentMessage(
    type: TripIncidentType,
    routeName: String,
    locationLabel: String,
    reportedAtLabel: String,
): String {
  return when (type) {
    TripIncidentType.ACCIDENT ->
        "He tenido un accidente de tránsito en la ruta $routeName, a las $reportedAtLabel, en la ubicación $locationLabel. Necesito asistencia."
    TripIncidentType.BREAKDOWN ->
        "La unidad presenta una avería mecánica en la ruta $routeName, a las $reportedAtLabel, en la ubicación $locationLabel. Necesito apoyo."
    TripIncidentType.SECURITY ->
        "Tenemos una situación de seguridad en la ruta $routeName, a las $reportedAtLabel, en la ubicación $locationLabel. Necesitamos apoyo."
  }
}

private fun Coordinate?.toIncidentLocationLabel(): String {
  if (this == null) return "Ubicación actual no disponible"
  val latitudeLabel = latitude.formatCoordinateValue()
  val longitudeLabel = longitude.formatCoordinateValue()
  return "$latitudeLabel, $longitudeLabel"
}

private fun Double.formatCoordinateValue(): String = "%,.6f".format(this).replace(",", "")
