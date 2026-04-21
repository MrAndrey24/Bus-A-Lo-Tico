/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.presentation.driver

import com.example.shared.domain.entities.Driver
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.TripExecution

data class DriverRouteUiState(
    val route: Route? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isStartingTrip: Boolean = false,
    val isEndingTrip: Boolean = false,
    val isResumingTrip: Boolean = false,
    val isTransferringTrip: Boolean = false,
    val isReportingIncident: Boolean = false,
    val activeTripExecutionId: String? = null,
    val currentTripExecution: TripExecution? = null,
    val completedTodayScheduleIds: Set<String> = emptySet(),
    val tripHistory: List<DriverTripHistoryRecord> = emptyList(),
    val transferDrivers: List<Driver> = emptyList(),
)
