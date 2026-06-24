/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.busalotico

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.busalotico.config.DevSessionConfig
import com.example.busalotico.data.PermissionRequest
import com.example.busalotico.ui.components.mapview.BindPlatformLocationTrackerEffect
import com.example.busalotico.ui.components.mapview.getLocationTracker
import com.example.busalotico.ui.navigation.RoleBasedNavigation
import com.example.busalotico.ui.screen.auth.AuthScreen
import com.example.busalotico.ui.screen.onboarding.PermissionsRequesterScreen
import com.example.busalotico.ui.screen.splash.SplashScreen
import com.example.busalotico.ui.theme.AppTheme
import com.example.sharedlogic.domain.entities.UserRole
import com.example.sharedlogic.presentation.auth.AuthViewModel
import com.example.sharedlogic.presentation.company.CompanyListViewModel
import com.example.sharedlogic.presentation.location.LocationViewModel
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.location.LOCATION
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
  AppTheme {
    val viewModel = koinViewModel<LocationViewModel>()
    val authViewModel = koinViewModel<AuthViewModel>()
    val companyListViewModel = koinViewModel<CompanyListViewModel>()
    AppContent(
        viewModel = viewModel,
        authViewModel = authViewModel,
        companyListViewModel = companyListViewModel,
    )
  }
}

@Composable
fun AppContent(
    viewModel: LocationViewModel,
    authViewModel: AuthViewModel,
    companyListViewModel: CompanyListViewModel,
) {
  val coordinate by viewModel.coordinate.collectAsState()
  val authUiState by authViewModel.uiState.collectAsState()
  val companyListUiState by companyListViewModel.uiState.collectAsState()
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
    delay(1300.milliseconds)
    showSplash = false
  }

  if (showSplash) {
    SplashScreen()
    return
  }

  if (!isAuthenticated) {
    AuthScreen(
        uiState = authUiState,
        onSignIn = authViewModel::signIn,
        onSignUp = authViewModel::signUp,
    )
    return
  }

  LaunchedEffect(isAuthenticated, currentUser?.role) {
    if (
        isAuthenticated &&
            (currentUser?.role == UserRole.ADMIN || currentUser?.role == UserRole.DRIVER)
    ) {
      companyListViewModel.loadCompanies()
    }
  }

  PermissionsRequesterScreen(
      permissions =
          listOf(
              PermissionRequest(
                  permission = Permission.LOCATION,
                  title = "Ubicación",
                  description = "Para mostrar tu ubicación y paradas cercanas",
                  icon = Icons.Default.LocationOn,
              )
          ),
      snackBarHostState = snackbarHostState,
  ) { controller, _ ->
    val locationTracker = remember { getLocationTracker(controller) }
    BindPlatformLocationTrackerEffect(locationTracker = locationTracker)

    LaunchedEffect(Unit) { viewModel.startTracking(locationTracker) }

    DisposableEffect(Unit) { onDispose { viewModel.stopTracking() } }

    RoleBasedNavigation(
        role = currentUser?.role ?: UserRole.PASSENGER,
        coordinate = coordinate,
        userId = currentUser?.id.orEmpty(),
        userName = currentUser?.name.orEmpty(),
        userEmail = currentUser?.email.orEmpty(),
        userRoleLabel = currentUser?.role?.toDisplayName().orEmpty(),
        companyId = currentUser?.companyId,
        companies = companyListUiState.companies,
        companiesLoading = companyListUiState.isLoading,
        companyLoadError = companyListUiState.errorMessage,
        onUpdateCompany = authViewModel::updateCurrentUserCompany,
        onSignOut =
            if (DevSessionConfig.BYPASS_AUTH) {
              {}
            } else {
              authViewModel::signOut
            },
    )
  }
}

private fun UserRole.toDisplayName(): String {
  return when (this) {
    UserRole.PASSENGER -> "Pasajero"
    UserRole.DRIVER -> "Conductor"
    UserRole.ADMIN -> "Administrador"
  }
}
