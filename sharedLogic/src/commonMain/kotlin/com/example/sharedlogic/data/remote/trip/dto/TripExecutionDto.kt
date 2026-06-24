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

import com.example.sharedlogic.domain.entities.TripExecution
import com.example.sharedlogic.domain.entities.enums.TripExecutionStatus
import kotlinx.serialization.Serializable

@Serializable
data class TripExecutionDto(
    val scheduleId: String = "",
    val routeId: String = "",
    val driverId: String = "",
    val startedAt: Long = 0L,
    val endedAt: Long? = null,
    val durationSeconds: Int? = null,
    val status: String = TripExecutionStatus.IN_PROGRESS.name,
    val hadIncidents: Boolean = false,
    val transferredToDriverId: String? = null,
    val transferredAt: Long? = null,
) {
  fun toDomain(documentId: String): TripExecution {
    return TripExecution(
        id = documentId,
        scheduleId = scheduleId,
        routeId = routeId,
        driverId = driverId,
        startedAt = startedAt,
        endedAt = endedAt,
        durationSeconds = durationSeconds,
        status =
            TripExecutionStatus.entries.firstOrNull { it.name == status }
                ?: TripExecutionStatus.IN_PROGRESS,
        hadIncidents = hadIncidents,
        transferredToDriverId = transferredToDriverId,
        transferredAt = transferredAt,
    )
  }

  companion object {
    fun fromDomain(tripExecution: TripExecution): TripExecutionDto {
      return TripExecutionDto(
          scheduleId = tripExecution.scheduleId,
          routeId = tripExecution.routeId,
          driverId = tripExecution.driverId,
          startedAt = tripExecution.startedAt,
          endedAt = tripExecution.endedAt,
          durationSeconds = tripExecution.durationSeconds,
          status = tripExecution.status.name,
          hadIncidents = tripExecution.hadIncidents,
          transferredToDriverId = tripExecution.transferredToDriverId,
          transferredAt = tripExecution.transferredAt,
      )
    }
  }
}
