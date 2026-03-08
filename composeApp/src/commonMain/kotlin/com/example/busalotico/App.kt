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
import com.example.busalotico.ui.model.RouteSelectionUiState
import com.example.busalotico.ui.screen.HomeScreen
import com.example.busalotico.ui.screen.PermissionsRequesterScreen
import com.example.busalotico.ui.screen.RouteSelectionScreen
import com.example.busalotico.ui.screen.toRouteListItemUi
import com.example.shared.presentation.LocationViewModel
import com.example.shared.presentation.RouteViewModel
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.location.LOCATION
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
                    val routeItems = remember(routeUiState.routes, coordinate) {
                        routeUiState.routes.map { route ->
                            route.toRouteListItemUi(
                                isNearMe = isRouteNearUser(
                                    userLat = coordinate.latitude,
                                    userLon = coordinate.longitude,
                                    routeLat = route.startLatitude,
                                    routeLon = route.startLongitude
                                )
                            )
                        }
                    }

                    RouteSelectionScreen(
                        uiState = RouteSelectionUiState(
                            routes = routeItems,
                            isLoading = routeUiState.isLoading,
                            errorMessage = routeUiState.errorMessage
                        ),
                        onBackClick = { showRouteScreen = false },
                        onMoreClick = { },
                        onRouteClick = { },
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

private fun isRouteNearUser(
    userLat: Double,
    userLon: Double,
    routeLat: Double,
    routeLon: Double,
    maxDistanceKm: Double = 5.0
): Boolean {
    return distanceKm(
        lat1 = userLat,
        lon1 = userLon,
        lat2 = routeLat,
        lon2 = routeLon
    ) <= maxDistanceKm
}

private fun distanceKm(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val earthRadiusKm = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
        cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
        sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadiusKm * c
}
