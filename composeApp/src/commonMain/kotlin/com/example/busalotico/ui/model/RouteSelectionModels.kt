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
package com.example.busalotico.ui.model

import androidx.compose.runtime.Immutable
import com.example.shared.domain.entities.geo.Canton as SharedCanton
import com.example.shared.domain.entities.geo.Province as SharedProvince

typealias Province = SharedProvince

typealias Canton = SharedCanton

@Immutable
data class RouteListItemUi(
    val id: String,
    val code: String,
    val title: String,
    val companyName: String? = null,
    val fareCrc: Int? = null,
    val statusText: String,
    val etaText: String,
    val frequencyText: String,
    val isActive: Boolean = true,
    val isLive: Boolean = true,
    val isDelayed: Boolean = false,
    val isFavorite: Boolean = false,
    val isNearMe: Boolean = true,
    val province: String? = null,
    val canton: String? = null,
    val district: String? = null,
    val endProvince: String? = null,
    val endCanton: String? = null,
    val endDistrict: String? = null,
)

data class RouteSelectionUiState(
    val routes: List<RouteListItemUi>,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

data class RouteFiltersState(
    val query: String = "",
    val nearMeOnly: Boolean = false,
    val favoritesOnly: Boolean = false,
    val selectedProvince: Province? = null,
    val selectedCanton: Canton? = null,
    val selectedDistrict: String? = null,
)
