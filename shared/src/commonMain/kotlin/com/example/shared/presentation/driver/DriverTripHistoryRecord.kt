/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
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
