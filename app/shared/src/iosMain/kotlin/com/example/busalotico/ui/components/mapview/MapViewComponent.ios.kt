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
import androidx.compose.ui.Modifier
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.entities.RouteStop

@Composable
actual fun MapViewComponent(
    modifier: Modifier,
    coordinate: Coordinate,
    recenterTrigger: Int,
    zoomInTrigger: Int,
    zoomOutTrigger: Int,
    stops: List<RouteStop>,
    polylinePoints: List<Coordinate>,
    busLocation: Coordinate?,
) {}
