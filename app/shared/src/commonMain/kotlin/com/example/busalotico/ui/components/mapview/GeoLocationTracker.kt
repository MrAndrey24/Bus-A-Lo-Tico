/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import com.example.sharedlogic.domain.location.PlatformLocationTracker
import dev.icerock.moko.permissions.PermissionsController

expect fun getLocationTracker(permissionsController: PermissionsController): PlatformLocationTracker

@Composable expect fun BindPlatformLocationTrackerEffect(locationTracker: PlatformLocationTracker)
