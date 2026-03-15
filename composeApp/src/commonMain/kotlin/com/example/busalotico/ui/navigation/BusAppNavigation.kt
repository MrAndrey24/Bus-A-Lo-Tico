package com.example.busalotico.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.busalotico.ui.model.RouteListItemUi
import com.example.busalotico.ui.model.RouteSelectionUiState
import com.example.busalotico.ui.screen.RouteDetailScreen
import com.example.busalotico.ui.screen.RouteSelectionScreen
import com.example.busalotico.ui.screen.home.HomeScreen
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.Route
import com.example.shared.presentation.route.RouteDetailViewModel
import com.example.shared.presentation.route.RouteUiState
import com.example.shared.presentation.route.RouteViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BusAppNavigation(
    coordinate: Coordinate?
) {
    val navController = rememberNavController()
    val routeViewModel = koinViewModel<RouteViewModel>()

    // ViewModel compartido (single en Koin)
    val detailViewModel = koinViewModel<RouteDetailViewModel>()
    val selectedId by detailViewModel.selectedRouteId.collectAsState()
    val isDetailEnabled = selectedId.isNotEmpty()

    val polylinePoints by detailViewModel.polylinePoints.collectAsState()
    val busLocation by detailViewModel.busLocation.collectAsState()

    val routeUiState by routeViewModel.uiState.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: BusAppScreen.Home.name

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == BusAppScreen.Home.name,
                    onClick = { navController.navigate(BusAppScreen.Home.name) },
                    label = { Text("Mapa") },
                    icon = { Text("📍") }
                )
                NavigationBarItem(
                    selected = currentRoute == BusAppScreen.Routes.name,
                    onClick = { navController.navigate(BusAppScreen.Routes.name) },
                    label = { Text("Rutas") },
                    icon = { Text("🚌") }
                )
                NavigationBarItem(
                    selected = currentRoute == BusAppScreen.Detail.name,
                    enabled = isDetailEnabled,
                    onClick = {
                        if (isDetailEnabled) {
                            navController.navigate(BusAppScreen.Detail.name)
                        }
                    },
                    label = {
                        Text(
                            text = "Detalle",
                            color = if (isDetailEnabled) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            }
                        )
                    },
                    icon = {
                        CompositionLocalProvider(
                            LocalContentColor provides if (isDetailEnabled)
                                MaterialTheme.colorScheme.onSurface
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        ) {
                            Text("📋")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BusAppScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Pantalla de Mapa (Home)
            composable(route = BusAppScreen.Home.name) {
                if (coordinate != null) {
                    HomeScreen(
                        coordinate = coordinate,
                        onSelectRouteClick = { navController.navigate(BusAppScreen.Routes.name) },
                        modifier = Modifier.fillMaxSize(),
                        polylinePoints = polylinePoints,
                        busLocation = busLocation
                    )
                } else {
                    Text("Buscando señal GPS...")
                }
            }

            // 2. Pantalla de Selección de Rutas
            composable(route = BusAppScreen.Routes.name) {
                RouteSelectionScreen(
                    uiState = routeUiState.toRouteSelectionUiState(),
                    onBackClick = { navController.popBackStack() },
                    onRouteClick = { routeItem ->
                        // Guardamos el ID dinámico
                        detailViewModel.updateSelectedRoute(routeItem.id)

                        // Cargamos los puntos de prueba en el ViewModel compartido
                        val paradasDePrueba = listOf(
                            Coordinate(9.984506, -84.729457),
                            Coordinate(9.982033, -84.734038),
                            Coordinate(9.979011, -84.734060),
                            Coordinate(9.978060, -84.735616),
                            Coordinate(9.980385, -84.735727),
                            Coordinate(9.981832, -84.735448),
                            Coordinate(9.983544, -84.735658),
                            Coordinate(9.984326, -84.734534),
                            Coordinate(9.985152, -84.732383),
                            Coordinate(9.986002, -84.730219),
                            Coordinate(9.984506, -84.729457),
                        )
                        detailViewModel.loadMapboxLine(paradasDePrueba)

                        // NAVEGACIÓN: Ahora sí existe el destino "Detail"
                        navController.navigate(BusAppScreen.Detail.name)
                    }
                )
            }

            // 3. Pantalla de Detalle (LA QUE FALTABA)
            composable(route = BusAppScreen.Detail.name) {
                val currentCoord = coordinate
                if (currentCoord != null) {
                    RouteDetailScreen(
                        routeId = selectedId,
                        coordinate = currentCoord,
                        viewModel = detailViewModel // Le pasamos el mismo VM para que vea los puntos
                    )
                } else {
                    Text("Esperando GPS para el detalle...")
                }
            }
        }
    }
}

// Helpers de conversión
private fun RouteUiState.toRouteSelectionUiState(): RouteSelectionUiState {
    return RouteSelectionUiState(
        routes = routes.map { route -> route.toRouteListItemUi() },
        isLoading = isLoading,
        errorMessage = errorMessage
    )
}

private fun Route.toRouteListItemUi(): RouteListItemUi {
    return RouteListItemUi(
        id = id,
        code = code.ifBlank { id.takeLast(3) },
        title = name,
        statusText = statusText,
        etaText = etaText,
        frequencyText = frequencyText,
        isActive = isActive,
        isLive = isLive,
        isFavorite = isFavorite
    )
}