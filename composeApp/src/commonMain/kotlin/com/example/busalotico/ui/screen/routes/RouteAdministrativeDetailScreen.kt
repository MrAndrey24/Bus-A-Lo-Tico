package com.example.busalotico.ui.screen.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.TripOrigin
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
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.presentation.route.RouteAdministrativeDetailViewModel
import com.example.shared.util.currentCostaRicaTimeMinutes
import com.example.shared.util.parseFlexibleTimeToMinutes
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
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
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
                                textAlign = TextAlign.Center
                            )
                        }
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
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { AdministrativeHeroCard(route) }
        item { AdministrativeMetricsSection(route) }
        item { AdministrativeScheduleCard(route = route, driverNamesById = driverNamesById) }
        item { AdministrativeFooter(route) }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun AdministrativeHeroCard(route: Route) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
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
                modifier = Modifier
                    .size(7.dp)
                    .background(Color(0xFF32B45F), CircleShape)
            )
            Text(
                text = "Servicio en horario regular",
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
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                modifier = Modifier
                    .width(1.dp)
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
            modifier = Modifier.size(18.dp)
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
        color = if (isAvailable) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = if (isAvailable) FontWeight.SemiBold else FontWeight.Normal,
    )
}

@Composable
private fun AdministrativeScheduleCard(
    route: Route,
    driverNamesById: Map<String, String>,
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "No hay horarios registrados.",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            route.schedules.sortedBy { it.departureTime }.forEachIndexed { index, schedule ->
                ScheduleListItem(
                    schedule = schedule,
                    driverName = driverNamesById[schedule.driverId],
                    isHighlighted = index == 0,
                )
            }
        }
    }
}

@Composable
private fun ScheduleListItem(
    schedule: RouteSchedule,
    driverName: String?,
    isHighlighted: Boolean,
) {
    val (timeText, meridiemText) = schedule.departureTime.toMeridiemParts()
    val (arrivalTimeText, arrivalMeridiemText) = schedule.arrivalTime.toMeridiemParts()
    val scheduleStatus = rememberScheduleStatus(schedule)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.width(74.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = "Salida",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = timeText,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Text(
                        text = meridiemText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Llegada:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = arrivalTimeText.ifBlank { "N/D" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                        )
                        if (arrivalMeridiemText.isNotBlank()) {
                            Text(
                                text = arrivalMeridiemText,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.6.sp,
                            )
                        }
                        ScheduleStatusChip(status = scheduleStatus)
                    }

                    Text(
                        text = "Conductor: ${driverName ?: schedule.driverId.takeIf { it.isNotBlank() } ?: "Chofer no asignado"}",
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
private fun ScheduleStatusChip(status: ScheduleStatus) {
    val background = when (status) {
        ScheduleStatus.COMPLETED -> Color(0xFFE9EEF5)
        ScheduleStatus.ACTIVE -> Color(0xFFE7F7EA)
        ScheduleStatus.SCHEDULED -> Color(0xFFEEF4FF)
    }
    val content = when (status) {
        ScheduleStatus.COMPLETED -> Color(0xFF6B7280)
        ScheduleStatus.ACTIVE -> Color(0xFF2F8D46)
        ScheduleStatus.SCHEDULED -> Color(0xFF295BA7)
    }
    val label = when (status) {
        ScheduleStatus.COMPLETED -> "COMPLETADO"
        ScheduleStatus.ACTIVE -> "ACTIVO"
        ScheduleStatus.SCHEDULED -> "PROGRAMADO"
    }

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = background,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = content,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

@Composable
private fun AdministrativeFooter(route: Route) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountBalance,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = "OPERADO POR",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp
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

private fun rememberScheduleStatus(schedule: RouteSchedule): ScheduleStatus {
    val currentMinutes = currentCostaRicaTimeMinutes()
    val departureMinutes = parseFlexibleTimeToMinutes(schedule.departureTime)
    val arrivalMinutes = parseFlexibleTimeToMinutes(schedule.arrivalTime)

    if (departureMinutes == null || arrivalMinutes == null) return ScheduleStatus.SCHEDULED

    return when {
        currentMinutes > arrivalMinutes -> ScheduleStatus.COMPLETED
        currentMinutes in departureMinutes..arrivalMinutes -> ScheduleStatus.ACTIVE
        else -> ScheduleStatus.SCHEDULED
    }
}

private fun String.toMeridiemParts(): Pair<String, String> {
    val parts = split(":")
    val hour24 = parts.firstOrNull()?.toIntOrNull() ?: return this to ""
    val minute = parts.getOrNull(1) ?: return this to ""
    val meridiem = if (hour24 >= 12) "PM" else "AM"
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    return "%02d:%s".format(hour12, minute) to meridiem
}

private enum class ScheduleStatus {
    SCHEDULED,
    ACTIVE,
    COMPLETED,
}
