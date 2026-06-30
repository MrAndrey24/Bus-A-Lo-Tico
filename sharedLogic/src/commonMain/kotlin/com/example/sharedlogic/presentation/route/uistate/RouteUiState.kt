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
package com.example.sharedlogic.presentation.route.uistate

import com.example.sharedlogic.domain.entities.Route

data class RouteUiState(
    val isLoading: Boolean = false,
    val routes: List<Route> = emptyList(),
    val favoriteRouteIds: Set<String> = emptySet(),
    val routeRuntimeById: Map<String, RouteRuntimeSummary> = emptyMap(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val searchResults: List<Route> = emptyList(),
    val isSearching: Boolean = false,
)

data class RouteRuntimeSummary(
    val status: RouteRuntimeStatus = RouteRuntimeStatus.IDLE,
    val statusText: String = "EN ESPERA",
)

enum class RouteRuntimeStatus {
  IDLE,
  ACTIVE,
  DELAYED,
}
