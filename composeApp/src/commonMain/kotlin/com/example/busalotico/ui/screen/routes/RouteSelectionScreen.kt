/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busalotico.ui.components.button.PillActionButton
import com.example.busalotico.ui.components.chip.SelectableFilterChip
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.busalotico.ui.components.input.DropdownSelectField
import com.example.busalotico.ui.components.route.RouteSelectionCard
import com.example.busalotico.ui.components.searchBar.SearchBar
import com.example.busalotico.ui.model.Canton
import com.example.busalotico.ui.model.Province
import com.example.busalotico.ui.model.RouteFiltersState
import com.example.busalotico.ui.model.RouteListItemUi
import com.example.busalotico.ui.model.RouteSelectionUiState
import com.example.shared.domain.entities.geo.costaRicaGeoData

@Composable
fun RouteSelectionScreen(
    uiState: RouteSelectionUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onRouteClick: (RouteListItemUi) -> Unit = {},
    onAdministrativeDetailClick: (RouteListItemUi) -> Unit = {},
    onToggleFavorite: (routeId: String, isFavorite: Boolean) -> Unit = { _, _ -> },
) {
  val provinces = remember {
    costaRicaGeoData()
        .sortedBy { it.name.lowercase() }
        .map { province ->
          province.copy(
              cantons =
                  province.cantons
                      .sortedBy { it.name.lowercase() }
                      .map { canton ->
                        canton.copy(districts = canton.districts.sortedBy { it.lowercase() })
                      },
          )
        }
  }
  val filtersSaver =
      remember(provinces) {
        listSaver<RouteFiltersState, String?>(
            save = { state ->
              listOf(
                  state.query,
                  state.favoritesOnly.toString(),
                  state.selectedProvince?.name,
                  state.selectedCanton?.name,
                  state.selectedDistrict,
              )
            },
            restore = { saved ->
              val province = provinces.firstOrNull { it.name == saved.getOrNull(2) }
              val canton = province?.cantons?.firstOrNull { it.name == saved.getOrNull(3) }
              RouteFiltersState(
                  query = saved.getOrNull(0).orEmpty(),
                  favoritesOnly = saved.getOrNull(1).toBoolean(),
                  selectedProvince = province,
                  selectedCanton = canton,
                  selectedDistrict = saved.getOrNull(4),
              )
            },
        )
      }
  var showLocationPanel by rememberSaveable { mutableStateOf(false) }

  var filters by rememberSaveable(stateSaver = filtersSaver) { mutableStateOf(RouteFiltersState()) }
  var favoriteRouteIds by
      remember(uiState.routes) {
        mutableStateOf(uiState.routes.filter { it.isFavorite }.map { it.id }.toSet())
      }

  val availableCantons = filters.selectedProvince?.cantons ?: emptyList()
  val availableDistricts = filters.selectedCanton?.districts ?: emptyList()

  val routesWithFavorites by
      remember(uiState.routes, favoriteRouteIds) {
        derivedStateOf {
          uiState.routes.map { route ->
            route.copy(isFavorite = route.isFavorite || favoriteRouteIds.contains(route.id))
          }
        }
      }

  val filteredRoutes by
      remember(routesWithFavorites, filters) {
        derivedStateOf {
          routesWithFavorites.filter { route ->
            if (!route.isActive) return@filter false

            val queryMatches =
                if (filters.query.isBlank()) {
                  true
                } else {
                  buildString {
                        append(route.title)
                        append(" ")
                        append(route.code)
                        append(" ")
                        append(route.province.orEmpty())
                        append(" ")
                        append(route.canton.orEmpty())
                        append(" ")
                        append(route.district.orEmpty())
                        append(" ")
                        append(route.endProvince.orEmpty())
                        append(" ")
                        append(route.endCanton.orEmpty())
                        append(" ")
                        append(route.endDistrict.orEmpty())
                      }
                      .contains(filters.query, ignoreCase = true)
                }

            val favoritesMatch = !filters.favoritesOnly || route.isFavorite
            val provinceMatch =
                filters.selectedProvince == null ||
                    route.province.equals(filters.selectedProvince?.name, ignoreCase = true) ||
                    route.endProvince.equals(filters.selectedProvince?.name, ignoreCase = true)
            val cantonMatch =
                filters.selectedCanton == null ||
                    route.canton.equals(filters.selectedCanton?.name, ignoreCase = true) ||
                    route.endCanton.equals(filters.selectedCanton?.name, ignoreCase = true)
            val districtMatch =
                filters.selectedDistrict == null ||
                    route.district.equals(filters.selectedDistrict, ignoreCase = true) ||
                    route.endDistrict.equals(filters.selectedDistrict, ignoreCase = true)

            queryMatches && favoritesMatch && provinceMatch && cantonMatch && districtMatch
          }
        }
      }

  Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Column(modifier = Modifier.fillMaxSize()) {
      RouteSelectionTopBar(onBackClick = onBackClick)

      Column(modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        SearchField(
            value = filters.query,
            onValueChange = { filters = filters.copy(query = it) },
            placeholder = "Buscar ruta, parada o lugar...",
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterChipsRow(
            locationLabel =
                buildLocationLabel(
                    filters.selectedProvince,
                    filters.selectedCanton,
                    filters.selectedDistrict,
                ),
            locationSelected = showLocationPanel,
            onLocationClick = { showLocationPanel = !showLocationPanel },
            favoriteCount = routesWithFavorites.count { it.isFavorite },
            favoritesOnly = filters.favoritesOnly,
            onFavoritesToggle = { filters = filters.copy(favoritesOnly = !filters.favoritesOnly) },
        )

        AnimatedVisibility(visible = showLocationPanel) {
          LocationPanel(
              provinces = provinces,
              selectedProvince = filters.selectedProvince,
              onProvinceSelected = { province ->
                filters =
                    filters.copy(
                        selectedProvince = province,
                        selectedCanton = null,
                        selectedDistrict = null,
                    )
              },
              cantons = availableCantons,
              selectedCanton = filters.selectedCanton,
              onCantonSelected = { canton ->
                filters = filters.copy(selectedCanton = canton, selectedDistrict = null)
              },
              districts = availableDistricts,
              selectedDistrict = filters.selectedDistrict,
              onDistrictSelected = { filters = filters.copy(selectedDistrict = it) },
              onClear = {
                filters =
                    filters.copy(
                        selectedProvince = null,
                        selectedCanton = null,
                        selectedDistrict = null,
                    )
              },
              onApply = { showLocationPanel = false },
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              text = "RUTAS DISPONIBLES",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary,
              letterSpacing = 1.2.sp,
          )

          ActiveCountPill(count = filteredRoutes.count { it.isActive })
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.isLoading) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            androidx.compose.material3.CircularProgressIndicator()
          }
        } else {
          uiState.errorMessage?.let { message ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
            ) {
              Text(
                  text = message,
                  modifier = Modifier.padding(12.dp),
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onErrorContainer,
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
          }

          if (filteredRoutes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Text(
                  text = "No hay rutas para los filtros seleccionados.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
              items(filteredRoutes, key = { it.id }) { route ->
                RouteSelectionCard(
                    route = route,
                    onClick = { onRouteClick(route) },
                    onToggleFavorite = {
                      val nextFavoriteState = !favoriteRouteIds.contains(route.id)
                      favoriteRouteIds =
                          if (favoriteRouteIds.contains(route.id)) favoriteRouteIds - route.id
                          else favoriteRouteIds + route.id
                      onToggleFavorite(route.id, nextFavoriteState)
                    },
                    onShowAdministrativeDetail = { onAdministrativeDetailClick(route) },
                )
              }
              item { Spacer(modifier = Modifier.height(24.dp)) }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun RouteSelectionTopBar(onBackClick: () -> Unit) {
  ScreenHeader(
      title = "Rutas",
      subtitle = "Selecciona tu destino",
      leadingContent = {
        IconButton(onClick = onBackClick) {
          Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface,
          )
        }
      },
  )
}

@Composable
private fun SearchField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
  SearchBar(
      query = value,
      onQueryChange = onValueChange,
      isSearching = false,
      searchResults = emptyList(),
      onRouteSelected = {},
      placeholder = placeholder,
      modifier = Modifier.fillMaxWidth(),
  )
}

@Composable
private fun FilterChipsRow(
    locationLabel: String,
    locationSelected: Boolean,
    onLocationClick: () -> Unit,
    favoriteCount: Int,
    favoritesOnly: Boolean,
    onFavoritesToggle: () -> Unit,
) {
  Row(
      modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically,
  ) {
    SelectableFilterChip(
        text = locationLabel,
        selected = locationSelected,
        onClick = onLocationClick,
    )

    SelectableFilterChip(
        text = "Favoritas ($favoriteCount)",
        selected = favoritesOnly,
        onClick = onFavoritesToggle,
    )
  }
}

@Composable
private fun LocationPanel(
    provinces: List<Province>,
    selectedProvince: Province?,
    onProvinceSelected: (Province) -> Unit,
    cantons: List<Canton>,
    selectedCanton: Canton?,
    onCantonSelected: (Canton) -> Unit,
    districts: List<String>,
    selectedDistrict: String?,
    onDistrictSelected: (String) -> Unit,
    onClear: () -> Unit,
    onApply: () -> Unit,
) {
  Surface(
      modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
      shape = RoundedCornerShape(20.dp),
      tonalElevation = 0.dp,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
            text = "Filtrar por Ubicación",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )

        TextButton(onClick = onClear) { Text("Limpiar", color = MaterialTheme.colorScheme.error) }
      }

      DropdownSelectField(
          value = selectedProvince?.name ?: "",
          placeholder = "Provincia",
          enabled = true,
          options = provinces.map { it.name },
          onSelect = { name -> provinces.firstOrNull { it.name == name }?.let(onProvinceSelected) },
      )

      DropdownSelectField(
          value = selectedCanton?.name ?: "",
          placeholder = "Cantón",
          enabled = selectedProvince != null,
          options = cantons.map { it.name },
          onSelect = { name -> cantons.firstOrNull { it.name == name }?.let(onCantonSelected) },
      )

      DropdownSelectField(
          value = selectedDistrict ?: "",
          placeholder = "Distrito",
          enabled = selectedCanton != null,
          options = districts,
          onSelect = onDistrictSelected,
      )

      PillActionButton(
          text = "Aplicar Filtros",
          onClick = onApply,
          modifier = Modifier.fillMaxWidth(),
      )
    }
  }
}

@Composable
private fun ActiveCountPill(count: Int) {
  Surface(
      shape = RoundedCornerShape(12.dp),
      color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
  ) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
          modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.tertiary, CircleShape)
      )
      Spacer(modifier = Modifier.size(8.dp))
      Text(
          text = "$count activas",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onTertiaryContainer,
      )
    }
  }
}

@Composable
private fun RouteLocationLine(label: String, value: String) {
  Row(
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    Icon(
        imageVector = Icons.Filled.LocationOn,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(14.dp),
    )
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun RouteCodeTile(code: String, isActive: Boolean, modifier: Modifier = Modifier) {
  val bg =
      if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
  val fg =
      if (isActive) MaterialTheme.colorScheme.onPrimary
      else MaterialTheme.colorScheme.onSurfaceVariant

  Surface(modifier = modifier.size(56.dp), shape = RoundedCornerShape(12.dp), color = bg) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
      Text(
          "RUTA",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
          color = fg.copy(alpha = 0.9f),
      )
      Text(
          code,
          style = MaterialTheme.typography.titleSmall,
          color = fg,
          fontWeight = FontWeight.SemiBold,
      )
    }
  }
}

private fun buildLocationLabel(province: Province?, canton: Canton?, district: String?): String {
  return when {
    province == null -> "Ubicación"
    canton == null -> province.name
    district == null -> "${province.name} · ${canton.name}"
    else -> "${province.name} · ${canton.name} · $district"
  }
}
