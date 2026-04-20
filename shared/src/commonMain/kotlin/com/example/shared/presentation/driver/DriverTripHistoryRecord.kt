package com.example.shared.presentation.driver

data class DriverTripHistoryRecord(
    val tripExecutionId: String,
    val routeName: String,
    val departureTime: String,
    val startedAtLabel: String,
    val endedAtLabel: String,
    val statusLabel: String = "COMPLETADO",
    val incidentTypeLabel: String? = null,
    val incidentDetail: String? = null,
)
