package com.example.shared.presentation.route

import com.example.shared.domain.entities.Route

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
