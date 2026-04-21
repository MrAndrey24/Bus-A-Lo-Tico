/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import com.example.shared.domain.location.PlatformLocationTracker
import dev.icerock.moko.permissions.PermissionsController

expect fun getLocationTracker(permissionsController: PermissionsController): PlatformLocationTracker

@Composable expect fun BindPlatformLocationTrackerEffect(locationTracker: PlatformLocationTracker)
