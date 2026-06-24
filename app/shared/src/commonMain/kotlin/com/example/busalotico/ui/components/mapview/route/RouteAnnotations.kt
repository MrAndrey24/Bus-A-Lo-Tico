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
package com.example.busalotico.ui.components.mapview.route

import androidx.compose.runtime.Composable
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.entities.RouteStop

@Composable
expect fun RouteAnnotations(
    polylinePoints: List<Coordinate>,
    stops: List<RouteStop>,
    busLocation: Coordinate?,
)
