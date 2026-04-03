package com.example.shared.domain.entities

data class TripExecution(
    val id: String = "",
    val scheduleId: String,
    val routeId: String,
    val driverId: String,
    val startedAt: Long,
    val endedAt: Long? = null,
    val durationSeconds: Int? = null,
    val status: TripExecutionStatus = TripExecutionStatus.IN_PROGRESS,
    val hadIncidents: Boolean = false,
    val transferredToDriverId: String? = null,
    val transferredAt: Long? = null,
)

enum class TripExecutionStatus {
    PENDING,
    IN_PROGRESS,
    DELAYED,
    TRANSFERRED,
    COMPLETED,
    CANCELLED,
}
