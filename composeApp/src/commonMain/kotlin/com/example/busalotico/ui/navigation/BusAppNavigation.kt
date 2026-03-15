package com.example.busalotico.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import com.example.busalotico.ui.screen.RouteDetailScreen
import com.example.busalotico.ui.screen.RouteSelectionScreen
import com.example.busalotico.ui.screen.home.HomeScreen
import com.example.busalotico.ui.screen.profile.ProfileScreen
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.Route
import com.example.shared.presentation.route.RouteDetailViewModel
import com.example.shared.presentation.route.RouteUiState
import com.example.shared.presentation.route.RouteViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BusAppNavigation(
    coordinate: Coordinate?,
    userId: String,
    userName: String,
    userEmail: String,
    userRole: String,
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    val routeViewModel = koinViewModel<RouteViewModel>()
    val detailViewModel = koinViewModel<RouteDetailViewModel>()
    val selectedId by detailViewModel.selectedRouteId.collectAsState()
    val isDetailEnabled = selectedId.isNotEmpty()

    val polylinePoints by detailViewModel.polylinePoints.collectAsState()
    val busLocation by detailViewModel.busLocation.collectAsState()

    val routeUiState by routeViewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: BusAppScreen.Home.name

    LaunchedEffect(userId) {
        routeViewModel.onUserChanged(userId.ifBlank { null })
    }

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
                NavigationBarItem(
                    selected = currentRoute == BusAppScreen.Profile.name,
                    onClick = { navController.navigate(BusAppScreen.Profile.name) },
                    label = { Text("Perfil") },
                    icon = { Text("👤") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BusAppScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
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

            composable(route = BusAppScreen.Routes.name) {
                RouteSelectionScreen(
                    uiState = routeUiState.toRouteSelectionUiState(),
                    onBackClick = { navController.popBackStack() },
                    onRouteClick = { routeItem ->
                        detailViewModel.updateSelectedRoute(routeItem.id)
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
                        navController.navigate(BusAppScreen.Detail.name)
                    },
                    onToggleFavorite = routeViewModel::toggleFavorite
                )
            }

            composable(route = BusAppScreen.Detail.name) {
                val currentCoord = coordinate
                if (currentCoord != null) {
                    RouteDetailScreen(
                        routeId = selectedId,
                        coordinate = currentCoord,
                        viewModel = detailViewModel
                    )
                } else {
                    Text("Esperando GPS para el detalle...")
                }
            }

            composable(route = BusAppScreen.Profile.name) {
                ProfileScreen(
                    userName = userName,
                    userEmail = userEmail,
                    userRole = userRole,
                    onSignOut = onSignOut
                )
            }
        }
    }
}

private fun RouteUiState.toRouteSelectionUiState(): RouteSelectionUiState {
    return RouteSelectionUiState(
        routes = routes.map { route -> route.toRouteListItemUi(favoriteRouteIds) },
        isLoading = isLoading,
        errorMessage = errorMessage
    )
}

private fun Route.toRouteListItemUi(
    favoriteRouteIds: Set<String>
): RouteListItemUi {
    return RouteListItemUi(
        id = id,
        code = code,
        title = name.toDisplayRouteName(),
        statusText = statusText,
        etaText = etaText,
        frequencyText = frequencyText,
        isActive = isActive,
        isLive = isLive,
        isFavorite = favoriteRouteIds.contains(id),
        province = startLocation.province,
        canton = startLocation.canton,
        district = startLocation.district
    )
}

private fun String.toDisplayRouteName(): String {
    return trim()
        .lowercase()
        .split(Regex("\\s+"))
        .joinToString(" ") { word ->
            word.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase() else char.toString()
            }
        }
}
