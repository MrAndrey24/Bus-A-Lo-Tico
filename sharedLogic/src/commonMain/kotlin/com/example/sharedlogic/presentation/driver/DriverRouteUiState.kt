/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.presentation.driver

import com.example.sharedlogic.domain.entities.Driver
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.TripExecution

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
