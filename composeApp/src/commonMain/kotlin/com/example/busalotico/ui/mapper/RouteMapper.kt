package com.example.busalotico.ui.mapper

import com.example.busalotico.ui.model.RouteListItemUi
import com.example.busalotico.ui.model.RouteSelectionUiState
import com.example.shared.domain.entities.Route
import com.example.shared.presentation.route.RouteUiState

fun Route.toRouteListItemUi(favoriteRouteIds: Set<String>): RouteListItemUi {
    return RouteListItemUi(
        id = id,
        code = code,
        title = name.trim().uppercase(),
        companyName = operator,
        fareCrc = fareCrc,
        statusText = statusText,
        etaText = etaText,
        frequencyText = frequencyText,
        isActive = isActive,
        isLive = isLive,
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
        routes = routes.map { route -> route.toRouteListItemUi(favoriteRouteIds) },
        isLoading = isLoading,
        errorMessage = errorMessage,
    )
}
