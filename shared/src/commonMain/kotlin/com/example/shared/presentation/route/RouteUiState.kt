package com.example.shared.presentation.route

import com.example.shared.domain.entities.Route

data class RouteUiState(
    val isLoading: Boolean = false,
    val routes: List<Route> = emptyList(),
    val favoriteRouteIds: Set<String> = emptySet(),
    val routeRuntimeById: Map<String, RouteRuntimeSummary> = emptyMap(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val searchResults: List<Route> = emptyList(),
    val isSearching: Boolean = false
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
