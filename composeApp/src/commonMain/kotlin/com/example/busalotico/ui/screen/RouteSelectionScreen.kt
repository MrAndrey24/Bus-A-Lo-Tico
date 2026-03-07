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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busalotico.ui.components.searchBar.SearchBar
import com.example.shared.domain.entities.Route

enum class RouteSelectionTab(val label: String) {
    ROUTES("Rutas"),
    MAP("Mapa"),
    FAVORITES("Favoritos"),
    PROFILE("Perfil")
}

@Immutable
data class Province(val name: String, val cantons: List<Canton>)

@Immutable
data class Canton(val name: String, val districts: List<String>)

@Immutable
data class RouteListItemUi(
    val id: String,
    val code: String,
    val title: String,
    val statusText: String,
    val etaText: String,
    val frequencyText: String,
    val isActive: Boolean = true,
    val isLive: Boolean = true,
    val isFavorite: Boolean = false,
    val isNearMe: Boolean = true,
    val province: String? = null,
    val canton: String? = null,
    val district: String? = null
)

data class RouteSelectionUiState(
    val routes: List<RouteListItemUi>,
    val selectedTab: RouteSelectionTab = RouteSelectionTab.ROUTES
)

@Composable
fun RouteSelectionScreen(
    uiState: RouteSelectionUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onRouteClick: (RouteListItemUi) -> Unit = {},
    onTabClick: (RouteSelectionTab) -> Unit = {}
) {
    val provinces = remember { demoCRGeoData() }

    var query by remember { mutableStateOf("") }
    var showLocationPanel by remember { mutableStateOf(false) }
    var nearMe by remember { mutableStateOf(false) }
    var favoritesOnly by remember { mutableStateOf(false) }

    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedCanton by remember { mutableStateOf<Canton?>(null) }
    var selectedDistrict by remember { mutableStateOf<String?>(null) }

    val availableCantons = selectedProvince?.cantons ?: emptyList()
    val availableDistricts = selectedCanton?.districts ?: emptyList()

    val filteredRoutes = remember(
        uiState.routes,
        query,
        nearMe,
        favoritesOnly,
        selectedProvince,
        selectedCanton,
        selectedDistrict
    ) {
        uiState.routes.filter { route ->
            val queryMatches = if (query.isBlank()) {
                true
            } else {
                (route.title + " " + route.code).contains(query, ignoreCase = true)
            }

            val nearMeMatches = !nearMe || route.isNearMe
            val favoritesMatch = !favoritesOnly || route.isFavorite
            val provinceMatch = selectedProvince == null || route.province == selectedProvince?.name
            val cantonMatch = selectedCanton == null || route.canton == selectedCanton?.name
            val districtMatch = selectedDistrict == null || route.district == selectedDistrict

            queryMatches && nearMeMatches && favoritesMatch && provinceMatch && cantonMatch && districtMatch
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF4F4F6)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RouteSelectionTopBar(
                onBackClick = onBackClick,
                onMoreClick = onMoreClick
            )

            Divider(color = Color(0xFFE4E4E8))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(14.dp))

                SearchField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = "Buscar ruta, parada o lugar..."
                )

                Spacer(modifier = Modifier.height(10.dp))

                FilterChipsRow(
                    nearMe = nearMe,
                    onNearMeToggle = { nearMe = !nearMe },
                    locationLabel = buildLocationLabel(selectedProvince, selectedCanton, selectedDistrict),
                    locationSelected = showLocationPanel,
                    onLocationClick = { showLocationPanel = !showLocationPanel },
                    favoriteCount = uiState.routes.count { it.isFavorite },
                    favoritesOnly = favoritesOnly,
                    onFavoritesToggle = { favoritesOnly = !favoritesOnly }
                )

                AnimatedVisibility(visible = showLocationPanel) {
                    LocationPanel(
                        provinces = provinces,
                        selectedProvince = selectedProvince,
                        onProvinceSelected = { province ->
                            selectedProvince = province
                            selectedCanton = null
                            selectedDistrict = null
                        },
                        cantons = availableCantons,
                        selectedCanton = selectedCanton,
                        onCantonSelected = { canton ->
                            selectedCanton = canton
                            selectedDistrict = null
                        },
                        districts = availableDistricts,
                        selectedDistrict = selectedDistrict,
                        onDistrictSelected = { selectedDistrict = it },
                        onClear = {
                            selectedProvince = null
                            selectedCanton = null
                            selectedDistrict = null
                        },
                        onApply = { showLocationPanel = false }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RUTAS DISPONIBLES",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF7B7D84)
                    )

                    ActiveCountPill(count = filteredRoutes.count { it.isActive })
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredRoutes, key = { it.id }) { route ->
                        RouteSelectionCard(
                            route = route,
                            onClick = { onRouteClick(route) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(18.dp)) }
                }
            }

        }
    }
}

