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
package com.example.busalotico.ui.model.mapper

import com.example.busalotico.ui.model.RouteListItemUi
import com.example.busalotico.ui.model.RouteSelectionUiState
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.presentation.route.uistate.RouteRuntimeStatus
import com.example.sharedlogic.presentation.route.uistate.RouteRuntimeSummary
import com.example.sharedlogic.presentation.route.uistate.RouteUiState

fun Route.toRouteListItemUi(
    favoriteRouteIds: Set<String>,
    runtimeByRouteId: Map<String, RouteRuntimeSummary> = emptyMap(),
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
      isLive =
          runtime?.status == RouteRuntimeStatus.ACTIVE ||
              runtime?.status == RouteRuntimeStatus.DELAYED ||
              isLive,
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
