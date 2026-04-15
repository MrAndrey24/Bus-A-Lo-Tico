package com.example.busalotico.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busalotico.ui.components.RecenterButton
import com.example.busalotico.ui.components.ZoomControls
import com.example.busalotico.ui.components.mapview.MapViewComponent
import com.example.busalotico.ui.components.route.RouteSelectionCard
import com.example.busalotico.ui.components.searchBar.SearchBar
import com.example.busalotico.ui.model.mapper.toRouteListItemUi
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteStop
import com.example.shared.presentation.route.RouteUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    coordinate: Coordinate,
    uiState: RouteUiState,
    onSearchQueryChange: (String) -> Unit,
    onRouteSelected: (Route) -> Unit,
    modifier: Modifier = Modifier,
    onSelectRouteClick: () -> Unit = {},
    onToggleFavorite: (String, Boolean) -> Unit = { _, _ -> },
    onAdministrativeDetailClick: (String) -> Unit = {},
    polylinePoints: List<Coordinate> = emptyList(),
    stops: List<RouteStop> = emptyList(),
    busLocation: Coordinate? = null,
    flyToLocation: Coordinate? = null
) {
    var recenterTrigger by remember { mutableIntStateOf(0) }
    var zoomInTrigger by remember { mutableIntStateOf(0) }
    var zoomOutTrigger by remember { mutableIntStateOf(0) }

    val favoriteRoutes = remember(uiState.routes, uiState.favoriteRouteIds) {
        uiState.routes.filter { uiState.favoriteRouteIds.contains(it.id) }
            .map { it.toRouteListItemUi(uiState.favoriteRouteIds) }
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true,
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 220.dp,
        sheetContainerColor = Color(0xFFF1F3F6),
        sheetContent = {
            // Bottom Section: Favorites / Subscribed Routes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MIS RUTAS",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.2.sp
                    )
                    
                    TextButton(onClick = onSelectRouteClick) {
                        Text("Ver todas", style = MaterialTheme.typography.labelLarge)
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (favoriteRoutes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No tienes rutas favoritas aún",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = onSelectRouteClick) {
                                Text("Explorar rutas")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(favoriteRoutes, key = { it.id }) { route ->
                            RouteSelectionCard(
                                route = route,
                                onClick = { 
                                    uiState.routes.find { it.id == route.id }?.let { onRouteSelected(it) }
                                },
                                onToggleFavorite = { 
                                    onToggleFavorite(route.id, !route.isFavorite)
                                },
                                onShowAdministrativeDetail = { 
                                    onAdministrativeDetailClick(route.id)
                                }
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            MapViewComponent(
                coordinate = coordinate,
                recenterTrigger = recenterTrigger,
                zoomInTrigger = zoomInTrigger,
                zoomOutTrigger = zoomOutTrigger,
                polylinePoints = polylinePoints,
                stops = stops,
                busLocation = busLocation,
                flyToLocation = flyToLocation
            )

            // Overlays on Map
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChange,
                isSearching = uiState.isSearching,
                searchResults = uiState.searchResults,
                onRouteSelected = onRouteSelected,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 240.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ZoomControls(
                    onZoomIn = { zoomInTrigger++ },
                    onZoomOut = { zoomOutTrigger++ }
                )
                RecenterButton(
                    onClick = { recenterTrigger++ }
                )
            }
        }
    }
}
