package com.example.shared.presentation.route

import com.example.shared.domain.entities.Route

data class DriverRouteUiState(
    val route: Route? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
