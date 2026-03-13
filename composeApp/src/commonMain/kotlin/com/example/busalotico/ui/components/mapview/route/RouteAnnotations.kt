package com.example.busalotico.ui.components.mapview.route

import androidx.compose.runtime.Composable
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop

@Composable
expect fun RouteAnnotations(
    polylinePoints: List<Coordinate>,
    stops: List<RouteStop>
)