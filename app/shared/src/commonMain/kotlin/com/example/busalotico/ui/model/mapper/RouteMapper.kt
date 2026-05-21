/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.busalotico.ui.model.mapper

import com.example.busalotico.ui.model.RouteListItemUi
import com.example.busalotico.ui.model.RouteSelectionUiState
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.presentation.route.RouteRuntimeStatus
import com.example.sharedlogic.presentation.route.RouteUiState

fun Route.toRouteListItemUi(
    favoriteRouteIds: Set<String>,
    runtimeByRouteId: Map<String, com.example.sharedlogic.presentation.route.RouteRuntimeSummary> =
        emptyMap(),
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