@Composable
private fun RouteSelectionTopBar(
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1E1F24)
                )
            }
            Column {
                Text(
                    text = "Bus A Lo Tico",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF17181C)
                )
                Text(
                    text = "SELECCIÓN DE RUTAS",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF8B8E95)
                )
            }
        }

        Text(
            text = "⋯",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF555861),
            modifier = Modifier
                .padding(end = 14.dp)
                .clickable { onMoreClick() }
        )
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    SearchBar(
        query = value,
        onQueryChange = onValueChange,
        placeholder = placeholder,
        modifier = Modifier.fillMaxWidth()
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
    onFavoritesToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChipManual(
            text = if (nearMe) "Cerca de mí ✓" else "Cerca de mí",
            selected = nearMe,
            onClick = onNearMeToggle
        )

        FilterChipManual(
            text = locationLabel,
            selected = locationSelected,
            onClick = onLocationClick
        )

        FilterChipManual(
            text = if (favoritesOnly) "Favoritas ($favoriteCount) ✓" else "Favoritas ($favoriteCount)",
            selected = favoritesOnly,
            onClick = onFavoritesToggle
        )
    }
}

@Composable
private fun FilterChipManual(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = Color.White
    val border = if (selected) Color(0xFFBFC3CC) else Color(0xFFD2D4DB)
    val fg = Color(0xFF2A2C33)

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = bg,
        border = androidx.compose.foundation.BorderStroke(1.dp, border),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = fg
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "▾",
                style = MaterialTheme.typography.labelSmall,
                color = fg.copy(alpha = 0.75f)
            )
        }
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
    onApply: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E6EB)),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Ubicación",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF2A2C33)
            )

            DropdownField(
                value = selectedProvince?.name ?: "",
                placeholder = "Selecciona una provincia",
                enabled = true,
                options = provinces.map { it.name },
                onSelect = { name -> provinces.firstOrNull { it.name == name }?.let(onProvinceSelected) }
            )

            DropdownField(
                value = selectedCanton?.name ?: "",
                placeholder = if (selectedProvince == null) "Primero elige provincia" else "Selecciona un cantón",
                enabled = selectedProvince != null,
                options = cantons.map { it.name },
                onSelect = { name -> cantons.firstOrNull { it.name == name }?.let(onCantonSelected) }
            )

            DropdownField(
                value = selectedDistrict ?: "",
                placeholder = if (selectedCanton == null) "Primero elige cantón" else "Selecciona un distrito",
                enabled = selectedCanton != null,
                options = districts,
                onSelect = onDistrictSelected
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onApply() },
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1B1D23)
                ) {
                    Text(
                        "Aplicar",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 12.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onClear() },
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE7E7EC)
                ) {
                    Text(
                        "Limpiar",
                        color = Color(0xFF66636F),
                        modifier = Modifier.padding(vertical = 12.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun DropdownField(
    value: String,
    placeholder: String,
    enabled: Boolean,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size
                }
                .clickable(enabled = enabled) { expanded = true },
            shape = RoundedCornerShape(16.dp),
            color = if (enabled) Color.White else Color(0xFFF8F8FA),
            border = BorderStroke(1.dp, Color(0xFFE5E6EB))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (value.isBlank()) placeholder else value,
                    color = if (value.isBlank()) Color(0xFF8A8D95) else Color(0xFF2A2C33),
                    fontSize = 15.sp
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF666A74)
                )
            }
        }

        DropdownMenu(
            modifier = Modifier.width(with(density) { textFieldSize.width.toDp() }),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(14.dp),
            containerColor = Color.White,
            tonalElevation = 0.dp,
            shadowElevation = 4.dp,
            border = BorderStroke(1.dp, Color(0xFFE5E6EB))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF2A2C33)
                        )
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color(0xFF2A2C33)
                    ),
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ActiveCountPill(count: Int) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E1E6)),
        color = Color(0xFFF9FAFB)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF7A64FF), CircleShape)
            )
            Spacer(modifier = Modifier.size(7.dp))
            Text(
                text = "$count activas",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6F7279)
            )
        }
    }
}

@Composable
private fun RouteSelectionCard(
    route: RouteListItemUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = route.isActive, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE5E6EB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RouteCodeTile(code = route.code, isActive = route.isActive)

            Spacer(modifier = Modifier.size(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = route.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1F2126),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (route.isLive) {
                        Text(
                            text = route.statusText,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF1DB954),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                    }

                    Text(
                        text = "◷ ${route.etaText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B6E75)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = route.frequencyText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8A8D94)
                )
            }

            Text(
                text = "›",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFD0D2D7)
            )
        }
    }
}

