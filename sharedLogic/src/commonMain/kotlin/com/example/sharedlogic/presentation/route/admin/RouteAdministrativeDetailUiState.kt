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
package com.example.sharedlogic.presentation.route.admin

import com.example.sharedlogic.domain.entities.Route

data class RouteAdministrativeDetailUiState(
    val route: Route? = null,
    val driverNamesById: Map<String, String> = emptyMap(),
    val routeRuntimeStatus: AdministrativeRouteRuntimeStatus = AdministrativeRouteRuntimeStatus(),
    val scheduleStatusesById: Map<String, AdministrativeScheduleRuntimeStatus> = emptyMap(),
    val highlightedScheduleId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

data class AdministrativeRouteRuntimeStatus(
    val state: AdministrativeRouteStatus = AdministrativeRouteStatus.IDLE,
    val detail: String = "Esperando inicio del conductor",
)

enum class AdministrativeRouteStatus {
  IDLE,
  ACTIVE,
  DELAYED,
}

enum class AdministrativeScheduleRuntimeStatus {
  SCHEDULED,
  ACTIVE,
  DELAYED,
  COMPLETED,
}
