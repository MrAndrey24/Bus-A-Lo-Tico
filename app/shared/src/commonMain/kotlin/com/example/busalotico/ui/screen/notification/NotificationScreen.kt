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
package com.example.busalotico.ui.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ReportProblem
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
import com.example.sharedlogic.domain.entities.UserIncidentNotification
import com.example.sharedlogic.presentation.notification.NotificationViewModel
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
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
            },
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
  val accent = notification.notificationAccent()
  val title = notification.notificationTitle()
  val isUnread = notification.readAt == null
  Card(
      modifier =
          Modifier.fillMaxWidth().clickable(enabled = isUnread) { onMarkAsRead(notification.id) },
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
      Surface(
          modifier = Modifier.size(38.dp),
          shape = RoundedCornerShape(999.dp),
          color = accent.iconBackground.copy(alpha = 0.32f),
          shadowElevation = 0.dp,
          tonalElevation = 0.dp,
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
              imageVector = accent.icon,
              contentDescription = null,
              tint = accent.barColor,
              modifier = Modifier.size(18.dp),
          )
        }
      }

      Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
          Column(
              modifier = Modifier.weight(1f),
              verticalArrangement = Arrangement.spacedBy(4.dp),
          ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
            )
            Text(
                text = notification.sanitizedDetail,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                lineHeight = 17.sp,
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column(
              horizontalAlignment = Alignment.End,
              verticalArrangement = Arrangement.spacedBy(6.dp),
          ) {
            Text(
                text = formatNotificationDateTime(notification.dispatchedAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              if (isUnread) {
                Box(
                    modifier =
                        Modifier.size(8.dp)
                            .background(
                                color = accent.barColor,
                                shape = RoundedCornerShape(999.dp),
                            )
                )
              }
              IconButton(
                  onClick = { onDelete(notification.id) },
                  modifier = Modifier.size(20.dp),
              ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Eliminar notificación",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp),
                )
              }
            }
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
    "ACCIDENTE" ->
        NotificationAccent(
            barColor = Color(0xFFE04B4B),
            iconBackground = Color(0xFFFDECEC),
            icon = Icons.Default.ReportProblem,
        )
    "AVERÍA",
    "AVERIA" ->
        NotificationAccent(
            barColor = Color(0xFFFFA07A),
            iconBackground = Color(0xFFFFF0EA),
            icon = Icons.Default.Build,
        )
    "SEGURIDAD" ->
        NotificationAccent(
            barColor = Color(0xFF1F4B8F),
            iconBackground = Color(0xFFEAF0FB),
            icon = Icons.Default.Security,
        )
    else ->
        NotificationAccent(
            barColor = Color(0xFF51677F),
            iconBackground = Color(0xFFEAF0F5),
            icon = Icons.Default.ReportProblem,
        )
  }
}

private fun UserIncidentNotification.notificationTitle(): String {
  return when (incidentType.trim().uppercase()) {
    "ACCIDENTE" -> "Incidente de tránsito reportado"
    "AVERÍA",
    "AVERIA" -> "Unidad fuera de servicio"
    "SEGURIDAD" -> "Incidente de seguridad"
    else -> "Actualización de ruta"
  }
}

fun formatNotificationDateTime(epochMilliseconds: Long): String {
  val instant = Instant.fromEpochMilliseconds(epochMilliseconds)
  val dateTime = instant.toLocalDateTime(TimeZone.of("America/Costa_Rica"))
  val day = dateTime.day.toString().padStart(2, '0')
  val month = dateTime.month.number.toString().padStart(2, '0')
  val year = dateTime.year
  val hour24 = dateTime.hour
  val hour12 =
      when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
      }
  val minute = dateTime.minute.toString().padStart(2, '0')
  val meridiem = if (hour24 >= 12) "PM" else "AM"
  return "${hour12.toString().padStart(2, '0')}:$minute $meridiem - $day/$month/$year"
}
