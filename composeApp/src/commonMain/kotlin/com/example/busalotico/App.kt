package com.example.busalotico

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.busalotico.data.PermissionRequest
import com.example.busalotico.ui.components.mapview.MapViewComponent
import com.example.busalotico.ui.components.mapview.getLocationTracker
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

    MaterialTheme {
        Scaffold { innerPadding ->
            PermissionsRequesterScreen(
                permissions = listOf(
                    PermissionRequest(
                        permission = Permission.LOCATION,
                        title = "Ubicación",
                        description = "Para mostrar tu ubicación y los paraderos cercanos",
                        icon = Icons.Default.LocationOn
                    )
                ),
                onAllPermissionsGranted = {
                    println("All permissions granted!")
                }
            ) { controller ->

                val locationTracker = remember {
                    getLocationTracker(controller)
                }

                BindLocationTrackerEffect(locationTracker = locationTracker)

                // Simplemente le decimos al ViewModel que inicie el rastreo
                // Esto se ejecuta una vez que entramos a este bloque (permisos concedidos)
                LaunchedEffect(Unit) {
                    println("Starting location tracking from ViewModel...")
                    viewModel.startTracking(locationTracker)
                }

                // Si el usuario sale de esta pantalla, se detiene el rastreo
                DisposableEffect(Unit) {
                    onDispose {
                        println("Stopping location tracking...")
                        viewModel.stopTracking()
                    }
                }

                // Manejo de Estado UI: Mostrar Mapa vs Cargando
                MapViewComponent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    coordinate = coordinate
                )
            }
        }
    }
}