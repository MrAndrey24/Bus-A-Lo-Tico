package com.example.shared.presentation.notification

import com.example.shared.domain.entities.TripIncidentType
import com.example.shared.domain.entities.UserIncidentNotification

internal fun UserIncidentNotification.toDisplayNotification(): UserIncidentNotification {
  val normalizedType = incidentType.toNotificationDisplayLabel()
  val routeReference = routeId.ifBlank { routeName }
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

internal fun TripIncidentType.toNotificationMessage(routeName: String = ""): String {
  return when (this) {
    TripIncidentType.ACCIDENT -> routeName.buildNotificationMessage("Se ha reportado un accidente")
    TripIncidentType.BREAKDOWN -> routeName.buildNotificationMessage("Se ha reportado una avería")
    TripIncidentType.SECURITY -> routeName.buildNotificationMessage("Se ha reportado un incidente de seguridad")
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

internal fun String.toNotificationMessage(originalIncidentType: String, routeName: String = ""): String {
  return when (originalIncidentType.toNotificationTypeKey()) {
    NotificationTypeKey.ACCIDENT -> routeName.buildNotificationMessage("Se ha reportado un accidente")
    NotificationTypeKey.BREAKDOWN ->
        if (trim().isBlank()) routeName.buildNotificationMessage("Se ha reportado una avería") else trim()
    NotificationTypeKey.SECURITY ->
        if (trim().isBlank()) routeName.buildNotificationMessage("Se ha reportado un incidente de seguridad") else trim()
    NotificationTypeKey.UNKNOWN -> trim().ifBlank { routeName.buildNotificationMessage("Se ha reportado un incidente") }
  }
}

private fun String.buildNotificationMessage(prefix: String): String {
  val routeLabel = trim()
  return if (routeLabel.isBlank()) {
    "$prefix en la ruta."
  } else {
    "$prefix en la ruta $routeLabel."
  }
}

private fun String.toNotificationTypeKey(): NotificationTypeKey {
  return when (trim().normalizeNotificationToken()) {
    "ACCIDENT", "ACCIDENTE" -> NotificationTypeKey.ACCIDENT
    "BREAKDOWN", "AVERIA" -> NotificationTypeKey.BREAKDOWN
    "SECURITY", "SEGURIDAD" -> NotificationTypeKey.SECURITY
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

private enum class NotificationTypeKey {
  ACCIDENT,
  BREAKDOWN,
  SECURITY,
  UNKNOWN,
}