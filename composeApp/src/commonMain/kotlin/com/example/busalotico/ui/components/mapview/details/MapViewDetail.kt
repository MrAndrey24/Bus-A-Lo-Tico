package com.example.busalotico.ui.components.mapview.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop

@Composable
expect fun MapViewDetail(
    modifier: Modifier,
    recenterTrigger: Int,
    zoomInTrigger: Int,
    zoomOutTrigger: Int,
    stops: List<RouteStop> = emptyList(),
    polylinePoints: List<Coordinate> = emptyList(),
    busLocation: Coordinate?

)