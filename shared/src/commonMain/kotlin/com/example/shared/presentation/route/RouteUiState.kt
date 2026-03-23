package com.example.shared.presentation.route

import com.example.shared.domain.entities.Route

data class RouteUiState(
    val isLoading: Boolean = false,
    val routes: List<Route> = emptyList(),
    val favoriteRouteIds: Set<String> = emptySet(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val searchResults: List<Route> = emptyList(),
    val isSearching: Boolean = false
)
