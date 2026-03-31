package com.example.shared.data.remote.trip.dto

import com.example.shared.domain.entities.TripExecution
import com.example.shared.domain.entities.TripExecutionStatus
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
            status = TripExecutionStatus.entries.firstOrNull { it.name == status } ?: TripExecutionStatus.IN_PROGRESS,
            hadIncidents = hadIncidents,
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
            )
        }
    }
}
