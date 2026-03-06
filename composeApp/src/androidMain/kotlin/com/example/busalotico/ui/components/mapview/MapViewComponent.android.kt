package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.shared.domain.entities.Coordinate
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
actual fun MapViewComponent(
    modifier: Modifier,
    coordinate: Coordinate,
    recenterTrigger: Int,
    zoomInTrigger: Int,
    zoomOutTrigger: Int
) {
    val currentPoint = Point.fromLngLat(coordinate.longitude, coordinate.latitude)


    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(currentPoint)
            zoom(15.0)
        }
    }

    // Map GPS
    LaunchedEffect(coordinate) {
        mapViewportState.easeTo(
            cameraOptions {
                center(currentPoint)
                zoom(15.0)
            },
            MapAnimationOptions.mapAnimationOptions {
                duration(1000)
            }
        )
    }

    // Recenter GPS
    LaunchedEffect(recenterTrigger) {
        if (recenterTrigger > 0) {
            mapViewportState.easeTo(
                cameraOptions {
                    center(currentPoint)
                    zoom(15.0)
                },
                MapAnimationOptions.mapAnimationOptions { duration(800) }
            )
        }
    }

    // Zoom In
    LaunchedEffect(zoomInTrigger) {
        if (zoomInTrigger > 0) {
            val currentZoom = mapViewportState.cameraState?.zoom ?: 15.0
            mapViewportState.easeTo(
                cameraOptions {
                    zoom(currentZoom + 1.0)
                },
                MapAnimationOptions.mapAnimationOptions { duration(300) }
            )
        }
    }

    // Zoom Out
    LaunchedEffect(zoomOutTrigger) {
        if (zoomOutTrigger > 0) {
            val currentZoom = mapViewportState.cameraState?.zoom ?: 15.0
            mapViewportState.easeTo(
                cameraOptions {
                    zoom(currentZoom - 1.0)
                },
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
        CircleAnnotation(point = currentPoint) {
            circleRadius = 10.0
            circleColor = Color.Blue
            circleStrokeWidth = 3.0
            circleStrokeColor = Color.White
        }
    }
}