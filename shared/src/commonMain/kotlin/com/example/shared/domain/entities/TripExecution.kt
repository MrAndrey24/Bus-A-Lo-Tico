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
)

enum class TripExecutionStatus {
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
}
