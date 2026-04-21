/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.components.mapview.route

import androidx.compose.runtime.Composable
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop

@Composable
actual fun RouteAnnotations(
    polylinePoints: List<Coordinate>,
    stops: List<RouteStop>,
    busLocation: Coordinate?,
) {}
