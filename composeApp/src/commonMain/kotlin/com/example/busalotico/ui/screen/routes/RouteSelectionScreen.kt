package com.example.busalotico.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.busalotico.ui.components.button.PillActionButton
import com.example.busalotico.ui.components.chip.SelectableFilterChip
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.busalotico.ui.components.input.DropdownSelectField
import com.example.busalotico.ui.components.searchBar.SearchBar
import com.example.busalotico.ui.model.Canton
import com.example.busalotico.ui.model.Province
import com.example.busalotico.ui.model.RouteFiltersState
import com.example.busalotico.ui.model.RouteListItemUi
import com.example.busalotico.ui.model.RouteSelectionUiState

@Composable
fun RouteSelectionScreen(
    uiState: RouteSelectionUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onRouteClick: (RouteListItemUi) -> Unit = {},
    onAdministrativeDetailClick: (RouteListItemUi) -> Unit = {},
    onToggleFavorite: (routeId: String, isFavorite: Boolean) -> Unit = { _, _ -> },
) {
  val provinces = remember { demoCRGeoData() }
  var showLocationPanel by remember { mutableStateOf(false) }

  var filters by remember { mutableStateOf(RouteFiltersState()) }
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
                  (route.title + " " + route.code).contains(filters.query, ignoreCase = true)
                }

            val nearMeMatches = !filters.nearMeOnly || route.isNearMe
            val favoritesMatch = !filters.favoritesOnly || route.isFavorite
            val provinceMatch =
                filters.selectedProvince == null ||
                    route.province.equals(filters.selectedProvince?.name, ignoreCase = true)
            val cantonMatch =
                filters.selectedCanton == null ||
                    route.canton.equals(filters.selectedCanton?.name, ignoreCase = true)
            val districtMatch =
                filters.selectedDistrict == null ||
                    route.district.equals(filters.selectedDistrict, ignoreCase = true)

            queryMatches &&
                nearMeMatches &&
                favoritesMatch &&
                provinceMatch &&
                cantonMatch &&
                districtMatch
          }
        }
      }

  Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Column(modifier = Modifier.fillMaxSize()) {
      RouteSelectionTopBar(onBackClick = onBackClick)

      Column(modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(14.dp))

        SearchField(
            value = filters.query,
            onValueChange = { filters = filters.copy(query = it) },
            placeholder = "Buscar ruta, parada o lugar...",
        )

        Spacer(modifier = Modifier.height(10.dp))

        FilterChipsRow(
            nearMe = filters.nearMeOnly,
            onNearMeToggle = { filters = filters.copy(nearMeOnly = !filters.nearMeOnly) },
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

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              text = "RUTAS DISPONIBLES",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          ActiveCountPill(count = filteredRoutes.count { it.isActive })
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.isLoading) {
          Text(
              text = "Cargando rutas...",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        } else {
          uiState.errorMessage?.let { message ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
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
            Text(
                text = "No hay rutas para los filtros seleccionados.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
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
              item { Spacer(modifier = Modifier.height(18.dp)) }
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
      subtitle = "Seleccion de rutas",
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
    nearMe: Boolean,
    onNearMeToggle: () -> Unit,
    locationLabel: String,
    locationSelected: Boolean,
    onLocationClick: () -> Unit,
    favoriteCount: Int,
    favoritesOnly: Boolean,
    onFavoritesToggle: () -> Unit,
) {
  Row(
      modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically,
  ) {
    SelectableFilterChip(
        text = if (nearMe) "Cerca de mí ✓" else "Cerca de mí",
        selected = nearMe,
        onClick = onNearMeToggle,
    )

    SelectableFilterChip(
        text = locationLabel,
        selected = locationSelected,
        onClick = onLocationClick,
    )

    SelectableFilterChip(
        text = if (favoritesOnly) "Favoritas ($favoriteCount) ✓" else "Favoritas ($favoriteCount)",
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
      shape = RoundedCornerShape(16.dp),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      color = MaterialTheme.colorScheme.surface,
  ) {
    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Text(
          text = "Ubicación",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.onSurface,
      )

      DropdownSelectField(
          value = selectedProvince?.name ?: "",
          placeholder = "Selecciona una provincia",
          enabled = true,
          options = provinces.map { it.name },
          onSelect = { name -> provinces.firstOrNull { it.name == name }?.let(onProvinceSelected) },
      )

      DropdownSelectField(
          value = selectedCanton?.name ?: "",
          placeholder =
              if (selectedProvince == null) "Primero elige provincia" else "Selecciona un cantón",
          enabled = selectedProvince != null,
          options = cantons.map { it.name },
          onSelect = { name -> cantons.firstOrNull { it.name == name }?.let(onCantonSelected) },
      )

      DropdownSelectField(
          value = selectedDistrict ?: "",
          placeholder =
              if (selectedCanton == null) "Primero elige cantón" else "Selecciona un distrito",
          enabled = selectedCanton != null,
          options = districts,
          onSelect = onDistrictSelected,
      )

      Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        PillActionButton(
            text = "Aplicar",
            onClick = onApply,
            modifier = Modifier.weight(1f),
        )

        PillActionButton(
            text = "Limpiar",
            onClick = onClear,
            modifier = Modifier.weight(1f),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
      }
    }
  }
}

@Composable
private fun ActiveCountPill(count: Int) {
  Surface(
      shape = RoundedCornerShape(999.dp),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      color = MaterialTheme.colorScheme.surfaceVariant,
  ) {
    Row(
        modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.tertiary, CircleShape))
      Spacer(modifier = Modifier.size(7.dp))
      Text(
          text = "$count activas",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
private fun RouteSelectionCard(
    route: RouteListItemUi,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShowAdministrativeDetail: () -> Unit,
) {
  Card(
      modifier = Modifier.fillMaxWidth().clickable(enabled = route.isActive, onClick = onClick),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      RouteCodeTile(
          code = route.code,
          isActive = route.isActive,
          modifier = Modifier.align(Alignment.CenterVertically),
      )

      Spacer(modifier = Modifier.size(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
          Text(
              text = route.title,
              modifier = Modifier.weight(1f),
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onSurface,
              fontWeight = FontWeight.SemiBold,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis,
          )
        }

        val locationText =
            listOfNotNull(
                    route.province?.takeIf { it.isNotBlank() }?.toDisplayLocationName(),
                    route.canton?.takeIf { it.isNotBlank() }?.toDisplayLocationName(),
                    route.district?.takeIf { it.isNotBlank() }?.toDisplayLocationName(),
                )
                .joinToString(", ")

        if (locationText.isNotBlank()) {
          Spacer(modifier = Modifier.height(4.dp))

          Surface(shape = RoundedCornerShape(999.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              Icon(
                  imageVector = Icons.Filled.LocationOn,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(14.dp),
              )
              Text(
                  text = locationText,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (route.isLive) {
            Text(
                text = route.statusText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.size(8.dp))
          }

          Text(
              text = "⏱ ${route.etaText}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          route.fareCrc?.let { fareCrc ->
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "•",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              Text(
                  text = "TARIFA",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontWeight = FontWeight.Medium,
              )
              Text(
                  text = "₡$fareCrc",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold,
              )
            }
          }
        }

        route.companyName?.takeIf { it.isNotBlank() }?.let { companyName ->
          Spacer(modifier = Modifier.height(8.dp))

          Text(
              text = companyName,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
          )
        }
      }

      Column(
          modifier = Modifier.padding(top = 2.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween,
      ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "Ver ficha administrativa",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp).clickable { onShowAdministrativeDetail() },
        )
        Spacer(modifier = Modifier.height(12.dp))
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "Toggle favorite",
            tint = if (route.isFavorite) Color(0xFFF4C430) else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(18.dp).clickable { onToggleFavorite() },
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "›", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.outline)
      }
    }
  }
}


@Composable
private fun RouteCodeTile(code: String, isActive: Boolean, modifier: Modifier = Modifier) {
  val bg = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
  val fg = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

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

private fun String.toDisplayLocationName(): String {
  return trim().lowercase().split(Regex("\\s+")).joinToString(" ") { word ->
    word.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase() else char.toString() }
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

private fun demoCRGeoData(): List<Province> =
    listOf(
        Province(
            name = "San José",
            cantons =
                listOf(
                    Canton(
                        "San José",
                        listOf(
                            "Carmen",
                            "Merced",
                            "Hospital",
                            "Catedral",
                            "Zapote",
                            "San Francisco de Dos Ríos",
                            "Uruca",
                            "Mata Redonda",
                            "Pavas",
                            "Hatillo",
                            "San Sebastián",
                        ),
                    ),
                    Canton("Escazú", listOf("Escazú", "San Antonio", "San Rafael")),
                    Canton(
                        "Desamparados",
                        listOf(
                            "Desamparados",
                            "San Miguel",
                            "San Juan de Dios",
                            "San Rafael Arriba",
                            "San Antonio",
                            "Frailes",
                            "Patarrá",
                            "San Cristobal",
                            "Rosario",
                            "Damas",
                            "San Rafael Abajo",
                            "Gravilias",
                            "Los Guido",
                        ),
                    ),
                    Canton(
                        "Puriscal",
                        listOf(
                            "Santiago",
                            "Mercedes Sur",
                            "Barbacoas",
                            "Grifo Alto",
                            "San Rafael",
                            "Candelarita",
                            "Desamparaditos",
                            "San Antonio",
                            "Chires",
                        ),
                    ),
                    Canton("Tarrazú", listOf("San Marcos", "San Lorenzo", "San Carlos")),
                    Canton(
                        "Aserrí",
                        listOf(
                            "Aserrí",
                            "Tarbaca",
                            "Vuelta de Jorco",
                            "San Gabriel",
                            "Legua",
                            "Monterrey",
                            "Salitrillos",
                        ),
                    ),
                    Canton(
                        "Mora",
                        listOf(
                            "Colón",
                            "Guayabo",
                            "Tabarcia",
                            "Piedras Negras",
                            "Picagres",
                            "Jaris",
                            "Quitirrisí",
                        ),
                    ),
                    Canton(
                        "Goicoechea",
                        listOf(
                            "Guadalupe",
                            "San Francisco",
                            "Calle Blancos",
                            "Mata de Plátano",
                            "Ipís",
                            "Rancho Redondo",
                            "Purral",
                        ),
                    ),
                    Canton(
                        "Santa Ana",
                        listOf("Santa Ana", "Salitral", "Pozos", "Uruca", "Piedades", "Brasil"),
                    ),
                    Canton(
                        "Alajuelita",
                        listOf(
                            "Alajuelita",
                            "San Josecito",
                            "San Antonio",
                            "Concepción",
                            "San Felipe",
                        ),
                    ),
                    Canton(
                        "Vázquez de Coronado",
                        listOf(
                            "San Isidro",
                            "San Rafael",
                            "Dulce Nombre de Jesús",
                            "Patalillo",
                            "Cascajal",
                        ),
                    ),
                    Canton(
                        "Acosta",
                        listOf("San Ignacio", "Guaitil", "Palmichal", "Cangrejal", "Sabanillas"),
                    ),
                    Canton(
                        "Tibás",
                        listOf(
                            "San Juan",
                            "Cinco Esquinas",
                            "Anselmo Llorente",
                            "León XIII",
                            "Colima",
                        ),
                    ),
                    Canton("Moravia", listOf("San Vicente", "San Jerónimo", "La Trinidad")),
                    Canton(
                        "Montes de Oca",
                        listOf("San Pedro", "Sabanilla", "Mercedes", "San Rafael"),
                    ),
                    Canton(
                        "Turrubares",
                        listOf("San Pablo", "San Pedro", "San Juan de Mata", "San Juan", "Carara"),
                    ),
                    Canton("Dota", listOf("Santa María", "Jardín", "Copey")),
                    Canton("Curridabat", listOf("Curridabat", "Granadilla", "Sánchez", "Tirrases")),
                    Canton(
                        "Pérez Zeledón",
                        listOf(
                            "San Isidro de El General",
                            "El General",
                            "Daniel Flores",
                            "Rivas",
                            "San Pedro",
                            "Platanares",
                            "Pejibaye",
                            "Cajón",
                            "Barú",
                            "Río Nuevo",
                            "Páramo",
                            "La Amistad",
                        ),
                    ),
                    Canton(
                        "León Cortés Castro",
                        listOf(
                            "San Pablo",
                            "San Andrés",
                            "Llano Bonito",
                            "San Isidro",
                            "Santa Cruz",
                            "San Antonio",
                        ),
                    ),
                ),
        ),
        Province(
            name = "Alajuela",
            cantons =
                listOf(
                    Canton(
                        "Alajuela",
                        listOf(
                            "Alajuela",
                            "San José",
                            "Carrizal",
                            "San Antonio",
                            "Guácima",
                            "San Isidro",
                            "Sabanilla",
                            "San Rafael",
                            "Río Segundo",
                            "Desamparados",
                            "Turrúcares",
                            "Tambor",
                            "Garita",
                            "Sarapiquí",
                        ),
                    ),
                    Canton(
                        "San Ramón",
                        listOf(
                            "San Ramón",
                            "Santiago",
                            "San Juan",
                            "Piedades Norte",
                            "Piedades Sur",
                            "San Rafael",
                            "San Isidro",
                            "Ángeles",
                            "Alfaro",
                            "Volio",
                            "Concepción",
                            "Zapotal",
                            "Peñas Blancas",
                            "San Lorenzo",
                        ),
                    ),
                    Canton(
                        "Grecia",
                        listOf(
                            "Grecia",
                            "San Isidro",
                            "San José",
                            "San Roque",
                            "Tacares",
                            "Puente de Piedra",
                            "Bolivar",
                        ),
                    ),
                    Canton("San Mateo", listOf("San Mateo", "Desmonte", "Jesús María", "Labrador")),
                    Canton(
                        "Atenas",
                        listOf(
                            "Atenas",
                            "Jesús",
                            "Mercedes",
                            "San Isidro",
                            "Concepción",
                            "San José",
                            "Santa Eulalia",
                            "Escobal",
                        ),
                    ),
                    Canton(
                        "Naranjo",
                        listOf(
                            "Naranjo",
                            "San Miguel",
                            "San José",
                            "Cirrí Sur",
                            "San Jerónimo",
                            "San Juan",
                            "El Rosario",
                            "Palmitos",
                        ),
                    ),
                    Canton(
                        "Palmares",
                        listOf(
                            "Palmares",
                            "Zaragoza",
                            "Buenos Aires",
                            "Santiago",
                            "Candelaria",
                            "Esquipulas",
                            "La Granja",
                        ),
                    ),
                    Canton(
                        "Poás",
                        listOf(
                            "San Pedro",
                            "San Juan",
                            "San Rafael",
                            "Carrillos",
                            "Sabana Redonda",
                        ),
                    ),
                    Canton(
                        "Orotina",
                        listOf("Orotina", "El Mastate", "Hacienda Vieja", "Coyolar", "La Ceiba"),
                    ),
                    Canton(
                        "San Carlos",
                        listOf(
                            "Quesada",
                            "Florencia",
                            "Buenavista",
                            "Aguas Zarcas",
                            "Venecia",
                            "Pital",
                            "La Fortuna",
                            "La Tigra",
                            "La Palmera",
                            "Venado",
                            "Cutris",
                            "Monterrey",
                            "Pocosol",
                        ),
                    ),
                    Canton(
                        "Zarcero",
                        listOf(
                            "Zarcero",
                            "Laguna",
                            "Tapesco",
                            "Guadalupe",
                            "Palmira",
                            "Zapote",
                            "Brisas",
                        ),
                    ),
                    Canton(
                        "Sarchí",
                        listOf(
                            "Sarchí Norte",
                            "Sarchí Sur",
                            "Toro Amarillo",
                            "San Pedro",
                            "Rodríguez",
                        ),
                    ),
                    Canton(
                        "Upala",
                        listOf(
                            "Upala",
                            "Aguas Claras",
                            "San José O Pizote",
                            "Bijagua",
                            "Delicias",
                            "Dos Ríos",
                            "Yolillal",
                            "Canalete",
                        ),
                    ),
                    Canton(
                        "Los Chiles",
                        listOf("Los Chiles", "Caño Negro", "El Amparo", "San Jorge"),
                    ),
                    Canton("Guatuso", listOf("San Rafael", "Buenavista", "Cote", "Katira")),
                    Canton("Río Cuarto", listOf("Río Cuarto", "Santa Rita", "Santa Isabel")),
                ),
        ),
        Province(
            name = "Cartago",
            cantons =
                listOf(
                    Canton(
                        "Cartago",
                        listOf(
                            "Oriental",
                            "Occidental",
                            "Carmen",
                            "San Nicolás",
                            "Aguacaliente o San Francisco",
                            "Guadalupe o Arenilla",
                            "Corralillo",
                            "Tierra Blanca",
                            "Dulce Nombre",
                            "Llano Grande",
                            "Quebradilla",
                        ),
                    ),
                    Canton(
                        "Paraíso",
                        listOf(
                            "Paraíso",
                            "Santiago",
                            "Orosi",
                            "Cachí",
                            "Llanos de Santa Lucía",
                            "Birrisito",
                        ),
                    ),
                    Canton(
                        "La Unión",
                        listOf(
                            "Tres Ríos",
                            "San Diego",
                            "San Juan",
                            "San Rafael",
                            "Concepción",
                            "Dulce Nombre",
                            "San Ramón",
                            "Río Azul",
                        ),
                    ),
                    Canton(
                        "Jiménez",
                        listOf("Juan Viñas", "Tucurrique", "Pejibaye", "La Victoria"),
                    ),
                    Canton(
                        "Turrialba",
                        listOf(
                            "Turrialba",
                            "La Suiza",
                            "Peralta",
                            "Santa Cruz",
                            "Santa Teresita",
                            "Pavones",
                            "Tuis",
                            "Tayutic",
                            "Santa Rosa",
                            "Tres Equis",
                            "La Isabel",
                            "Chirripó",
                        ),
                    ),
                    Canton("Alvarado", listOf("Pacayas", "Cervantes", "Capellades")),
                    Canton(
                        "Oreamuno",
                        listOf("San Rafael", "Cot", "Potrero Cerrado", "Cipreses", "Santa Rosa"),
                    ),
                    Canton(
                        "El Guarco",
                        listOf("El Tejar", "San Isidro", "Tobosi", "Patio de Agua"),
                    ),
                ),
        ),
        Province(
            name = "Heredia",
            cantons =
                listOf(
                    Canton(
                        "Heredia",
                        listOf("Heredia", "Mercedes", "San Francisco", "Ulloa", "Varablanca"),
                    ),
                    Canton(
                        "Barva",
                        listOf(
                            "Barva",
                            "San Pedro",
                            "San Pablo",
                            "San Roque",
                            "Santa Lucía",
                            "San José de la Montaña",
                            "Puente Salas",
                        ),
                    ),
                    Canton(
                        "Santo Domingo",
                        listOf(
                            "Santo Domingo",
                            "San Vicente",
                            "San Miguel",
                            "Paracito",
                            "Santo Tomás",
                            "Santa Rosa",
                            "Tures",
                            "Pará",
                        ),
                    ),
                    Canton(
                        "Santa Bárbara",
                        listOf(
                            "Santa Bárbara",
                            "San Pedro",
                            "San Juan",
                            "Jesús",
                            "Santo Domingo",
                            "Purabá",
                        ),
                    ),
                    Canton(
                        "San Rafael",
                        listOf("San Rafael", "San Josecito", "Santiago", "Ángeles", "Concepción"),
                    ),
                    Canton(
                        "San Isidro",
                        listOf("San Isidro", "San José", "Concepción", "San Francisco"),
                    ),
                    Canton("Belén", listOf("San Antonio", "La Ribera", "La Asunción")),
                    Canton("Flores", listOf("San Joaquín", "Barrantes", "Llorente")),
                    Canton("San Pablo", listOf("San Pablo", "Rincón de Sabanilla")),
                    Canton(
                        "Sarapiquí",
                        listOf(
                            "Puerto Viejo",
                            "La Virgen",
                            "Las Horquetas",
                            "Llanuras del Gaspar",
                            "Cureña",
                        ),
                    ),
                ),
        ),
        Province(
            name = "Guanacaste",
            cantons =
                listOf(
                    Canton(
                        "Liberia",
                        listOf("Liberia", "Cañas Dulces", "Mayorga", "Nacascolo", "Curubandé"),
                    ),
                    Canton(
                        "Nicoya",
                        listOf(
                            "Nicoya",
                            "Mansión",
                            "San Antonio",
                            "Quebrada Honda",
                            "Sámara",
                            "Nosara",
                            "Belén de Nosarita",
                        ),
                    ),
                    Canton(
                        "Santa Cruz",
                        listOf(
                            "Santa Cruz",
                            "Bolsón",
                            "Veintisiete de Abril",
                            "Tempate",
                            "Cartagena",
                            "Cuajiniquil",
                            "Diriá",
                            "Cabo Velas",
                            "Tamarindo",
                        ),
                    ),
                    Canton("Bagaces", listOf("Bagaces", "La Fortuna", "Mogote", "Río Naranjo")),
                    Canton("Carrillo", listOf("Filadelfia", "Palmira", "Sardinal", "Belén")),
                    Canton(
                        "Cañas",
                        listOf("Cañas", "Palmira", "San Miguel", "Bebedero", "Porozal"),
                    ),
                    Canton("Abangares", listOf("Las Juntas", "Sierra", "San Juan", "Colorado")),
                    Canton(
                        "Tilarán",
                        listOf(
                            "Tilarán",
                            "Quebrada Grande",
                            "Tronadora",
                            "Santa Rosa",
                            "Líbano",
                            "Tierras Morenas",
                            "Arenal",
                            "Cabeceras",
                        ),
                    ),
                    Canton(
                        "Nandayure",
                        listOf(
                            "Carmona",
                            "Santa Rita",
                            "Zapotal",
                            "San Pablo",
                            "Porvenir",
                            "Bejuco",
                        ),
                    ),
                    Canton(
                        "La Cruz",
                        listOf("La Cruz", "Santa Cecilia", "La Garita", "Santa Elena"),
                    ),
                    Canton(
                        "Hojancha",
                        listOf("Hojancha", "Monte Romo", "Puerto Carrillo", "Huacas", "Matambú"),
                    ),
                ),
        ),
        Province(
            name = "Puntarenas",
            cantons =
                listOf(
                    Canton(
                        "Puntarenas",
                        listOf(
                            "Puntarenas",
                            "Pitahaya",
                            "Chomes",
                            "Lepanto",
                            "Paquera",
                            "Manzanillo",
                            "Guacimal",
                            "Barranca",
                            "Isla del Coco",
                            "Cóbano",
                            "Chacarita",
                            "Chira",
                            "Acapulco",
                            "El Roble",
                            "Arancibia",
                        ),
                    ),
                    Canton(
                        "Esparza",
                        listOf(
                            "Espíritu Santo",
                            "San Juan Grande",
                            "Macacona",
                            "San Rafael",
                            "San Jerónimo",
                            "Caldera",
                        ),
                    ),
                    Canton(
                        "Buenos Aires",
                        listOf(
                            "Buenos Aires",
                            "Volcán",
                            "Potrero Grande",
                            "Boruca",
                            "Pilas",
                            "Colinas",
                            "Chánguena",
                            "Biolley",
                            "Brunka",
                        ),
                    ),
                    Canton("Montes de Oro", listOf("Miramar", "La Unión", "San Isidro")),
                    Canton(
                        "Osa",
                        listOf(
                            "Puerto Cortés",
                            "Palmar",
                            "Sierpe",
                            "Bahía Ballena",
                            "Piedras Blancas",
                            "Bahía Drake",
                        ),
                    ),
                    Canton("Quepos", listOf("Quepos", "Savegre", "Naranjito")),
                    Canton("Golfito", listOf("Golfito", "Guaycará", "Pavón")),
                    Canton(
                        "Coto Brus",
                        listOf(
                            "San Vito",
                            "Sabalito",
                            "Aguabuena",
                            "Limoncito",
                            "Pittier",
                            "Gutiérrez Braun",
                        ),
                    ),
                    Canton("Parrita", listOf("Parrita")),
                    Canton("Corredores", listOf("Corredor", "La cuesta", "Canoas", "Laurel")),
                    Canton("Garabito", listOf("Jacó", "Tárcoles", "Lagunillas")),
                    Canton("Monteverde", listOf("Monteverde")),
                    Canton("Puerto Jiménez", listOf("Puerto Jiménez")),
                ),
        ),
        Province(
            name = "Limón",
            cantons =
                listOf(
                    Canton("Limón", listOf("Limón", "Valle La Estrella", "Río Blanco", "Matama")),
                    Canton(
                        "Pococí",
                        listOf(
                            "Guápiles",
                            "Jiménez",
                            "Rita",
                            "Roxana",
                            "Cariari",
                            "Colorado",
                            "La Colonia",
                        ),
                    ),
                    Canton(
                        "Siquirres",
                        listOf(
                            "Siquirres",
                            "Pacuarito",
                            "Florida",
                            "Germania",
                            "El Cairo",
                            "Alegría",
                            "Reventazón",
                        ),
                    ),
                    Canton("Talamanca", listOf("Bratsi", "Sixaola", "Cahuita", "Telire")),
                    Canton("Matina", listOf("Matina", "Batán", "Carrandí")),
                    Canton(
                        "Guácimo",
                        listOf("Guácimo", "Mercedes", "Pocora", "Río Jiménez", "Duacarí"),
                    ),
                ),
        ),
    )
