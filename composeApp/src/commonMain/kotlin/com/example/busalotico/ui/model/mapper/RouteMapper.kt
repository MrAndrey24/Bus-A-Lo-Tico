package com.example.busalotico.ui.model.mapper

import com.example.busalotico.ui.model.RouteListItemUi
import com.example.busalotico.ui.model.RouteSelectionUiState
import com.example.shared.domain.entities.Route
import com.example.shared.presentation.route.RouteRuntimeStatus
import com.example.shared.presentation.route.RouteUiState

fun Route.toRouteListItemUi(
    favoriteRouteIds: Set<String>,
    runtimeByRouteId: Map<String, com.example.shared.presentation.route.RouteRuntimeSummary> = emptyMap(),
): RouteListItemUi {
    val runtime = runtimeByRouteId[id]
    return RouteListItemUi(
        id = id,
        code = code,
        title = name.trim().uppercase(),
        companyName = operator,
        fareCrc = fareCrc,
        statusText = runtime?.statusText ?: statusText,
        etaText = etaText,
        frequencyText = frequencyText,
        isActive = isActive,
        isLive = runtime?.status == RouteRuntimeStatus.ACTIVE || runtime?.status == RouteRuntimeStatus.DELAYED || isLive,
        isDelayed = runtime?.status == RouteRuntimeStatus.DELAYED,
        isFavorite = favoriteRouteIds.contains(id),
        province = startLocation.province,
        canton = startLocation.canton,
        district = startLocation.district,
        endProvince = endLocation.province,
        endCanton = endLocation.canton,
        endDistrict = endLocation.district,
    )
}

fun RouteUiState.toRouteSelectionUiState(): RouteSelectionUiState {
    return RouteSelectionUiState(
        routes = routes.map { route -> route.toRouteListItemUi(favoriteRouteIds, routeRuntimeById) },
        isLoading = isLoading,
        errorMessage = errorMessage,
    )
}
