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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.busalotico.ui.components.button.PillActionButton
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.presentation.route.DriverRouteViewModel
import com.example.shared.util.currentCostaRicaTimeMinutes
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toInstant
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

private data class DriverTripHistoryItem(
    val routeName: String,
    val departureTime: String,
    val startedAt: String,
    val endedAt: String,
)

private val DriverPageBackground = Color(0xFFF7F8FA)
private val DriverCardBackground = Color.White
private val DriverSoftPanelBackground = Color(0xFFFAFBFD)
private val DriverSoftBorder = Color(0xFFE9EDF3)
private val DriverBorderWidth = 0.75.dp
private val DriverTabContainerBackground = Color(0xFFF1F3F6)

private enum class DriverPanelTab {
  ACTIVE_ROUTE,
  TRAVEL_HISTORY,
}

@Composable
fun DriverHomeScreen(
    userId: String,
    userName: String,
    viewModel: DriverRouteViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsState()

  LaunchedEffect(userId) {
    viewModel.loadRouteForDriver(userId)
  }

  Surface(modifier = modifier.fillMaxSize(), color = DriverPageBackground) {
    Column(modifier = Modifier.fillMaxSize()) {
      ScreenHeader(
          title = "Panel de conductor",
          subtitle = userName.ifBlank { "Conductor" },
      )

      Column(
          modifier =
              Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 16.dp)
                  .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(14.dp),
      ) {
        when {
          uiState.isLoading -> DriverLoadingCard()
          uiState.route != null -> DriverDashboardContent(
              route = uiState.route!!,
              userId = userId,
          )
          else -> DriverEmptyState(
              userName = userName,
              errorMessage = uiState.errorMessage ?: "No hay una ruta asignada a este conductor.",
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
) {
  val schedules = route.schedules.filter { it.driverId == userId }.sortedBy { parseTimeToMinutes(it.departureTime) }
  val currentMinutes = currentCostaRicaTimeMinutes()
  var isTripRunning by remember(route.id) { mutableStateOf(false) }
  var tripElapsedSeconds by remember(route.id) { mutableIntStateOf(0) }
  var tripCompleted by remember(route.id) { mutableStateOf(false) }
  var tripStartedAt by remember(route.id) { mutableStateOf<String?>(null) }
  var tripEndedAt by remember(route.id) { mutableStateOf<String?>(null) }
  var activeTripScheduleId by remember(route.id) { mutableStateOf<String?>(null) }
  var completedScheduleIds by remember(route.id) { mutableStateOf(setOf<String>()) }
  var tripHistory by remember(route.id) { mutableStateOf<List<DriverTripHistoryItem>>(emptyList()) }
  var selectedTab by remember(route.id) { mutableStateOf(DriverPanelTab.ACTIVE_ROUTE) }

  val remainingSchedules = schedules.filterNot { completedScheduleIds.contains(it.id) }
  val currentSchedule =
      activeTripScheduleId?.let { activeId ->
        remainingSchedules.firstOrNull { it.id == activeId }
      } ?: remainingSchedules
          .firstOrNull { parseTimeToMinutes(it.departureTime) >= currentMinutes }
          ?: remainingSchedules.firstOrNull()
  val nextSchedule =
      currentSchedule?.let { current ->
        remainingSchedules.dropWhile { it.id != current.id }.drop(1).firstOrNull()
      }
  val routeStatusLabel = "Activa"
  val routeStatusColor = MaterialTheme.colorScheme.tertiary
  val routeStatusContainer = MaterialTheme.colorScheme.tertiaryContainer
  val nextShiftStatusLabel = if (nextSchedule != null) "Programada" else "Sin más"
  val nextShiftStatusColor = Color(0xFF8A6B2B)
  val nextShiftStatusContainer = Color(0xFFFFF4CC)

  LaunchedEffect(isTripRunning) {
    while (isTripRunning) {
      delay(1000)
      tripElapsedSeconds += 1
    }
  }

  DriverPanelTabs(
      selectedTab = selectedTab,
      onTabSelected = { selectedTab = it },
  )

  when (selectedTab) {
    DriverPanelTab.ACTIVE_ROUTE -> {
      if (currentSchedule != null) {
        DriverCurrentRouteCard(
            route = route,
            schedule = currentSchedule,
            statusLabel = routeStatusLabel,
            statusColor = routeStatusColor,
            statusContainer = routeStatusContainer,
            isTripRunning = isTripRunning,
            elapsedTime = formatElapsedTime(tripElapsedSeconds),
            tripCompleted = tripCompleted,
            startedAt = tripStartedAt,
            endedAt = tripEndedAt,
            onPrimaryAction = {
              if (isTripRunning) {
                isTripRunning = false
                val endedTime = currentCostaRicaDateTimeLabel()
                if (currentSchedule != null && tripStartedAt != null) {
                  tripHistory =
                      listOf(
                          DriverTripHistoryItem(
                              routeName = route.name.toDriverDisplayName(),
                              departureTime = currentSchedule.departureTime,
                              startedAt = tripStartedAt!!,
                              endedAt = endedTime,
                          )
                      ) + tripHistory
                }
                currentSchedule.id.let { finishedId ->
                  completedScheduleIds = completedScheduleIds + finishedId
                }
                activeTripScheduleId = null
                tripElapsedSeconds = 0
                tripCompleted = false
                tripStartedAt = null
                tripEndedAt = null
              } else {
                activeTripScheduleId = currentSchedule.id
                isTripRunning = true
                tripElapsedSeconds = 0
                tripCompleted = false
                tripStartedAt = currentCostaRicaDateTimeLabel()
                tripEndedAt = null
              }
            },
        )
      } else {
        DriverNoMoreRoutesCard()
      }

      DriverInfoStrip(
          title = "Próxima salida",
          value =
              nextSchedule?.let { "${it.departureTime}  →  ${route.name.toDriverDisplayName()}" }
                  ?: "No hay más rutas asignadas hoy",
          iconTint = nextShiftStatusColor,
          statusLabel = nextShiftStatusLabel,
          statusColor = nextShiftStatusColor,
          statusContainer = nextShiftStatusContainer,
      )

      DriverMetricCard(
          title = "Tiempo turno",
          value = currentSchedule?.let(::formatShiftDuration) ?: "--:--",
          icon = {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          },
          modifier = Modifier.fillMaxWidth(),
      )

      DriverIncidentCard()
    }

    DriverPanelTab.TRAVEL_HISTORY -> {
      DriverHistoryCard(history = tripHistory)
    }
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
        modifier = Modifier.fillMaxWidth().padding(6.dp),
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
      Text(
          text = label,
          style = MaterialTheme.typography.bodySmall,
          color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
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
    elapsedTime: String,
    tripCompleted: Boolean,
    startedAt: String?,
    endedAt: String?,
    onPrimaryAction: () -> Unit,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
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
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
      )

      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.Top,
      ) {
        Surface(
            modifier = Modifier.weight(1f).height(108.dp),
            shape = RoundedCornerShape(16.dp),
            color = DriverSoftPanelBackground,
            border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
        ) {
          Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
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
                value = schedule?.departureTime ?: "No disponible",
            )
          }
        }

        DriverEmbeddedTripStatus(
            elapsedTime = elapsedTime,
            isTripRunning = isTripRunning,
            tripCompleted = tripCompleted,
            startedAt = startedAt,
            endedAt = endedAt,
            modifier = Modifier.weight(1f).height(108.dp),
        )
      }
      PillActionButton(
          text = if (isTripRunning) "FINALIZAR RUTA" else "INICIAR RUTA",
          onClick = onPrimaryAction,
          modifier = Modifier.fillMaxWidth(),
          containerColor = MaterialTheme.colorScheme.primary,
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
        style = MaterialTheme.typography.titleLarge,
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      Text(
          text = "Recorrido",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
      )
      Text(
          text = elapsedTime,
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
      )
      Text(
          text =
              when {
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
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = if (compact) MaterialTheme.typography.labelSmall.fontSize * 0.88f else MaterialTheme.typography.labelSmall.fontSize * 0.94f
        ),
        color = contentColor,
        fontWeight = FontWeight.SemiBold,
    )
  }
}

@Composable
private fun DriverIncidentCard() {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(18.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Text(
          text = "REPORTE DE INCIDENCIAS",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.SemiBold,
      )
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        IncidentAction(
            label = "Accidente",
            icon = Icons.Default.ReportProblem,
            tint = Color(0xFFB14D4D),
            modifier = Modifier.weight(1f),
        )
        IncidentAction(
            label = "Avería",
            icon = Icons.Default.Build,
            tint = Color(0xFF8A6B2B),
            modifier = Modifier.weight(1f),
        )
        IncidentAction(
            label = "Seguridad",
            icon = Icons.Default.Security,
            tint = Color(0xFF51677F),
            modifier = Modifier.weight(1f),
        )
      }
    }
  }
}

