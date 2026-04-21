/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.presentation.notification

import com.example.shared.domain.entities.TripIncidentType
import com.example.shared.domain.entities.UserIncidentNotification

internal fun UserIncidentNotification.toDisplayNotification(): UserIncidentNotification {
  val normalizedType = incidentType.toNotificationDisplayLabel()
  val routeReference = routeName.ifBlank { routeId }
  return copy(
      incidentType = normalizedType,
      routeId = routeReference,
      sanitizedDetail = sanitizedDetail.toNotificationMessage(incidentType, routeReference),
  )
}

internal fun TripIncidentType.toNotificationDisplayLabel(): String {
  return when (this) {
    TripIncidentType.ACCIDENT -> "Accidente"
    TripIncidentType.BREAKDOWN -> "Avería"
    TripIncidentType.SECURITY -> "Seguridad"
  }
}

internal fun TripIncidentType.toNotificationMessage(
    routeName: String = "",
    scheduledTimeLabel: String? = null,
): String {
  return when (this) {
    TripIncidentType.ACCIDENT ->
        routeName.buildNotificationMessage("Se ha reportado un accidente", scheduledTimeLabel)
    TripIncidentType.BREAKDOWN ->
        routeName.buildNotificationMessage("Se ha reportado una avería", scheduledTimeLabel)
    TripIncidentType.SECURITY ->
        routeName.buildNotificationMessage(
            "Se ha reportado un incidente de seguridad",
            scheduledTimeLabel,
        )
  }
}

internal fun String.toNotificationDisplayLabel(): String {
  return when (toNotificationTypeKey()) {
    NotificationTypeKey.ACCIDENT -> "Accidente"
    NotificationTypeKey.BREAKDOWN -> "Avería"
    NotificationTypeKey.SECURITY -> "Seguridad"
    NotificationTypeKey.UNKNOWN -> trim().ifBlank { "Incidente" }
  }
}

internal fun String.toNotificationMessage(
    originalIncidentType: String,
    routeName: String = "",
): String {
  val scheduleSuffix = extractScheduleSuffix()
  return when (originalIncidentType.toNotificationTypeKey()) {
    NotificationTypeKey.ACCIDENT ->
        routeName.buildNotificationMessage("Se ha reportado un accidente", scheduleSuffix)
    NotificationTypeKey.BREAKDOWN ->
        if (routeName.isBlank() && trim().isNotBlank()) trim()
        else routeName.buildNotificationMessage("Se ha reportado una avería", scheduleSuffix)
    NotificationTypeKey.SECURITY ->
        if (routeName.isBlank() && trim().isNotBlank()) trim()
        else
            routeName.buildNotificationMessage(
                "Se ha reportado un incidente de seguridad",
                scheduleSuffix,
            )
    NotificationTypeKey.UNKNOWN ->
        trim().ifBlank {
          routeName.buildNotificationMessage("Se ha reportado un incidente", scheduleSuffix)
        }
  }
}

private fun String.buildNotificationMessage(
    prefix: String,
    scheduledTimeLabel: String? = null,
): String {
  val routeLabel = trim()
  val scheduleSuffix =
      scheduledTimeLabel
          ?.trim()
          ?.takeIf { it.isNotBlank() }
          ?.let { " para el horario de las $it" }
          .orEmpty()
  return if (routeLabel.isBlank()) {
    "$prefix en la ruta$scheduleSuffix."
  } else {
    "$prefix en la ruta $routeLabel$scheduleSuffix."
  }
}

private fun String.toNotificationTypeKey(): NotificationTypeKey {
  return when (trim().normalizeNotificationToken()) {
    "ACCIDENT",
    "ACCIDENTE" -> NotificationTypeKey.ACCIDENT
    "BREAKDOWN",
    "AVERIA" -> NotificationTypeKey.BREAKDOWN
    "SECURITY",
    "SEGURIDAD" -> NotificationTypeKey.SECURITY
    else -> NotificationTypeKey.UNKNOWN
  }
}

private fun String.normalizeNotificationToken(): String {
  return uppercase()
      .replace('Á', 'A')
      .replace('É', 'E')
      .replace('Í', 'I')
      .replace('Ó', 'O')
      .replace('Ú', 'U')
}

private fun String.extractScheduleSuffix(): String? {
  val text = trim()
  val marker = " para el horario de las "
  val startIndex = text.indexOf(marker, ignoreCase = true)
  if (startIndex == -1) return null
  val rawSuffix = text.substring(startIndex + marker.length).removeSuffix(".").trim()
  return rawSuffix.ifBlank { null }
}

private enum class NotificationTypeKey {
  ACCIDENT,
  BREAKDOWN,
  SECURITY,
  UNKNOWN,
}