@Composable
private fun RouteCodeTile(code: String, isActive: Boolean) {
    val bg = if (isActive) Color(0xFF1B1D23) else Color(0xFFECECEF)
    val fg = if (isActive) Color.White else Color(0xFFA0A3AA)

    Surface(
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(12.dp),
        color = bg
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("RUTA", style = MaterialTheme.typography.labelSmall, color = fg.copy(alpha = 0.9f))
            Text(code, style = MaterialTheme.typography.titleMedium, color = fg, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun BottomTabs(
    selectedTab: RouteSelectionTab,
    onTabClick: (RouteSelectionTab) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE7E8ED))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RouteSelectionTab.entries.forEach { tab ->
                val selected = tab == selectedTab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onTabClick(tab) }
                ) {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) Color(0xFF1A1C21) else Color(0xFF9699A1),
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

private fun buildLocationLabel(
    province: Province?,
    canton: Canton?,
    district: String?
): String {
    return when {
        province == null -> "Ubicación"
        canton == null -> province.name
        district == null -> "${province.name} · ${canton.name}"
        else -> "${province.name} · ${canton.name} · $district"
    }
}

private fun demoCRGeoData(): List<Province> = listOf(
    Province(
        name = "San José",
        cantons = listOf(
            Canton("San José", listOf("Carmen", "Merced", "Hospital", "Catedral", "Zapote")),
            Canton("Escazú", listOf("Escazú", "San Antonio", "San Rafael")),
            Canton("Desamparados", listOf("Desamparados", "San Miguel", "San Juan de Dios"))
        )
    ),
    Province(
        name = "Alajuela",
        cantons = listOf(
            Canton("Alajuela", listOf("Alajuela", "San José", "Carrizal", "San Antonio")),
            Canton("San Ramón", listOf("San Ramón", "Santiago", "San Juan")),
            Canton("Grecia", listOf("Grecia", "San Isidro", "San Roque"))
        )
    ),
    Province(
        name = "Cartago",
        cantons = listOf(
            Canton("Cartago", listOf("Oriental", "Occidental", "Carmen", "San Nicolás")),
            Canton("La Unión", listOf("Tres Ríos", "San Diego", "San Juan"))
        )
    )
)

fun Route.toRouteListItemUi(
    etaText: String = "8 mins",
    frequencyText: String = "Cada 10 minutos"
): RouteListItemUi {
    val digits = id.filter { it.isDigit() }
    val routeCode = if (digits.isNotBlank()) digits.takeLast(3).padStart(3, '0') else "000"

    return RouteListItemUi(
        id = id,
        code = routeCode,
        title = name,
        statusText = "EN TIEMPO REAL",
        etaText = etaText,
        frequencyText = frequencyText,
        isActive = true,
        isLive = true,
        isFavorite = false,
        isNearMe = true,
        province = "San José",
        canton = "San José",
        district = "Catedral"
    )
}

fun demoRouteSelectionItems(): List<RouteListItemUi> = listOf(
    RouteListItemUi(
        id = "102",
        code = "102",
        title = "San José - Sabana Estadio",
        statusText = "EN TIEMPO REAL",
        etaText = "5 mins",
        frequencyText = "Cada 10 minutos",
        isFavorite = true,
        isNearMe = true,
        province = "San José",
        canton = "San José",
        district = "Catedral"
    ),
    RouteListItemUi(
        id = "402",
        code = "402",
        title = "Alajuela - Heredia",
        statusText = "EN TIEMPO REAL",
        etaText = "12 mins",
        frequencyText = "Cada 20 minutos",
        isFavorite = false,
        isNearMe = true,
        province = "Alajuela",
        canton = "Alajuela",
        district = "Carrizal"
    ),
    RouteListItemUi(
        id = "058",
        code = "058",
        title = "Escazú - Multiplaza",
        statusText = "LLEGANDO",
        etaText = "Ahora",
        frequencyText = "Cada 15 minutos",
        isFavorite = true,
        isNearMe = true,
        province = "San José",
        canton = "Escazú",
        district = "Escazú"
    ),
    RouteListItemUi(
        id = "215",
        code = "215",
        title = "Cartago - Tres Ríos",
        statusText = "SIN DATOS",
        etaText = "25 mins",
        frequencyText = "Cada 30 minutos",
        isActive = false,
        isLive = false,
        isFavorite = false,
        isNearMe = false,
        province = "Cartago",
        canton = "La Unión",
        district = "Tres Ríos"
    ),
    RouteListItemUi(
        id = "312",
        code = "312",
        title = "San Pedro - UCR",
        statusText = "EN TIEMPO REAL",
        etaText = "8 mins",
        frequencyText = "Cada 5 minutos",
        isFavorite = true,
        isNearMe = true,
        province = "San José",
        canton = "San José",
        district = "Carmen"
    )
)
