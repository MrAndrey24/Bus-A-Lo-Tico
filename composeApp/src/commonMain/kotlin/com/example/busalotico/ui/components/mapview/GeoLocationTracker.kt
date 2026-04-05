package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import com.example.shared.domain.location.PlatformLocationTracker
import dev.icerock.moko.permissions.PermissionsController

expect fun getLocationTracker(permissionsController: PermissionsController): PlatformLocationTracker

@Composable
expect fun BindPlatformLocationTrackerEffect(locationTracker: PlatformLocationTracker)
