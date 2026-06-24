/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.presentation.admin

import com.example.sharedlogic.domain.entities.Driver
import com.example.sharedlogic.domain.entities.Route

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
