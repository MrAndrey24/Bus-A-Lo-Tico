package com.example.busalotico.ui.model

import androidx.compose.runtime.Immutable

@Immutable data class Province(val name: String, val cantons: List<Canton>)

@Immutable data class Canton(val name: String, val districts: List<String>)

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
    val isFavorite: Boolean = false,
    val isNearMe: Boolean = true,
    val province: String? = null,
    val canton: String? = null,
    val district: String? = null,
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
