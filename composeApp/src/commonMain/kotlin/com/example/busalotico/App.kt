package com.example.busalotico

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.busalotico.data.PermissionRequest
import com.example.busalotico.ui.components.mapview.getLocationTracker
import com.example.busalotico.ui.screen.HomeScreen
import com.example.busalotico.ui.screen.PermissionsRequesterScreen
import com.example.busalotico.ui.screen.RouteSelectionScreen
import com.example.busalotico.ui.screen.RouteSelectionTab
import com.example.busalotico.ui.screen.RouteSelectionUiState
import com.example.busalotico.ui.screen.toRouteListItemUi
import com.example.shared.presentation.LocationViewModel
import com.example.shared.presentation.RouteViewModel
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.location.LOCATION
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val locationViewModel = koinViewModel<LocationViewModel>()
    val routeViewModel = koinViewModel<RouteViewModel>()
    AppContent(
        locationViewModel = locationViewModel,
        routeViewModel = routeViewModel
    )
}

@Composable
fun AppContent(
    locationViewModel: LocationViewModel,
    routeViewModel: RouteViewModel
) {
    val coordinate by locationViewModel.coordinate.collectAsState()
    val routeUiState by routeViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showRouteScreen by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(RouteSelectionTab.ROUTES) }

    MaterialTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->

            PermissionsRequesterScreen(
                permissions = listOf(
                    PermissionRequest(
                        permission = Permission.LOCATION,
                        title = "Location",
                        description = "To show your location and nearby stops",
                        icon = Icons.Default.LocationOn
                    )
                ),
                snackbarHostState = snackbarHostState
            ) { controller, isGranted ->

                // We activate GPS only if permission has been granted.
                if (isGranted) {
                    val locationTracker = remember { getLocationTracker(controller) }
                    BindLocationTrackerEffect(locationTracker = locationTracker)

                    LaunchedEffect(Unit) {
                        locationViewModel.startTracking(locationTracker)
                    }

                    DisposableEffect(Unit) {
                        onDispose { locationViewModel.stopTracking() }
                    }
                }

                // default (without GPS active)
                if (showRouteScreen) {
                    val routeItems = remember(routeUiState.routes) {
                        routeUiState.routes.map { it.toRouteListItemUi() }
                    }

                    RouteSelectionScreen(
                        uiState = RouteSelectionUiState(
                            routes = routeItems,
                            selectedTab = selectedTab
                        ),
                        onBackClick = { showRouteScreen = false },
                        onMoreClick = { },
                        onRouteClick = { },
                        onTabClick = { selectedTab = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                } else {
                    HomeScreen(
                        coordinate = coordinate,
                        onSelectRouteClick = { showRouteScreen = true },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}
