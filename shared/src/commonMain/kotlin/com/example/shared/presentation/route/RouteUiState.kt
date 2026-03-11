package com.example.shared.presentation.route

import com.example.shared.domain.entities.Route

data class RouteUiState(
    val isLoading: Boolean = false,
    val routes: List<Route> = emptyList(),
    val errorMessage: String? = null
)
