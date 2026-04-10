package com.example.busalotico.ui.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

  Card(
      modifier =
          Modifier.fillMaxWidth().clickable(enabled = notification.readAt == null) {
            onMarkAsRead(notification.id)
          },
      shape = RoundedCornerShape(8.dp),
      colors =
          CardDefaults.cardColors(
              containerColor =
                  if (notification.readAt == null) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                  } else {
                    MaterialTheme.colorScheme.surface
                  }
          ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
            text = notification.incidentType,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        if (notification.readAt == null) {
          Box(
              modifier =
                  Modifier.background(
                          MaterialTheme.colorScheme.primary,
                          shape = RoundedCornerShape(50),
                      )
                      .padding(6.dp, 3.dp)
          ) {
            Text(
                text = "Nuevo",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
          }
        }
      }

      Text(
          text = notification.sanitizedDetail,
          style = MaterialTheme.typography.bodySmall,
          modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
      )

      Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        if (routeReference.isNotBlank()) {
          Text(
              text = "Ruta: $routeReference",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              text = formatNotificationTime(notification.dispatchedAt),
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          TextButton(onClick = { onDelete(notification.id) }) { Text("Eliminar") }
        }
      }
    }
  }
}

fun formatNotificationTime(epochMilliseconds: Long): String {
  val instant = Instant.fromEpochMilliseconds(epochMilliseconds)
  val dateTime = instant.toLocalDateTime(TimeZone.of("America/Costa_Rica"))
  val day = dateTime.dayOfMonth.toString().padStart(2, '0')
  val month = dateTime.monthNumber.toString().padStart(2, '0')
  val hour = dateTime.hour.toString().padStart(2, '0')
  val minute = dateTime.minute.toString().padStart(2, '0')
  return "$day/$month $hour:$minute"
}