@Composable
private fun DriverHistoryCard(history: List<DriverTripHistoryItem>) {
  var historyQuery by remember { mutableStateOf("") }
  val filteredHistory =
      history.filter { item ->
        if (historyQuery.isBlank()) return@filter true
        val normalizedQuery = historyQuery.trim().lowercase()
        listOf(item.routeName, item.departureTime, item.startedAt, item.endedAt)
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
          val startParts = splitCostaRicaDateTimeLabel(item.startedAt)
          val endParts = splitCostaRicaDateTimeLabel(item.endedAt)
          val compactDuration = formatTripDurationCompactFromLabels(item.startedAt, item.endedAt)

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
                    color = Color(0xFFDDF1FF),
                ) {
                  Text(
                      text = "COMPLETADO",
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                      style = MaterialTheme.typography.labelSmall,
                      color = Color(0xFF2A6F97),
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
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                  DriverHistoryMetricColumn(
                      icon = Icons.Default.LocationOn,
                      label = "INICIO",
                      primaryValue = startParts?.second ?: item.startedAt,
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
                      primaryValue = endParts?.second ?: item.endedAt,
                      secondaryValue = endParts?.first,
                      modifier = Modifier.weight(1f),
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
          textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface),
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
private fun IncidentAction(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
) {
  Surface(
      modifier = modifier.height(118.dp),
      shape = RoundedCornerShape(16.dp),
      color = DriverCardBackground,
      border = BorderStroke(DriverBorderWidth, DriverSoftBorder),
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Surface(
          modifier = Modifier.size(52.dp),
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
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(top = 10.dp),
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
  val parts = time.split(":")
  if (parts.size < 2) return Int.MAX_VALUE
  val hour = parts[0].toIntOrNull() ?: return Int.MAX_VALUE
  val minute = parts[1].toIntOrNull() ?: return Int.MAX_VALUE
  return hour * 60 + minute
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

private fun currentCostaRicaDateTimeLabel(): String {
  val now = Clock.System.now()
  val local = now.toLocalDateTime(TimeZone.of("America/Costa_Rica"))
  val day = local.dayOfMonth.toString().padStart(2, '0')
  val month = local.monthNumber.toString().padStart(2, '0')
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
    minutes > 0 -> "${minutes} min"
    else -> "${remainingSeconds} s"
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

      LocalDateTime(year, month, day, hour, minute, second).toInstant(TimeZone.of("America/Costa_Rica"))
    }.getOrNull()

private fun String.toDriverDisplayName(): String =
    lowercase()
        .split(" ", "-", "/")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
          word.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
          }
        }
