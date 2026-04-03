package com.example.shared.presentation.route

import com.example.shared.domain.entities.RouteStop

data class RouteStopMaintenanceUiState(
    val routeId: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val stops: List<RouteStop> = emptyList(),
    val selectedStopId: String? = null,
    val name: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val order: String = "",
)
