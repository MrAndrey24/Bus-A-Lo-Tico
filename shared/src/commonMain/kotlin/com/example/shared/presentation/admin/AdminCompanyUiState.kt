/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.presentation.admin

import com.example.shared.domain.entities.Driver
import com.example.shared.domain.entities.Route

data class AdminCompanyUiState(
    val isLoading: Boolean = true,
    val routes: List<Route> = emptyList(),
    val selectedRoute: Route? = null,
    val companyNames: Map<String, String> = emptyMap(),
    val companyDrivers: List<Driver> = emptyList(),
    val recentIncidents: List<AdminRouteIncidentSummary> = emptyList(),
    val isSavingRoute: Boolean = false,
    val isSavingSchedule: Boolean = false,
    val isSavingStop: Boolean = false,
    val isSavingDriver: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
)
