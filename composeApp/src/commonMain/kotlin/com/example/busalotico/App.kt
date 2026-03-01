package com.example.busalotico

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.busalotico.data.PermissionRequest
import com.example.busalotico.ui.components.mapview.getLocationTracker
import com.example.busalotico.ui.screen.HomeScreen
import com.example.busalotico.ui.screen.PermissionsRequesterScreen
import com.example.shared.presentation.LocationViewModel
import com.example.shared.presentation.navigation.BusAppNavigation // Importamos tu nuevo menú
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
        // Mantenemos el PermissionsRequesterScreen como el filtro principal
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

            // Mantenemos tu lógica de activación de GPS original
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

            // CAMBIO CLAVE: Llamamos al Navegador y le pasamos el mapa por el "Slot"
            BusAppNavigation(
                mapContent = {
                    HomeScreen(
                        coordinate = coordinate,
                        modifier = Modifier.fillMaxSize()
                        // Quitamos el padding aquí porque BusAppNavigation ya lo maneja internamente
                    )
                }
            )
        }
    }
}