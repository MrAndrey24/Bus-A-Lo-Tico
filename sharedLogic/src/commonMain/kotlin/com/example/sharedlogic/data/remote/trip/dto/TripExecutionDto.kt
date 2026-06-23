/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
