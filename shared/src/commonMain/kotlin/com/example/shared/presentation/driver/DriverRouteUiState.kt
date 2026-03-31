package com.example.shared.presentation.driver

import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.TripExecution

data class DriverRouteUiState(
    val route: Route? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isStartingTrip: Boolean = false,
    val isEndingTrip: Boolean = false,
    val isReportingIncident: Boolean = false,
    val activeTripExecutionId: String? = null,
    val currentTripExecution: TripExecution? = null,
    val completedTodayScheduleIds: Set<String> = emptySet(),
    val tripHistory: List<DriverTripHistoryRecord> = emptyList(),
)
