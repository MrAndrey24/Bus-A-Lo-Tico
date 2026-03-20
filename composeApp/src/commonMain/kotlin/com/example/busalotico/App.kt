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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import busalotico.composeapp.generated.resources.Res
import busalotico.composeapp.generated.resources.permission_location_description
import busalotico.composeapp.generated.resources.permission_location_title
import com.example.busalotico.config.DevSessionConfig
import com.example.busalotico.data.PermissionRequest
import com.example.busalotico.ui.components.mapview.getLocationTracker
import com.example.busalotico.ui.navigation.BusAppNavigation
import com.example.busalotico.ui.screen.auth.AuthScreen
import com.example.busalotico.ui.screen.onboarding.PermissionsRequesterScreen
import com.example.busalotico.ui.screen.splash.SplashScreen
import com.example.busalotico.ui.theme.AppTheme
import com.example.shared.domain.entities.UserRole
import com.example.shared.presentation.auth.AuthViewModel
import com.example.shared.presentation.location.LocationViewModel
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
  AppTheme {
    val viewModel = koinViewModel<LocationViewModel>()
    val authViewModel = koinViewModel<AuthViewModel>()
    AppContent(viewModel = viewModel, authViewModel = authViewModel)
  }
}

@Composable
fun AppContent(viewModel: LocationViewModel, authViewModel: AuthViewModel) {
  val coordinate by viewModel.coordinate.collectAsState()
  val authUiState by authViewModel.uiState.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }
  var showSplash by remember { mutableStateOf(true) }
  val currentUser =
      if (DevSessionConfig.BYPASS_AUTH) {
        DevSessionConfig.devUser
      } else {
        authUiState.currentUser
      }
  val isAuthenticated = DevSessionConfig.BYPASS_AUTH || authUiState.isAuthenticated

  LaunchedEffect(Unit) {
    delay(1300)
    showSplash = false
  }

  MaterialTheme {
    if (showSplash) {
      SplashScreen()
      return@MaterialTheme
    }

    if (!isAuthenticated) {
      AuthScreen(
          uiState = authUiState,
          onSignIn = authViewModel::signIn,
          onSignUp = authViewModel::signUp,
      )
      return@MaterialTheme
    }

    PermissionsRequesterScreen(
        permissions =
            listOf(
                PermissionRequest(
                    permission = Permission.LOCATION,
                    title = stringResource(Res.string.permission_location_title),
                    description = stringResource(Res.string.permission_location_description),
                    icon = Icons.Default.LocationOn,
                )
            ),
        snackBarHostState = snackbarHostState,
    ) { controller, _ ->
      val locationTracker = remember { getLocationTracker(controller) }
      BindLocationTrackerEffect(locationTracker = locationTracker)

      LaunchedEffect(Unit) { viewModel.startTracking(locationTracker) }

      DisposableEffect(Unit) { onDispose { viewModel.stopTracking() } }

      BusAppNavigation(
          coordinate = coordinate,
          userId = currentUser?.id.orEmpty(),
          userName = currentUser?.name.orEmpty(),
          userEmail = currentUser?.email.orEmpty(),
          userRole = currentUser?.role?.toDisplayName().orEmpty(),
          onSignOut =
              if (DevSessionConfig.BYPASS_AUTH) {
                {}
              } else {
                authViewModel::signOut
              },
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
