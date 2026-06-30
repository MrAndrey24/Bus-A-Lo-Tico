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
package com.example.busalotico.ui.model

import androidx.compose.runtime.Immutable
import com.example.sharedlogic.domain.entities.geo.Canton as SharedCanton
import com.example.sharedlogic.domain.entities.geo.Province as SharedProvince

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
