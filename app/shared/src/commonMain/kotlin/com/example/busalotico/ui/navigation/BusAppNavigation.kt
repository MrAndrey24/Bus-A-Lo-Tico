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
package com.example.busalotico.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.busalotico.ui.model.mapper.toRouteSelectionUiState
import com.example.busalotico.ui.screen.RouteDetailScreen
import com.example.busalotico.ui.screen.RouteSelectionScreen
import com.example.busalotico.ui.screen.home.HomeScreen
import com.example.busalotico.ui.screen.notification.NotificationScreen
import com.example.busalotico.ui.screen.profile.ProfileScreen
import com.example.busalotico.ui.screen.routes.RouteAdministrativeDetailScreen
import com.example.shared.domain.entities.Coordinate
import com.example.shared.presentation.notification.NotificationViewModel
import com.example.shared.presentation.route.RouteDetailViewModel
import com.example.shared.presentation.route.RouteViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BusAppNavigation(
    coordinate: Coordinate?,
    userId: String,
    userName: String,
    userEmail: String,
    userRole: String,
    onSignOut: () -> Unit,
) {
  val navController = rememberNavController()
  val routeViewModel = koinViewModel<RouteViewModel>()
  val detailViewModel = koinViewModel<RouteDetailViewModel>()
  val selectedId by detailViewModel.selectedRouteId.collectAsState()
  val stops by detailViewModel.stops.collectAsState()
  val isDetailEnabled = selectedId.isNotEmpty()

  val polylinePoints by detailViewModel.polylinePoints.collectAsState()
  val busLocation by detailViewModel.busLocation.collectAsState()

  val routeUiState by routeViewModel.uiState.collectAsState()
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = backStackEntry?.destination?.route ?: BusAppScreen.Home.name

  var flyToLocation by remember { mutableStateOf<Coordinate?>(null) }

  LaunchedEffect(userId) { routeViewModel.onUserChanged(userId.ifBlank { null }) }

  Scaffold(
      bottomBar = {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
          NavigationBarItem(
              selected = currentRoute == BusAppScreen.Home.name,
              onClick = { navController.navigate(BusAppScreen.Home.name) },
              label = { Text("Mapa") },
              icon = { Icon(imageVector = Icons.Default.Map, contentDescription = "Mapa") },
              colors =
                  NavigationBarItemDefaults.colors(
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                  ),
          )
          NavigationBarItem(
              selected = currentRoute == BusAppScreen.Routes.name,
              onClick = { navController.navigate(BusAppScreen.Routes.name) },
              label = { Text("Rutas") },
              icon = {
                Icon(imageVector = Icons.Default.DirectionsBus, contentDescription = "Rutas")
              },
              colors =
                  NavigationBarItemDefaults.colors(
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                  ),
          )
          NavigationBarItem(
              selected = currentRoute == BusAppScreen.Detail.name,
              enabled = isDetailEnabled,
              onClick = { if (isDetailEnabled) navController.navigate(BusAppScreen.Detail.name) },
              label = { Text("Detalle") },
              icon = { Icon(imageVector = Icons.Default.Route, contentDescription = "Detalle") },
              colors =
                  NavigationBarItemDefaults.colors(
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                      disabledIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                      disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                  ),
          )
          NavigationBarItem(
              selected = currentRoute == BusAppScreen.Notifications.name,
              onClick = { navController.navigate(BusAppScreen.Notifications.name) },
              label = { Text("Avisos") },
              icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                )
              },
              colors =
                  NavigationBarItemDefaults.colors(
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                  ),
          )
          NavigationBarItem(
              selected = currentRoute == BusAppScreen.Profile.name,
              onClick = { navController.navigate(BusAppScreen.Profile.name) },
              label = { Text("Perfil") },
              icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil") },
              colors =
                  NavigationBarItemDefaults.colors(
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                  ),
          )
        }
      }
  ) { innerPadding ->
    NavHost(
        navController = navController,
        startDestination = BusAppScreen.Home.name,
        modifier = Modifier.padding(innerPadding),
    ) {
      composable(route = BusAppScreen.Home.name) {
        if (coordinate != null) {
          HomeScreen(
              coordinate = coordinate,
              uiState = routeUiState,
              onSearchQueryChange = routeViewModel::onSearchQueryChanged,
              onRouteSelected = { route ->
                routeViewModel.onRouteSelected(route)
                detailViewModel.loadStops(route.id)
              },
              onSelectRouteClick = { navController.navigate(BusAppScreen.Routes.name) },
              onToggleFavorite = routeViewModel::toggleFavorite,
              onAdministrativeDetailClick = { routeId ->
                navController.navigate("$AdministrativeRouteDestination/$routeId")
              },
              modifier = Modifier.fillMaxSize(),
              polylinePoints = polylinePoints,
              stops = stops,
              busLocation = busLocation,
              flyToLocation = flyToLocation,
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
              detailViewModel.loadStops(routeItem.id)
              navController.navigate(BusAppScreen.Detail.name)
            },
            onAdministrativeDetailClick = { routeItem ->
              navController.navigate("$AdministrativeRouteDestination/${routeItem.id}")
            },
            onToggleFavorite = routeViewModel::toggleFavorite,
        )
      }

      composable(route = BusAppScreen.Detail.name) {
        val currentCoord = coordinate
        if (currentCoord != null) {
          RouteDetailScreen(
              routeId = selectedId,
              coordinate = currentCoord,
              viewModel = detailViewModel,
              onBackClick = { navController.popBackStack() },
              navigateToMapOnComplete = true, // 👈 cambiá a false para ver versión sin navegación
              onSimulationComplete = {
                navController.navigate(BusAppScreen.Home.name) {
                  popUpTo(BusAppScreen.Home.name) { inclusive = false }
                }
              },
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
            onSignOut = onSignOut,
            onOpenNotifications = { navController.navigate(BusAppScreen.Notifications.name) },
        )
      }

      composable(route = BusAppScreen.Notifications.name) {
        val notificationViewModel = koinViewModel<NotificationViewModel>()
        NotificationScreen(
            viewModel = notificationViewModel,
            userId = userId,
            onBack = { navController.popBackStack() },
        )
      }

      composable(
          route = "$AdministrativeRouteDestination/{routeId}",
          arguments = listOf(navArgument("routeId") { type = NavType.StringType }),
      ) { backStackEntry ->
        RouteAdministrativeDetailScreen(
            routeId = backStackEntry.arguments?.getString("routeId").orEmpty(),
            onBackClick = { navController.popBackStack() },
        )
      }
    }
  }
}

private const val AdministrativeRouteDestination = "administrative_route_detail"
