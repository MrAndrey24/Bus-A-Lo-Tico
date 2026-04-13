package com.example.busalotico.ui.screen.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.shared.domain.entities.UserIncidentNotification
import com.example.shared.presentation.notification.NotificationViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    userId: String,
    onBack: () -> Unit,
) {
  val uiState = viewModel.uiState.collectAsState().value

  LaunchedEffect(userId) { viewModel.loadNotifications(userId) }

  Scaffold(
      topBar = {
          ScreenHeader(
              title = "Notificaciones",
              subtitle = "Lista de notificaciones recibidas",
              leadingContent = {
                  IconButton(onClick = onBack) {
                      Icon(
                          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                          contentDescription = "Back",
                          tint = MaterialTheme.colorScheme.onSurface,
                      )
                  }
              }
          )
      },
      content = { paddingValues ->
        if (uiState.isLoading) {
          Box(
              modifier = Modifier.fillMaxSize().padding(paddingValues),
              contentAlignment = Alignment.Center,
          ) {
            Text("Cargando notificaciones...")
          }
        } else if (uiState.errorMessage != null) {
          Box(
              modifier = Modifier.fillMaxSize().padding(paddingValues),
              contentAlignment = Alignment.Center,
          ) {
            Text(
                text = uiState.errorMessage ?: "Error desconocido",
                style = MaterialTheme.typography.bodyMedium,
            )
          }
        } else if (uiState.notifications.isEmpty()) {
          Box(
              modifier = Modifier.fillMaxSize().padding(paddingValues),
              contentAlignment = Alignment.Center,
          ) {
            Text("No hay notificaciones")
          }
        } else {
          LazyColumn(
              modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            items(uiState.notifications) { notification ->
              NotificationItem(
                  notification = notification,
                  onMarkAsRead = { notificationId ->
                    viewModel.markNotificationAsRead(notificationId, userId)
                  },
                  onDelete = { notificationId ->
                    viewModel.deleteNotification(notificationId, userId)
                  },
              )
            }
          }
        }
      },
  )
}

@Composable
fun NotificationItem(
    notification: UserIncidentNotification,
    onMarkAsRead: (String) -> Unit,
  onDelete: (String) -> Unit,
) {
  val routeReference = notification.routeId.ifBlank { notification.routeName }
  val accent = notification.notificationAccent()
  val title = notification.notificationTitle()

  Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(0.dp),
  ) {
    Box(
        modifier = Modifier
            .width(4.dp)
            .height(174.dp)
            .background(accent.barColor, RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp))
    )
    Card(
        modifier =
            Modifier
                .weight(1f)
                .clickable(enabled = notification.readAt == null) {
                  onMarkAsRead(notification.id)
                },
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
      Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Surface(
                modifier = Modifier.size(26.dp),
                shape = RoundedCornerShape(8.dp),
                color = accent.iconBackground,
                border = BorderStroke(1.dp, accent.barColor.copy(alpha = 0.18f)),
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = accent.icon,
                    contentDescription = null,
                    tint = accent.barColor,
                    modifier = Modifier.size(14.dp),
                )
              }
            }
            Text(
                text = notification.incidentType.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = accent.barColor,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.6.sp,
            )
          }
          Text(
              text = formatNotificationTime(notification.dispatchedAt),
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.Medium,
          )
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(
              text = title,
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.onSurface,
              fontWeight = FontWeight.ExtraBold,
          )
          Text(
              text = notification.sanitizedDetail,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
          ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = routeReference.ifBlank { "Ruta no disponible" }.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
          }
          TextButton(onClick = { onDelete(notification.id) }) {
            Text(
                text = "ELIMINAR",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFD85757),
                fontWeight = FontWeight.ExtraBold,
            )
          }
        }

        if (notification.readAt == null) {
          Spacer(modifier = Modifier.height(2.dp))
          Box(
              modifier = Modifier
                  .background(accent.barColor, shape = RoundedCornerShape(999.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
                text = "Nuevo",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
            )
          }
        }
      }
    }
  }
}

private data class NotificationAccent(
    val barColor: Color,
    val iconBackground: Color,
    val icon: ImageVector,
)

private fun UserIncidentNotification.notificationAccent(): NotificationAccent {
  return when (incidentType.trim().uppercase()) {
    "ACCIDENTE" -> NotificationAccent(
        barColor = Color(0xFFE04B4B),
        iconBackground = Color(0xFFFDECEC),
        icon = Icons.Default.ReportProblem,
    )
    "AVERÍA", "AVERIA" -> NotificationAccent(
        barColor = Color(0xFFFFA07A),
        iconBackground = Color(0xFFFFF0EA),
        icon = Icons.Default.Build,
    )
    "SEGURIDAD" -> NotificationAccent(
        barColor = Color(0xFF1F4B8F),
        iconBackground = Color(0xFFEAF0FB),
        icon = Icons.Default.Security,
    )
    else -> NotificationAccent(
        barColor = Color(0xFF51677F),
        iconBackground = Color(0xFFEAF0F5),
        icon = Icons.Default.ReportProblem,
    )
  }
}

private fun UserIncidentNotification.notificationTitle(): String {
  return when (incidentType.trim().uppercase()) {
    "ACCIDENTE" -> "Incidente de tránsito reportado"
    "AVERÍA", "AVERIA" -> "Unidad fuera de servicio"
    "SEGURIDAD" -> "Incidente de seguridad"
    else -> "Actualización de ruta"
  }
}

fun formatNotificationTime(epochMilliseconds: Long): String {
  val instant = Instant.fromEpochMilliseconds(epochMilliseconds)
  val dateTime = instant.toLocalDateTime(TimeZone.of("America/Costa_Rica"))
  val hour24 = dateTime.hour
  val hour12 =
      when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
      }
  val minute = dateTime.minute.toString().padStart(2, '0')
  val meridiem = if (hour24 >= 12) "PM" else "AM"
  return "${hour12.toString().padStart(2, '0')}:$minute $meridiem"
}
