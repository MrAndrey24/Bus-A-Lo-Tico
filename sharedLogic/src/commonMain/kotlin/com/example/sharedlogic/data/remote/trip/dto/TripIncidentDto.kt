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
package com.example.sharedlogic.data.remote.trip.dto

import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.entities.TripIncident
import com.example.sharedlogic.domain.entities.enums.TripIncidentType
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
