package com.example.shared.presentation.route

import com.example.shared.domain.entities.Route

data class RouteAdministrativeDetailUiState(
    val route: Route? = null,
    val driverNamesById: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
