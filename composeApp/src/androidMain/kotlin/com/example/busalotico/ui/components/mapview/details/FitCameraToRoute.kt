package com.example.busalotico.ui.components.mapview.details

import androidx.compose.runtime.Composable
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo

@Composable
fun FitCameraToRoute(
    recenterTrigger: Int,
    polylinePoints: List<Coordinate>,
    stops: List<RouteStop>,
) {
    MapEffect(polylinePoints, stops, recenterTrigger) { mapView ->
        // We list the points we want to address
        val coordinatesToFrame = if (polylinePoints.isNotEmpty()) {
            polylinePoints.map { Point.fromLngLat(it.longitude, it.latitude) }
        } else if (stops.isNotEmpty()) {
            stops.map { Point.fromLngLat(it.longitude, it.latitude) }
        } else {
            emptyList()
        }

        if (coordinatesToFrame.isNotEmpty()) {
            // Center and zoom so that all points fit on the screen
            val cameraOptions = mapView.mapboxMap.cameraForCoordinates(
                coordinatesToFrame,
                EdgeInsets(100.0, 100.0, 100.0, 100.0) // Margins
            )

            // Center
            mapView.mapboxMap.easeTo(
                cameraOptions,
                MapAnimationOptions.mapAnimationOptions { duration(800) }
            )
        }
    }
}