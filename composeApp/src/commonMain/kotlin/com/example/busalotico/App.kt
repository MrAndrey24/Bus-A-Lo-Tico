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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.busalotico.data.PermissionRequest
import com.example.busalotico.ui.components.mapview.getLocationTracker
import com.example.busalotico.ui.screen.HomeScreen
import com.example.busalotico.ui.screen.PermissionsRequesterScreen
import com.example.shared.presentation.LocationViewModel
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.location.LOCATION
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val viewModel = koinViewModel<LocationViewModel>()
    AppContent(viewModel = viewModel)
}

@Composable
fun AppContent(viewModel: LocationViewModel) {
    val coordinate by viewModel.coordinate.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
                        viewModel.startTracking(locationTracker)
                    }

                    DisposableEffect(Unit) {
                        onDispose { viewModel.stopTracking() }
                    }
                }

                // default (without GPS active)
                HomeScreen(
                    coordinate = coordinate,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}