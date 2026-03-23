package com.example.busalotico.ui.screen.driver

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
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
import org.koin.compose.viewmodel.koinViewModel

private data class DriverTripHistoryItem(
    val routeName: String,
    val departureTime: String,
    val startedAt: String,
    val endedAt: String,
)

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

  Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF4F4F6)) {
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
  val routeStatusColor = Color(0xFF2E9D57)
  val routeStatusContainer = Color(0xFFE8F6EC)
  val nextShiftStatusLabel = if (nextSchedule != null) "Programada" else "Sin más"
  val nextShiftStatusColor = Color(0xFF8B8E95)
  val nextShiftStatusContainer = Color(0xFFF0F2F5)

  LaunchedEffect(isTripRunning) {
    while (isTripRunning) {
      delay(1000)
      tripElapsedSeconds += 1
    }
  }

  if (currentSchedule != null) {
    DriverCurrentRouteCard(
        route = route,
        schedule = currentSchedule,
        statusLabel = routeStatusLabel,
        statusColor = routeStatusColor,
        statusContainer = routeStatusContainer,
        isTripRunning = isTripRunning,
      onPrimaryAction = {
        if (isTripRunning) {
          isTripRunning = false
          val endedTime = formatMinutes(currentCostaRicaTimeMinutes())
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
          currentSchedule?.id?.let { finishedId ->
            completedScheduleIds = completedScheduleIds + finishedId
          }
          activeTripScheduleId = null
          tripElapsedSeconds = 0
          tripCompleted = false
          tripStartedAt = null
          tripEndedAt = null
        } else {
          activeTripScheduleId = currentSchedule?.id
          isTripRunning = true
          tripElapsedSeconds = 0
          tripCompleted = false
            tripStartedAt = formatMinutes(currentCostaRicaTimeMinutes())
            tripEndedAt = null
          }
        },
    )
  } else {
    DriverNoMoreRoutesCard()
  }

  Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    DriverMetricCard(
        title = "Tiempo turno",
        value = currentSchedule?.let(::formatShiftDuration) ?: "--:--",
        icon = {
          Icon(
              imageVector = Icons.Default.AccessTime,
              contentDescription = null,
              tint = Color(0xFF5A5E66),
          )
        },
        modifier = Modifier.weight(1f),
    )
    DriverTripStatusCard(
        elapsedTime = formatElapsedTime(tripElapsedSeconds),
        isTripRunning = isTripRunning,
        tripCompleted = tripCompleted,
        startedAt = tripStartedAt,
        endedAt = tripEndedAt,
        modifier = Modifier.weight(1f),
    )
  }

  DriverInfoStrip(
      title = "Siguiente salida",
      value =
          nextSchedule?.let { "${it.departureTime} • ${route.name.toDriverDisplayName()}" }
              ?: "No hay más rutas asignadas hoy",
      iconTint = nextShiftStatusColor,
      statusLabel = nextShiftStatusLabel,
      statusColor = nextShiftStatusColor,
      statusContainer = nextShiftStatusContainer,
  )

  DriverIncidentCard()

  DriverHistoryCard(history = tripHistory)
}

@Composable
private fun DriverNoMoreRoutesCard() {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(28.dp),
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE8E9EE)),
      shadowElevation = 2.dp,
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Text(
          text = "JORNADA",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF8B8E95),
          fontWeight = FontWeight.SemiBold,
      )
      Text(
          text = "No hay más rutas asignadas hoy",
          style = MaterialTheme.typography.headlineSmall,
          color = Color(0xFF1F2126),
          fontWeight = FontWeight.Bold,
      )
      Text(
          text = "Ya completaste las salidas programadas para este día.",
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFF6F737B),
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
    onPrimaryAction: () -> Unit,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(28.dp),
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE8E9EE)),
      shadowElevation = 2.dp,
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
            text = "RUTA ACTUAL",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF8B8E95),
            fontWeight = FontWeight.SemiBold,
        )
        StatusChip(
            label = statusLabel,
            containerColor = statusContainer,
            contentColor = statusColor,
        )
      }
      Text(
          text = route.name.toDriverDisplayName(),
          style = MaterialTheme.typography.headlineSmall,
          color = Color(0xFF1F2126),
          fontWeight = FontWeight.Bold,
      )
      DriverQuickInfo(
          label = "Hora de salida",
          value = schedule?.departureTime ?: "No disponible",
      )
      PillActionButton(
          text = if (isTripRunning) "FINALIZAR RUTA" else "INICIAR RUTA",
          onClick = onPrimaryAction,
          modifier = Modifier.fillMaxWidth(),
          containerColor = Color(0xFF1B1D23),
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
        text = label.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF8B8E95),
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = value,
        style = MaterialTheme.typography.titleLarge,
        color = Color(0xFF1F2126),
        fontWeight = FontWeight.SemiBold,
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
      shape = RoundedCornerShape(22.dp),
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE8E9EE)),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Box(
          modifier = Modifier.size(44.dp),
          contentAlignment = Alignment.Center,
      ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = Color(0xFFF6F7FA),
        ) {}
        icon()
      }
      Text(
          text = title.uppercase(),
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF8B8E95),
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center,
      )
      Text(
          text = value,
          style = MaterialTheme.typography.titleMedium,
          color = Color(0xFF1F2126),
          fontWeight = FontWeight.Bold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          textAlign = TextAlign.Center,
      )
    }
  }
}

