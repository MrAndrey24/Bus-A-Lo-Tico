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
import com.example.busalotico.ui.screen.auth.AuthScreen
import com.example.busalotico.ui.screen.onboarding.PermissionsRequesterScreen
import com.example.shared.domain.entities.UserRole
import com.example.shared.presentation.auth.AuthViewModel
import com.example.shared.presentation.location.LocationViewModel
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.location.LOCATION
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val locationViewModel = koinViewModel<LocationViewModel>()

    val authViewModel = koinViewModel<AuthViewModel>()

    AppContent(
        locationViewModel = locationViewModel,
        authViewModel = authViewModel
    )
}
@Composable
fun AppContent(
    locationViewModel: LocationViewModel,
    authViewModel: AuthViewModel
) {
    val coordinate by locationViewModel.coordinate.collectAsState()

    val authUiState by authViewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme {
        //Si el usuario no está autenticado, se muestra AuthScreen.
        if (!authUiState.isAuthenticated) {
            AuthScreen(
                uiState = authUiState,
                onSignIn = authViewModel::signIn,
                onSignUp = authViewModel::signUp
            )
            return@MaterialTheme
        }

        PermissionsRequesterScreen(
            permissions = listOf(
                PermissionRequest(
                    permission = Permission.LOCATION,
                    title = stringResource(Res.string.permission_location_title),
                    description = stringResource(Res.string.permission_location_description),
                    icon = Icons.Default.LocationOn
                )
            ),
            snackBarHostState = snackbarHostState
        ) { controller, _ ->
            val locationTracker = remember { getLocationTracker(controller) }
            BindLocationTrackerEffect(locationTracker = locationTracker)

            LaunchedEffect(Unit) {
                locationViewModel.startTracking(locationTracker)
            }

            DisposableEffect(Unit) {
                onDispose { locationViewModel.stopTracking() }
            }

            BusAppNavigation(
                coordinate = coordinate,
                userId = authUiState.currentUser?.id.orEmpty(),
                userName = authUiState.currentUser?.name.orEmpty(),
                userEmail = authUiState.currentUser?.email.orEmpty(),
                userRole = authUiState.currentUser?.role?.toDisplayName().orEmpty(),
                onSignOut = authViewModel::signOut
            )
        }
    }
}

private fun UserRole.toDisplayName(): String {
    return when (this) {
        UserRole.PASSENGER -> "Pasajero"
        UserRole.DRIVER -> "Conductor"
        UserRole.ADMIN -> "Administrador"
    }
}
