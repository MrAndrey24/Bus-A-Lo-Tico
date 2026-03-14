package com.example.busalotico.ui.components.mapview.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.busalotico.ui.components.mapview.route.RouteAnnotations
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo

@Composable
actual fun MapViewDetail(
    modifier: Modifier,
    recenterTrigger: Int,
    zoomInTrigger: Int,
    zoomOutTrigger: Int,
    stops: List<RouteStop>,
    polylinePoints: List<Coordinate>
) {
    val mapViewportState = rememberMapViewportState()

    // Zoom In (+)
    LaunchedEffect(zoomInTrigger) {
        if (zoomInTrigger > 0) {
            val currentZoom = mapViewportState.cameraState?.zoom ?: 15.0
            mapViewportState.easeTo(
                cameraOptions { zoom(currentZoom + 1.0) },
                MapAnimationOptions.mapAnimationOptions { duration(300) }
            )
        }
    }

    // Zoom Out (-)
    LaunchedEffect(zoomOutTrigger) {
        if (zoomOutTrigger > 0) {
            val currentZoom = mapViewportState.cameraState?.zoom ?: 15.0
            mapViewportState.easeTo(
                cameraOptions { zoom(currentZoom - 1.0) },
                MapAnimationOptions.mapAnimationOptions { duration(300) }
            )
        }
    }

    MapboxMap(
        modifier = modifier,
        mapViewportState = mapViewportState,
        style = {
            MapStyle(style = Style.MAPBOX_STREETS)
        }
    ) {

        MapEffect(polylinePoints, stops, recenterTrigger) { mapView ->
            // Recolectamos los puntos que queremos encuadrar
            val coordinatesToFrame = if (polylinePoints.isNotEmpty()) {
                polylinePoints.map { Point.fromLngLat(it.longitude, it.latitude) }
            } else if (stops.isNotEmpty()) {
                stops.map { Point.fromLngLat(it.longitude, it.latitude) }
            } else {
                emptyList()
            }

            if (coordinatesToFrame.isNotEmpty()) {
                // Centro y el zoom para que todos los puntos quepan en pantalla
                val cameraOptions = mapView.mapboxMap.cameraForCoordinates(
                    coordinatesToFrame,
                    EdgeInsets(100.0, 100.0, 100.0, 100.0) // Los márgenes
                )

                // Center
                mapView.mapboxMap.easeTo(
                    cameraOptions,
                    MapAnimationOptions.mapAnimationOptions { duration(800) }
                )
            }
        }

        RouteAnnotations(
            polylinePoints = polylinePoints,
            stops = stops
        )

    }
}