@Composable
private fun DriverTripStatusCard(
    elapsedTime: String,
    isTripRunning: Boolean,
    tripCompleted: Boolean,
    startedAt: String?,
    endedAt: String?,
    modifier: Modifier = Modifier,
) {
  Surface(
      modifier = modifier,
      shape = RoundedCornerShape(22.dp),
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE8E9EE)),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Box(
          modifier = Modifier.size(44.dp),
          contentAlignment = Alignment.Center,
      ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = Color(0xFFF6F7FA),
        ) {}
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color(0xFF5A5E66),
        )
      }
      Text(
          text = if (isTripRunning) "En ruta" else "Recorrido",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF8B8E95),
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center,
      )
      Text(
          text = elapsedTime,
          style = MaterialTheme.typography.titleMedium,
          color = Color(0xFF1F2126),
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
      )
      Text(
          text =
              when {
                isTripRunning -> "Ruta en curso"
                tripCompleted -> "Ruta finalizada"
                else -> "Aun no ha iniciado"
              },
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF6F737B),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          textAlign = TextAlign.Center,
      )
      if (startedAt != null || endedAt != null) {
        Text(
            text =
                when {
                  startedAt != null && endedAt != null -> "$startedAt - $endedAt"
                  startedAt != null -> "Inicio $startedAt"
                  else -> "Fin $endedAt"
                },
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF8B8E95),
            textAlign = TextAlign.Center,
        )
      }
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
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE8E9EE)),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF8B8E95),
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF1F2126),
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
      }
      Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        StatusChip(
            label = statusLabel,
            containerColor = statusContainer,
            contentColor = statusColor,
        )
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE2E5EA)),
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun StatusChip(
    label: String,
    containerColor: Color,
    contentColor: Color,
) {
  Surface(
      shape = RoundedCornerShape(999.dp),
      color = containerColor,
  ) {
    Text(
        text = label.uppercase(),
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        style = MaterialTheme.typography.labelSmall,
        color = contentColor,
        fontWeight = FontWeight.SemiBold,
    )
  }
}

@Composable
private fun DriverIncidentCard() {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE8E9EE)),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Text(
          text = "REPORTE DE INCIDENCIAS",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF8B8E95),
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
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE8E9EE)),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Text(
          text = "HISTÓRICO",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF8B8E95),
          fontWeight = FontWeight.SemiBold,
      )

      if (history.isEmpty()) {
        Text(
            text = "Aún no hay recorridos finalizados en esta sesión.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6F737B),
        )
      } else {
        history.forEach { item ->
          Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(18.dp),
              color = Color(0xFFF9FAFB),
              border = BorderStroke(1.dp, Color(0xFFE8E9EE)),
          ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              Text(
                  text = item.routeName,
                  style = MaterialTheme.typography.titleSmall,
                  color = Color(0xFF1F2126),
                  fontWeight = FontWeight.SemiBold,
              )
              Text(
                  text = "Salida programada ${item.departureTime}",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color(0xFF6F737B),
              )
              Text(
                  text = "Inicio ${item.startedAt} · Fin ${item.endedAt}",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color(0xFF6F737B),
              )
            }
          }
        }
      }
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
      modifier = modifier.height(108.dp),
      shape = RoundedCornerShape(22.dp),
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE8E9EE)),
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Surface(
          modifier = Modifier.size(44.dp),
          shape = CircleShape,
          color = Color.White,
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
          color = Color(0xFF666A72),
          fontWeight = FontWeight.Medium,
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
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE5E6EB)),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Text(
          text = title.uppercase(),
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF8B8E95),
          fontWeight = FontWeight.Medium,
      )
      Text(
          text = body,
          style = MaterialTheme.typography.titleMedium,
          color = Color(0xFF1F2126),
          fontWeight = FontWeight.SemiBold,
      )
      supporting?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6F737B),
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
      color = Color.White,
      border = BorderStroke(1.dp, Color(0xFFE5E6EB)),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      CircularProgressIndicator(
          modifier = Modifier.size(22.dp),
          strokeWidth = 2.5.dp,
          color = Color(0xFF1F2126),
      )
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Cargando ruta",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF1F2126),
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Estamos buscando la ruta asignada al conductor autenticado.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6F737B),
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

private fun String.toDriverDisplayName(): String =
    lowercase()
        .split(" ", "-", "/")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
          word.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
          }
        }
