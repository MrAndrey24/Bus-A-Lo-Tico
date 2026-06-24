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
package com.example.busalotico.ui.screen.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.presentation.route.admin.AdministrativeRouteRuntimeStatus
import com.example.sharedlogic.presentation.route.admin.AdministrativeRouteStatus
import com.example.sharedlogic.presentation.route.admin.AdministrativeScheduleRuntimeStatus
import com.example.sharedlogic.presentation.route.admin.RouteAdministrativeDetailViewModel
import com.example.sharedlogic.util.currentCostaRicaTimeMinutes
import com.example.sharedlogic.util.parseFlexibleTimeToMinutes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RouteAdministrativeDetailScreen(
    routeId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: RouteAdministrativeDetailViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsState()

  LaunchedEffect(routeId) { viewModel.loadRoute(routeId) }

  Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Column(modifier = Modifier.fillMaxSize()) {
      AdministrativeTopBar(onBackClick = onBackClick)

      when {
        uiState.isLoading -> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
          }
        }

        uiState.errorMessage != null -> {
          Box(
              modifier = Modifier.fillMaxSize().padding(24.dp),
              contentAlignment = Alignment.Center,
          ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
            ) {
              Text(
                  text = uiState.errorMessage ?: "Error desconocido",
                  modifier = Modifier.padding(16.dp),
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onErrorContainer,
                  textAlign = TextAlign.Center,
              )
            }
          }
        }

        uiState.route != null -> {
          AdministrativeDetailContent(
              route = uiState.route!!,
              driverNamesById = uiState.driverNamesById,
              routeRuntimeStatus = uiState.routeRuntimeStatus,
              scheduleStatusesById = uiState.scheduleStatusesById,
              highlightedScheduleId = uiState.highlightedScheduleId,
              modifier = Modifier.weight(1f),
          )
        }
      }
    }
  }
}

@Composable
private fun AdministrativeTopBar(onBackClick: () -> Unit) {
  ScreenHeader(
      title = "Información administrativa",
      subtitle = "Detalle",
      leadingContent = {
        IconButton(onClick = onBackClick) {
          Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Volver",
              tint = MaterialTheme.colorScheme.onSurface,
          )
        }
      },
  )
}

@Composable
private fun AdministrativeDetailContent(
    route: Route,
    driverNamesById: Map<String, String>,
    routeRuntimeStatus: AdministrativeRouteRuntimeStatus,
    scheduleStatusesById: Map<String, AdministrativeScheduleRuntimeStatus>,
    highlightedScheduleId: String?,
    modifier: Modifier = Modifier,
) {
  LazyColumn(
      modifier = modifier.fillMaxWidth(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item { AdministrativeHeroCard(route, routeRuntimeStatus) }
    item { AdministrativeMetricsSection(route) }
    item {
      AdministrativeScheduleCard(
          route = route,
          driverNamesById = driverNamesById,
          scheduleStatusesById = scheduleStatusesById,
          highlightedScheduleId = highlightedScheduleId,
      )
    }
    item { AdministrativeFooter(route) }
    item { Spacer(modifier = Modifier.height(24.dp)) }
  }
}

@Composable
private fun AdministrativeHeroCard(
    route: Route,
    routeRuntimeStatus: AdministrativeRouteRuntimeStatus,
) {
  Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text(
        text = "RUTA ACTIVA",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.72f),
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
    )

    Text(
        text = route.name.trim().uppercase().replace(" - ", " • "),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.ExtraBold,
        lineHeight = 24.sp,
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Box(
          modifier =
              Modifier.size(7.dp).background(routeRuntimeStatus.state.dotColor(), CircleShape)
      )
      Text(
          text = routeRuntimeStatus.detail,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
      )
    }
  }
}

@Composable
private fun AdministrativeMetricsSection(route: Route) {
  Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        SectionLabel(
            text = "Tarifa regular",
            icon = Icons.Default.Payments,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = route.fareCrc?.let { "₡$it" } ?: "N/D",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
      }

      Box(
          modifier =
              Modifier.width(1.dp)
                  .height(64.dp)
                  .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f))
      )

      Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        SectionLabel(
            text = "Medios de pago",
            icon = Icons.Default.CreditCard,
            color = MaterialTheme.colorScheme.primary,
        )
        PaymentMethodLine(
            text = "Efectivo",
            isAvailable = true,
        )
        if (route.acceptsCardPayment) {
          PaymentMethodLine(
              text = "Tarjeta",
              isAvailable = true,
          )
        }
      }
    }
  }
}

@Composable
private fun SectionLabel(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
) {
  Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color.copy(alpha = 0.82f),
        modifier = Modifier.size(18.dp),
    )
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color.copy(alpha = 0.82f),
        fontWeight = FontWeight.SemiBold,
    )
  }
}

@Composable
private fun PaymentMethodLine(
    text: String,
    isAvailable: Boolean,
) {
  Text(
      text = text,
      style = MaterialTheme.typography.bodyMedium,
      color =
          if (isAvailable) MaterialTheme.colorScheme.onSurface
          else MaterialTheme.colorScheme.onSurfaceVariant,
      fontWeight = if (isAvailable) FontWeight.SemiBold else FontWeight.Normal,
  )
}

@Composable
private fun AdministrativeScheduleCard(
    route: Route,
    driverNamesById: Map<String, String>,
    scheduleStatusesById: Map<String, AdministrativeScheduleRuntimeStatus>,
    highlightedScheduleId: String?,
) {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(
        text = "Horarios de salida",
        modifier = Modifier.padding(horizontal = 4.dp),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
    )

    if (route.schedules.isEmpty()) {
      Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors =
              CardDefaults.cardColors(
                  containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
              ),
      ) {
        Text(
            text = "No hay horarios registrados.",
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
      }
    } else {
      route.schedules
          .sortedBy { it.departureTime }
          .forEachIndexed { index, schedule ->
            ScheduleListItem(
                schedule = schedule,
                driverName = driverNamesById[schedule.driverId],
                scheduleStatus =
                    scheduleStatusesById[schedule.id] ?: rememberScheduleStatus(schedule),
                isHighlighted = highlightedScheduleId?.let { it == schedule.id } ?: (index == 0),
            )
          }
    }
  }
}

