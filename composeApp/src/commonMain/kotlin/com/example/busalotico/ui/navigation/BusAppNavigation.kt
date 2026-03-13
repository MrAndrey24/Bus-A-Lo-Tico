package com.example.busalotico.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.busalotico.ui.screen.RouteSelectionScreen
import com.example.busalotico.ui.screen.home.HomeScreen
import com.example.busalotico.ui.screen.profile.ProfileScreen
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.Route
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
                    onClick = { navController.navigate(BusAppScreen.Detail.name) },
                    label = { Text("Detalle") },
                    icon = { Text("📋") }
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
                // LÓGICA DE SEGURIDAD: Solo entramos si hay coordenadas
                val currentCoord = coordinate
                if (currentCoord != null) {
                    HomeScreen(
                        coordinate = currentCoord,
                        onSelectRouteClick = { navController.navigate(BusAppScreen.Routes.name) },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Buscando señal GPS...")
                }
            }

            composable(route = BusAppScreen.Routes.name) {
                RouteSelectionScreen(
                    uiState = routeUiState.toRouteSelectionUiState(),
                    onBackClick = { navController.popBackStack() },
                    onToggleFavorite = routeViewModel::toggleFavorite
                )
            }

            composable(route = BusAppScreen.Detail.name) {
                Text("Pantalla de Detalle")
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
