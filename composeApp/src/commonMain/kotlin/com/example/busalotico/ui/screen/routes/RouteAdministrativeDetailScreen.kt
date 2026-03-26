package com.example.busalotico.ui.screen.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Schedule
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
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.presentation.route.RouteAdministrativeDetailViewModel
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
      ScreenHeader(
          title = "Información administrativa",
          subtitle = "Detalle de ruta",
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

      when {
        uiState.isLoading -> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Cargando ficha administrativa...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }

        uiState.errorMessage != null -> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = uiState.errorMessage ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
          }
        }

        uiState.route != null -> {
          AdministrativeDetailContent(
              route = uiState.route!!,
              driverNamesById = uiState.driverNamesById,
              modifier = Modifier.weight(1f),
          )
        }
      }
    }
  }
}

@Composable
private fun AdministrativeDetailContent(
    route: Route,
    driverNamesById: Map<String, String>,
    modifier: Modifier = Modifier,
) {
  LazyColumn(
      modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item { AdministrativeHeroCard(route) }
    item { AdministrativeMetricsCard(route) }
    item { AdministrativePaymentCard(route) }
    item { AdministrativeLocationCard(route) }
    item { AdministrativeScheduleCard(route.schedules, driverNamesById) }
    item { AdministrativeFooter(route) }
    item { Spacer(modifier = Modifier.height(12.dp)) }
  }
}

@Composable
private fun AdministrativeHeroCard(route: Route) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Surface(shape = RoundedCornerShape(999.dp), color = MaterialTheme.colorScheme.primary) {
        Text(
            text = "RUTA ${route.code}",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold,
        )
      }

      Text(
          text = route.name.trim().uppercase(),
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Medium,
      )
    }
  }
}

@Composable
private fun AdministrativeMetricsCard(route: Route) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      AdministrativeMetricBlock(
          modifier = Modifier.weight(1f),
          label = "Tarifa actual",
          value = route.fareCrc?.let { "₡$it" } ?: "N/D",
          supporting = "por viaje",
      )
      AdministrativeMetricBlock(
          modifier = Modifier.weight(1f),
          label = "Próximo bus",
          value = route.etaText.ifBlank { "N/D" },
          supporting = if (route.isLive) "en tiempo real" else "estimado",
      )
    }
  }
}

@Composable
private fun AdministrativeMetricBlock(
    label: String,
    value: String,
    supporting: String,
    modifier: Modifier = Modifier,
) {
  Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
    Text(
        text = label.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = value,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.SemiBold,
    )
    Text(
        text = supporting,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun AdministrativeLocationCard(route: Route) {
  val startLocation =
      listOfNotNull(
              route.startLocation.province?.takeIf { it.isNotBlank() }?.toDisplayTitle(),
              route.startLocation.canton?.takeIf { it.isNotBlank() }?.toDisplayTitle(),
              route.startLocation.district?.takeIf { it.isNotBlank() }?.toDisplayTitle(),
          )
          .joinToString(", ")

  val endLocation =
      listOfNotNull(
              route.endLocation.province?.takeIf { it.isNotBlank() }?.toDisplayTitle(),
              route.endLocation.canton?.takeIf { it.isNotBlank() }?.toDisplayTitle(),
              route.endLocation.district?.takeIf { it.isNotBlank() }?.toDisplayTitle(),
          )
          .joinToString(", ")

  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
          text = "Ubicación administrativa",
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.SemiBold,
      )

      if (startLocation.isBlank() && endLocation.isBlank()) {
        Text(
            text = "No disponible",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          if (startLocation.isNotBlank()) {
            AdministrativeLocationLine(
                label = "Inicio",
                value = startLocation,
            )
          }
          if (endLocation.isNotBlank()) {
            AdministrativeLocationLine(
                label = "Final",
                value = endLocation,
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AdministrativeLocationLine(label: String, value: String) {
  Row(
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Icon(
        imageVector = Icons.Filled.LocationOn,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.height(16.dp),
    )
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
      Text(
          text = label.uppercase(),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
      )
      Text(
          text = value,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface,
      )
    }
  }
}

@Composable
private fun AdministrativePaymentCard(route: Route) {
  val paymentMethods =
      if (route.acceptsCardPayment) listOf("Tarjeta", "Efectivo") else listOf("Efectivo")

  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Text(
          text = "Métodos de pago",
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.SemiBold,
      )

      FlowRow(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        paymentMethods.forEach { paymentMethod ->
          Surface(
              shape = RoundedCornerShape(14.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
          ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              if (paymentMethod == "Tarjeta") {
                Icon(
                    imageVector = Icons.Filled.CreditCard,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              } else {
                Icon(
                    imageVector = Icons.Filled.Payments,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
              Text(
                  text = paymentMethod,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.SemiBold,
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun AdministrativeScheduleCard(
    schedules: List<RouteSchedule>,
    driverNamesById: Map<String, String>,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
          text = "Horarios de salida",
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.SemiBold,
      )

      if (schedules.isEmpty()) {
        Text(
            text = "No hay horarios registrados para esta ruta.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      } else {
        schedules.sortedBy { it.departureTime }.forEachIndexed { _, schedule ->
          AdministrativeScheduleItem(
              schedule = schedule,
              supportingText = "Llegada ${schedule.arrivalTime.ifBlank { "No disponible" }}",
              driverName = driverNamesById[schedule.driverId],
          )
        }
      }
    }
  }
}

@Composable
private fun AdministrativeScheduleItem(
    schedule: RouteSchedule,
    supportingText: String,
    driverName: String?,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surface,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
      Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Surface(
            modifier = Modifier.height(56.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
          Box(
              modifier = Modifier.padding(horizontal = 16.dp),
              contentAlignment = Alignment.Center,
          ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
          Text(
              text = schedule.departureTime,
              style = MaterialTheme.typography.titleLarge,
              color = MaterialTheme.colorScheme.onSurface,
              fontWeight = FontWeight.SemiBold,
          )
          Text(
              text = supportingText,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          (driverName ?: schedule.driverId.takeIf { it.isNotBlank() })?.let { driverLabel ->
            Text(
                text = "Conductor $driverLabel",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AdministrativeFooter(route: Route) {
  Column(
      modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    Text(
        text = "OPERADO POR",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = route.operator.toDisplayTitle(),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
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