@Composable
private fun ScheduleListItem(
    schedule: RouteSchedule,
    driverName: String?,
    scheduleStatus: AdministrativeScheduleRuntimeStatus,
    isHighlighted: Boolean,
) {
  val (timeText, meridiemText) = schedule.departureTime.toMeridiemParts()
  val (arrivalTimeText, arrivalMeridiemText) = schedule.arrivalTime.toMeridiemParts()
  val statusColor = scheduleStatus.color()
  val statusLabel = scheduleStatus.label()
  Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.Top,
      ) {
        Column(
            modifier = Modifier.width(74.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
          Text(
              text = timeText,
              style = MaterialTheme.typography.titleLarge,
              color = MaterialTheme.colorScheme.onSurface,
              fontWeight = FontWeight.ExtraBold,
          )
          Text(
              text = meridiemText,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.8.sp,
          )
          if (isHighlighted) {
            Text(
                text = "PRÓXIMO",
                style = MaterialTheme.typography.labelSmall,
                color = statusColor,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp,
            )
          }
        }

        Box(
            modifier =
                Modifier.padding(top = 4.dp)
                    .width(3.dp)
                    .height(72.dp)
                    .background(statusColor, RoundedCornerShape(999.dp))
        )

        Column(
            modifier = Modifier.weight(1f).padding(top = 2.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
          Text(
              text = statusLabel,
              style = MaterialTheme.typography.labelSmall,
              color = statusColor,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.8.sp,
          )
          Text(
              text = buildArrivalLabel(arrivalTimeText, arrivalMeridiemText),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface,
              fontWeight = FontWeight.SemiBold,
          )
          Text(
              text = "Conductor",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.SemiBold,
          )
          Text(
              text =
                  driverName
                      ?: schedule.driverId.takeIf { it.isNotBlank() }
                      ?: "Chofer no asignado",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.Medium,
          )
        }
      }
    }
  }
}

@Composable
private fun AdministrativeFooter(route: Route) {
  Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Icon(
        imageVector = Icons.Outlined.AccountBalance,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        modifier = Modifier.size(20.dp),
    )
    Text(
        text = "OPERADO POR",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
    )
    Text(
        text = route.operator.toDisplayTitle(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
  }
}

private fun String.toDisplayTitle(): String {
  return trim().lowercase().split(Regex("\\s+")).joinToString(" ") { word ->
    word.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase() else char.toString() }
  }
}

private fun rememberScheduleStatus(schedule: RouteSchedule): AdministrativeScheduleRuntimeStatus {
  val currentMinutes = currentCostaRicaTimeMinutes()
  val departureMinutes = parseFlexibleTimeToMinutes(schedule.departureTime)
  val arrivalMinutes = parseFlexibleTimeToMinutes(schedule.arrivalTime)

  if (departureMinutes == null || arrivalMinutes == null)
      return AdministrativeScheduleRuntimeStatus.SCHEDULED

  return when {
    currentMinutes > arrivalMinutes -> AdministrativeScheduleRuntimeStatus.COMPLETED
    currentMinutes in departureMinutes..arrivalMinutes -> AdministrativeScheduleRuntimeStatus.ACTIVE
    else -> AdministrativeScheduleRuntimeStatus.SCHEDULED
  }
}

private fun String.toMeridiemParts(): Pair<String, String> {
  val parts = split(":")
  val hour24 = parts.firstOrNull()?.toIntOrNull() ?: return this to ""
  val minute = parts.getOrNull(1) ?: return this to ""
  val meridiem = if (hour24 >= 12) "PM" else "AM"
  val hour12 =
      when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
      }
  return "%02d:%s".format(hour12, minute) to meridiem
}

private fun AdministrativeScheduleRuntimeStatus.label(): String =
    when (this) {
      AdministrativeScheduleRuntimeStatus.COMPLETED -> "COMPLETADO"
      AdministrativeScheduleRuntimeStatus.ACTIVE -> "ACTIVO"
      AdministrativeScheduleRuntimeStatus.DELAYED -> "DETENIDO"
      AdministrativeScheduleRuntimeStatus.SCHEDULED -> "PROGRAMADO"
    }

private fun AdministrativeScheduleRuntimeStatus.color(): Color =
    when (this) {
      AdministrativeScheduleRuntimeStatus.COMPLETED -> Color(0xFF6F7D8C)
      AdministrativeScheduleRuntimeStatus.ACTIVE -> Color(0xFF25B48F)
      AdministrativeScheduleRuntimeStatus.DELAYED -> Color(0xFFD67C2E)
      AdministrativeScheduleRuntimeStatus.SCHEDULED -> Color(0xFF294A6B)
    }

private fun AdministrativeRouteStatus.dotColor(): Color =
    when (this) {
      AdministrativeRouteStatus.IDLE -> Color(0xFF6F7D8C)
      AdministrativeRouteStatus.ACTIVE -> Color(0xFF32B45F)
      AdministrativeRouteStatus.DELAYED -> Color(0xFFD67C2E)
    }

private fun buildArrivalLabel(timeText: String, meridiemText: String): String {
  val time = timeText.ifBlank { "N/D" }
  return if (meridiemText.isBlank()) {
    "Llegada $time"
  } else {
    "Llegada $time $meridiemText"
  }
}
