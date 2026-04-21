/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop

@Composable
expect fun MapViewComponent(
    modifier: Modifier = Modifier,
    coordinate: Coordinate,
    recenterTrigger: Int,
    zoomInTrigger: Int = 0,
    zoomOutTrigger: Int = 0,
    stops: List<RouteStop> = emptyList(),
    polylinePoints: List<Coordinate> = emptyList(),
    busLocation: Coordinate?,
    flyToLocation: Coordinate? = null,
)
