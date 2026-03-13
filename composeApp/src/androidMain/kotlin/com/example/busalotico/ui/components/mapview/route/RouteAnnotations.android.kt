package com.example.busalotico.ui.components.mapview.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation

@Composable
actual fun RouteAnnotations(
    polylinePoints: List<Coordinate>,
    stops: List<RouteStop>
) {
    // Route Line
    if (polylinePoints.isNotEmpty()) {
        PolylineAnnotation(
            points = polylinePoints.map { Point.fromLngLat(it.longitude, it.latitude) }
        ) {
            lineColor = Color(0xFF007AFF) // Blue
            lineWidth = 6.0
            lineOpacity = 0.8
        }
    }


    // Stops
    stops.forEach { stop ->
        CircleAnnotation(point = Point.fromLngLat(stop.longitude, stop.latitude)) {
            circleRadius = 8.0
            circleColor = Color.Red
            circleStrokeWidth = 2.0
            circleStrokeColor = Color.White
        }
    }
}