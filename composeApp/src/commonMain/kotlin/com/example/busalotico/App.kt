package com.example.busalotico

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import busalotico.composeapp.generated.resources.Res
import busalotico.composeapp.generated.resources.permission_location_description
import busalotico.composeapp.generated.resources.permission_location_title
import com.example.busalotico.data.PermissionRequest
import com.example.busalotico.ui.components.mapview.getLocationTracker
import com.example.busalotico.ui.navigation.BusAppNavigation
import com.example.busalotico.ui.screen.onboarding.PermissionsRequesterScreen
import com.example.shared.presentation.LocationViewModel
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.location.LOCATION
import org.jetbrains.compose.resources.stringResource
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
        // Mantenemos el PermissionsRequesterScreen como el filtro principal
        PermissionsRequesterScreen(
            permissions = listOf(
                PermissionRequest(
                    permission = Permission.LOCATION,
                    title = stringResource(Res.string.permission_location_title),
                    description = stringResource(Res.string.permission_location_description),
                    icon = Icons.Default.LocationOn
                )
            ),
            snackbarHostState = snackbarHostState
        ) { controller, isGranted ->

            // Lógica de GPS original
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

            // LLAMADO FINAL  AL NAVEGADOR
            // Le pasamos la coordenada (que puede ser nula) y él se encarga del resto
            BusAppNavigation(
                coordinate = coordinate
            )
        }
    }
}