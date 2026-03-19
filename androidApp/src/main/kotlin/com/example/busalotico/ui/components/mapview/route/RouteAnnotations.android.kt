package com.example.busalotico.ui.components.mapview.route

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.busalotico.R
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.mapbox.geojson.Point
import com.mapbox.maps.ViewAnnotationOptions
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.viewannotation.geometry

@Composable
actual fun RouteAnnotations(
    polylinePoints: List<Coordinate>,
    stops: List<RouteStop>,
    busLocation: Coordinate?
) {
    // Route Line
    if (polylinePoints.isNotEmpty()) {
        PolylineAnnotation(
            points = polylinePoints.map { Point.fromLngLat(it.longitude, it.latitude) }
        ) {
            lineColor = Color(0xFF1D4ED8)
            lineWidth = 6.0
            lineOpacity = 0.8
        }
    }


    // Stops
    if (stops.isNotEmpty()){
        stops.forEach { stop ->
            ViewAnnotation(
                options = ViewAnnotationOptions.Builder()
                    .geometry(Point.fromLngLat(stop.longitude, stop.latitude))
                    .allowOverlap(true)
                    .build()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stops),
                    contentDescription = "Parada: ${stop.name}",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }


    // Bus
    if (busLocation != null) {
        ViewAnnotation(
            options = ViewAnnotationOptions.Builder()
                .geometry(Point.fromLngLat(busLocation.longitude, busLocation.latitude))
                .allowOverlap(true)
                .build()
        ) {
            Image(
                painter = painterResource(id = R.drawable.bus),
                contentDescription = "Bus",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}