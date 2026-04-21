/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.trip.dto

import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.TripIncident
import com.example.shared.domain.entities.TripIncidentType
import kotlinx.serialization.Serializable

@Serializable
data class TripIncidentDto(
    val type: String = TripIncidentType.SECURITY.name,
    val detail: String = "",
    val createdAt: Long = 0L,
    val location: TripIncidentLocationDto? = null,
) {
  fun toDomain(documentId: String): TripIncident {
    return TripIncident(
        id = documentId,
        type =
            TripIncidentType.entries.firstOrNull { it.name == type } ?: TripIncidentType.SECURITY,
        detail = detail,
        createdAt = createdAt,
        location = location?.toDomain(),
    )
  }

  companion object {
    fun fromDomain(incident: TripIncident): TripIncidentDto {
      return TripIncidentDto(
          type = incident.type.name,
          detail = incident.detail,
          createdAt = incident.createdAt,
          location = incident.location?.let(TripIncidentLocationDto::fromDomain),
      )
    }
  }
}

@Serializable
data class TripIncidentLocationDto(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
) {
  fun toDomain(): Coordinate = Coordinate(latitude = latitude, longitude = longitude)

  companion object {
    fun fromDomain(coordinate: Coordinate): TripIncidentLocationDto {
      return TripIncidentLocationDto(
          latitude = coordinate.latitude,
          longitude = coordinate.longitude,
      )
    }
  }
}
